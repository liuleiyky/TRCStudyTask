package com.liul.trc_study_task.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 内存缓存和磁盘缓存是ImageLoader的核心
 */
public class ImageLoader {
    private final int DISK_CACHE_SIZE=1024*1024*50;
    private DiskLruCache diskLruCache;
    private LruCache<String, Bitmap> lruCache;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;

    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private final int KEEP_ALIVE_SECONDS = 30;

    private final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    private final ThreadPoolExecutor threadPool;

    private final int MESSAGE_CODE_LOADBITMAP=0x1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_CODE_LOADBITMAP:
                    Bundle bundle = msg.getData();
                    ResultInfo resultInfo= (ResultInfo) bundle.getSerializable("ResultInfo");
                    if(resultInfo.url.equals(resultInfo.imageView.getTag()) ){
                        resultInfo.imageView.setImageBitmap(resultInfo.bitmap);
                    }
                    break;
            }
        }
    };

    public ImageLoader(Context context){
        //初始化LruCache和DiskLruCache
        long maxMemory=Runtime.getRuntime().maxMemory()/1024;
        int cacheSize= (int) (maxMemory/8);
        lruCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
        //初始化DiskLruCache
        File diskCacheFile = getDiskCacheDir(context);
        if(!diskCacheFile.exists())
            diskCacheFile.mkdirs();
        if(getUseableSpace(diskCacheFile)>DISK_CACHE_SIZE){
            try {
                diskLruCache = DiskLruCache.open(diskCacheFile,1,1,DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //初始化线程池
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(128),sThreadFactory);
        threadPool.allowCoreThreadTimeOut(true);
    }

    public void loadBitmap(final String url,final ImageView imageView,final int width,final int height){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                imageView.setTag(url);
                Bitmap bitmap = getBitmapFromCache(url, width, height);
                ResultInfo resultInfo=new ResultInfo(url,imageView,bitmap);
                if(bitmap!=null){
                    Message message = Message.obtain();
                    message.what=MESSAGE_CODE_LOADBITMAP;
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("ResultInfo",resultInfo);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 获取磁盘缓存目录
     * @param context
     * @return
     */
    private File getDiskCacheDir(Context context){
        String diskCacheDir;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                &&!Environment.isExternalStorageRemovable()){
            diskCacheDir=context.getExternalCacheDir().getPath();
        }else{
            diskCacheDir=context.getCacheDir().getPath();
        }
        return new File(diskCacheDir);
    }

    /**
     * 获取可用的磁盘空间
     * @param diskCacheFile
     * @return
     */
    private long getUseableSpace(File diskCacheFile){
        return diskCacheFile.getUsableSpace();
    }

    /**
     * 从缓存中获取Bitmap
     * @param url
     * @param width
     * @param height
     * @return
     */
    public Bitmap getBitmapFromCache(String url,int width, int height){
        Bitmap bitmap=null;
        String key = getHashKeyFromUrl(url);
        try{
            bitmap = getBitmapFromMemory(key);
            if(bitmap ==null)
                bitmap = loadBitmapFromDiskCache(key,width, height);
            else if(bitmap ==null)
                bitmap = loadBitmapToNet(key,width, height);
        }catch (Exception e){
        }
        return bitmap;
    }

    /**
     * 从内存缓存中获取Bitmap
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemory(String key){
        return lruCache.get(key);
    }

    /**
     * 将Bitmap添加到内存缓存中
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemory(String key,Bitmap bitmap){
        lruCache.put(key,bitmap);
    }

    /**
     * 从网络下载Bitmap并保存到磁盘缓存中
     * @param url
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public Bitmap loadBitmapToNet(String url,int width,int height) throws Exception{
        if(Looper.myLooper()==Looper.getMainLooper()){
            throw new RuntimeException("can not visit network in UI thread");
        }
        if(diskLruCache==null){
            return null;
        }
        String key = getHashKeyFromUrl(url);
        DiskLruCache.Editor edit = diskLruCache.edit(key);
        if(edit!=null){
            OutputStream outputStream = edit.newOutputStream(0);
            if(downloadBitmapToStream(url,outputStream)){
                edit.commit();
            }else{
                edit.abort();
            }
            diskLruCache.flush();
        }
        Bitmap bitmap = loadBitmapFromDiskCache(key, width, height);
        addBitmapToMemory(url,bitmap);
        return bitmap;
    }

    /**
     * 下载bitmap文件到输出流中
     * @param url
     * @param outputStream
     * @return
     */
    public boolean downloadBitmapToStream(String url,OutputStream outputStream ){
        try {
            URL urls=new URL(url);
            HttpURLConnection connection= (HttpURLConnection) urls.openConnection();
            BufferedInputStream bis=new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bos=new BufferedOutputStream(outputStream);
            int len;
            byte[] buffer=new byte[1024];
            while((len=bis.read(buffer))>0){
                bos.write(buffer,0,buffer.length);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if(bos!=null)
                    bos.close();
                if(bis!=null)
                    bis.close();
            }catch (Exception e){
            }
        }
        return false;
    }

    /**
     * 从磁盘缓存中加载Bitmap
     * @param key
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public Bitmap loadBitmapFromDiskCache(String key, int width, int height) throws IOException {
        if(diskLruCache!=null){
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            FileInputStream fis= (FileInputStream) snapshot.getInputStream(0);
            FileDescriptor fileDescriptor = fis.getFD();
            Bitmap bitmap = decodeFileDescriptor(fileDescriptor, width, height);
            return bitmap;
        }
        return null;
    }

    /**
     * 从文件描述对象中加载Bitmap
     * @param fileDescriptor
     * @param width
     * @param height
     * @return
     */
    private Bitmap decodeFileDescriptor(FileDescriptor fileDescriptor,int width,int height){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inJustDecodeBounds=false;
        int bitmapWidth=bitmap.getWidth();
        int bitmapHeight=bitmap.getHeight();
        options.inSampleSize=1;
        if(bitmapWidth>width||bitmapHeight>height){
            int scaleWidth=bitmapWidth/width;
            int scaleHeight=bitmapHeight/height;
            options.inSampleSize=scaleWidth>scaleHeight?scaleHeight:scaleWidth;
        }
        bitmap=BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        return bitmap;
    }

    /**
     * 从流中加载Bitmap
     * @param inputStream
     * @param width
     * @param height
     * @return
     */
    public Bitmap decodeSampleBitmapFromStream(InputStream inputStream, int width, int height){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
        options.inJustDecodeBounds=false;
        int bitmapWidth=bitmap.getWidth();
        int bitmapHeight=bitmap.getHeight();
        options.inSampleSize=1;
        if(bitmapWidth>width||bitmapHeight>height){
            int scaleWidth=bitmapWidth/width;
            int scaleHeight=bitmapHeight/height;
            options.inSampleSize=scaleWidth>scaleHeight?scaleHeight:scaleWidth;
        }
        bitmap=BitmapFactory.decodeStream(inputStream,null,options);
        return bitmap;
    }

    private String getHashKeyFromUrl(String url){
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA");
            digest.update(url.getBytes());
            byte[] digestData = digest.digest();
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i<digestData.length;i++){
                String hex = Integer.toHexString(digestData[i] & 0xFF);
                if(hex.length()==1){
                    stringBuilder.append('0');
                }
                stringBuilder.append(hex);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fis=new FileOutputStream(new File(""));
            FileWriter fileWriter=new FileWriter("");
            fileWriter.write("abc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(url.hashCode());
    }

    private class ResultInfo implements Serializable{
        private String url="";
        private ImageView imageView;
        private Bitmap bitmap;

        public ResultInfo(String url, ImageView imageView,Bitmap bitmap) {
            this.url = url;
            this.imageView = imageView;
            this.bitmap=bitmap;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

}
