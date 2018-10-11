package com.liul.trc_study_task_2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioRecordActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 1001;
    private int sampleRateInHz=44100;
    private boolean isRecording=false;
    private Button btnStartRecord;
    private Button btnStopRecord;
    private FileOutputStream fos;
    private AudioRecord audioRecord;

    /**
     * 需要申请的运行时权限
     */
    private String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 被用户拒绝的权限列表
     */
    private List<String> mPermissionList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trcstudytask2_audio_record);

        btnStartRecord = (Button) findViewById(R.id.btnStartRecord);
        btnStopRecord = (Button) findViewById(R.id.btnStopRecord);

        initAudioRecord();

        //开始录音
        btnStartRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startAudioRecord();
                    }
                }).start();
            }
        });
        //停止录音
        btnStopRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                stopAudioRecord();

            }
        });
    }

    private void initAudioRecord() {

    }

    //开始录音
    private void startAudioRecord() {
        /**
         * android录音 API：AudioRecord
         * 实现android录音的流程：
         *      1、构造AudioRecord对象，其中最小的录音缓存buffer大小可以通过getMinBufferSize方法得到，如果buffer容量过小将导致构造对象失败
         *      2、初始化缓存buffer，大小大于等于AudioRecord对象的用于写声音数据的buffer大小
         *      3、开始录音
         *      4、创建一个数据流，一边从AudioRecord中读取声音数据到缓存buffer中，一边将缓存buffer中的数据导入流中
         *      5、关闭数据流
         *      6、停止录音
         */

        /**
         * sampleRateInHz：采样率
         * channelConfig：声道设置
         * audioFormat：采样率大小
         */
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        /**
         * audioResource：音频源
         * sampleRateInHz：采样率
         * channelConfig：声道设置
         * audioFormat：采样率大小
         * bufferSizeInBytes：采样数据需要的缓冲区大小
         */
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT,minBufferSize);

        //设置缓冲区,大小与采样数据需要的缓冲区大小一样
        byte[] buffer=new byte[minBufferSize];

        File file=new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
        file.mkdirs();
        if(file.exists()){
            file.delete();
        }
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //开始录音
        isRecording = true;
        audioRecord.startRecording();

        while(isRecording){
            //将数据读到缓冲区
            int read=audioRecord.read(buffer,0,minBufferSize);
            if(read!=AudioRecord.ERROR_INVALID_OPERATION){
                try {
                    fos.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        if(fos!=null){
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //停止录音
    private void stopAudioRecord(){
        isRecording=false;
        //关闭流
        if(fos!=null){
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //释放资源
        if(audioRecord!=null){
            audioRecord.stop();
            audioRecord.release();
            audioRecord=null;

        }
    }

    private void checkPermissions() {
        // Marshmallow开始才用申请运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) !=
                        PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            }
        }
    }

}
