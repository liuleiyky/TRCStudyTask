package com.liul.trc_study_task_4;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * 使用Camera API采集视频数据并保存到文件，分别使用SurfaceView、TextureView来预览Camera数据
 * 相机调整预览方向的实质是将数据进行旋转以后给SurfaceView进行显示
 */
public class TRCStudyCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,Camera.PreviewCallback{

    private SurfaceView surfaceView;
    private Camera mCamera;
    private SurfaceHolder holder;
    private boolean videoRecoding=false;
    private Button btnStartVR;
    private Button btnStopVR;

    int width = 1280;
    int height = 720;
    int framerate = 30;
    H264Encoder encoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtc_study_camera);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        btnStartVR = (Button) findViewById(R.id.btnStartVR);
        btnStopVR = (Button) findViewById(R.id.btnStopVR);

        encoder = new H264Encoder(width, height, framerate);
        encoder.startEncoder();

        //初始化Surface  1、
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnStartVR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        btnStopVR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });


    }

    private boolean supportH264Codec() {
        // 遍历支持的编码格式信息
        if (Build.VERSION.SDK_INT >= 18) {
            for (int j = MediaCodecList.getCodecCount() - 1; j >= 0; j--) {
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(j);

                String[] types = codecInfo.getSupportedTypes();
                for (int i = 0; i < types.length; i++) {
                    if (types[i].equalsIgnoreCase("video/avc")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void stopVideoRecord(){
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化相机，设置数据回调的格式
        mCamera = Camera.open();
        mCamera.setPreviewCallback(this);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setPreviewSize(1280, 720);
        mCamera.setParameters(parameters);
        try {
            //设置相机预览数据的显示器
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(getPreviewDegree(this));
            //开始预览
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
        }
        if (encoder != null) {
            encoder.stopEncoder();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (encoder != null) {
            encoder.putData(data);
        }
    }

    // 根据手机方向获得相机预览画面应该旋转的角度
    public int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    public int getBitmapDegreee(String path){
        int degree=0;
        try {
            ExifInterface exifInterface=new ExifInterface(path);
            //获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree=90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree=180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree=270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


}
