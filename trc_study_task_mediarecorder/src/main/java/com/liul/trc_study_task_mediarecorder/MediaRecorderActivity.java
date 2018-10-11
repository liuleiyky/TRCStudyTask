package com.liul.trc_study_task_mediarecorder;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liul.trc_study_task_common.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MediaRecorderActivity extends AppCompatActivity  implements SurfaceHolder.Callback,Camera.PreviewCallback{

    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Camera mCamera;
    private Button btnStartVR;
    private Button btnStopVR;
    private MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);

        btnStartVR = (Button) findViewById(R.id.btnStartVR);
        btnStopVR = (Button) findViewById(R.id.btnStopVR);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnStartVR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startMediaRecord();
            }
        });

        btnStopVR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopMediaRecord();
            }
        });

    }

    private void startMediaRecord() {
        recorder = new MediaRecorder();
        recorder.reset();
        //设置调用的摄像头
        mCamera.unlock();
        recorder.setCamera(mCamera);
        recorder.setOrientationHint(90);
        //指定Audio，video来源
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // 指定CamcorderProfile(需要API Level 8以上版本)
//        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        //使用CamcorderProfile做配置的话，输出格式，音频编码，视频编码 不要写
        // 设置输出格式和编码格式(针对低于API Level 8版本)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //设置输出格式，.THREE_GPP为3gp，.MPEG_4为mp4
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置声音编码类型 mic
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//设置视频编码类型，一般h263，h264
        // 保存到文件中(路径)
        String path = FileManager.getInstance().getVideoPath() + "/test.mp4";
        File saveFile = new File(path);
        saveFile.mkdirs();
        if (saveFile.exists()) {
            saveFile.delete();
        }
        recorder.setOutputFile(saveFile.getAbsolutePath());
        recorder.setVideoSize(640,480);//设置视频分辨率，设置错误调用start()时会报错，可注释掉在运行程序测试，有时注释掉可以运行
//     mediarecorder.setVideoFrameRate(24);//设置视频帧率，可省略
        recorder.setVideoEncodingBitRate(10*1024*1024);//提高帧频率，录像模糊，花屏，绿屏可写上调试
        recorder.setPreviewDisplay(holder.getSurface()); //设置视频预览
        try {
            // 准备录制
            recorder.prepare();
            // 开始录制
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
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
            mCamera.release();
            mCamera=null;
        }
    }

    // 根据手机方向获得相机预览画面应该旋转的角度
    public int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
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

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    private void stopMediaRecord(){
        if (recorder != null) {
            recorder.reset();
            recorder.release();
            recorder = null;
        }
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
        }

    }
}
