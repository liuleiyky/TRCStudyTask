package com.liul.trc_study_task.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;

public class BitmapUtil {

    public static Bitmap decodeSampleBitmapFromResource(Resources resources, int res, int width, int height){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, res, options);
        options.inJustDecodeBounds=false;
        int bitmapWidth=bitmap.getWidth();
        int bitmapHeight=bitmap.getHeight();
        options.inSampleSize=1;
        if(bitmapWidth>width||bitmapHeight>height){
            int scaleWidth=bitmapWidth/width;
            int scaleHeight=bitmapHeight/height;
            options.inSampleSize=scaleWidth>scaleHeight?scaleHeight:scaleWidth;
        }
        bitmap=BitmapFactory.decodeResource(resources,res,options);
        return bitmap;
    }

    public static Bitmap getSampleBitmapFromFile(String path,int width, int height){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        int bitmapWidth=bitmap.getWidth();
        int bitmapHeight=bitmap.getHeight();
        options.inJustDecodeBounds=false;
        options.inSampleSize=1;
        if(bitmapWidth>width||bitmapHeight>height){
            int scaleWidth=bitmapWidth/width;
            int scaleHeight=bitmapHeight/height;
            options.inSampleSize=scaleWidth>scaleHeight?scaleHeight:scaleWidth;
        }
        bitmap=BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    public static Bitmap getSampleBitmapFromByteArray(byte[] bytes,int width,int height){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        int bitmapWidth=bitmap.getWidth();
        int bitmapHeight=bitmap.getHeight();
        options.inJustDecodeBounds=false;
        options.inSampleSize=1;
        if(bitmapWidth>width||bitmapHeight>height){
            int scaleWidth=bitmapWidth/width;
            int scaleHeight=bitmapHeight/height;
            options.inSampleSize=scaleWidth>scaleHeight?scaleHeight:scaleWidth;
        }
        bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        return bitmap;
    }

    public static Bitmap getScaleBitmap(Bitmap src,int width,int height){
        int bitmapWidth=src.getWidth();
        int bitmapHeight=src.getHeight();
        int scaleSize=1;
        if(bitmapWidth>width||bitmapHeight>height){
            int scaleWidth=bitmapWidth/width;
            int scaleHeight=bitmapHeight/height;
            scaleSize=scaleWidth>scaleHeight?scaleHeight:scaleWidth;
        }

        Matrix matrix=new Matrix();
        matrix.postScale(scaleSize,scaleSize);

        return Bitmap.createBitmap(src,0,0,width,height,matrix,false);
    }


}
