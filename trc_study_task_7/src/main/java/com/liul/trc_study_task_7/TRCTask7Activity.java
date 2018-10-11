package com.liul.trc_study_task_7;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * 在前面学习了AudioRecord、AudioTask、Camera、MediaExtractor、MediaMuxer API、MediaCodec。学习和使用了上述API以后，对android音视频系统处理
 * 有了一定经验和心得，本文及后面的几篇文章就是将这些知识串起来，做一些稍微复杂的事情。
 *
 * 串联整个音视频录制流程，完成音视频的采集、编码、封包成mp4输出
 *
 * android音视频采集的方法：预览用SurfaceView，视频采集用Camera，音频采集用AudioRecord
 *
 * 数据处理思路：使用MediaCodec类进行编码压缩，视频压缩为H.264，音频压缩为aac，使用MediaMuxer将音视频合成mp4
 *
 * 大致流程：
 *      1、收集Camera数据，并转码为H264存储到文件
 *      2、音视频采集+混合，存储到文件
 */
public class TRCTask7Activity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private Button btnStartVR;
    private Button btnStopVR;
    private Button btnStartAR;
    private Button btnStopAR;
    private Button btnMergeVA;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trc_task7);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        btnStartVR = (Button)findViewById(R.id.btnStartVR);
        btnStopVR = (Button)findViewById(R.id.btnStopVR);
        btnStartAR = (Button)findViewById(R.id.btnStartAR);
        btnStopAR = (Button)findViewById(R.id.btnStopAR);
        btnMergeVA = (Button)findViewById(R.id.btnMergeVA);

        //创建绘图表层（Surface）并添加回调
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mCamera = Camera.open();
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewFormat(ImageFormat.NV21);
                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        //开始录视频
        btnStartVR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startRecordVideo();
            }
        });
        //停止录视频
        btnStopVR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopRecordVideo();
            }
        });
        //开始录音频
        btnStartAR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startAudioRecord();
            }
        });
        //停止录音频
        btnStopAR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopAudioRecord();
            }
        });
        //合并音频和视频
        btnMergeVA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mergeVideoAndAudio();
            }
        });

    }

    private void startRecordVideo() {

    }

    private void stopRecordVideo() {

    }

    private void startAudioRecord() {

    }

    private void stopAudioRecord() {

    }

    private void mergeVideoAndAudio() {

    }
}
