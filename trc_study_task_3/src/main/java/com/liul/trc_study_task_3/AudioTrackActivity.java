package com.liul.trc_study_task_3;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liul.trc_study_task_common.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * AudioTrack类可以完成android平台上音频数据的输出任务。AudioTrack有2种数据加载模式，MODE_STREAM和MODE_STATIC，分别是数据加载模式和音频流，
 *           对应着2中完全不同的使用场景
 *           MODE_STREAM模式下，
 */
public class AudioTrackActivity extends AppCompatActivity {

    private String musicFileName="test.pcm";
    private int sampleRate=44100;//采样率
    private int channelCout=AudioFormat.CHANNEL_IN_STEREO;//双声道声道
    private int audioFormat=AudioFormat.ENCODING_PCM_16BIT;//量化经度
    private int audioResource=MediaRecorder.AudioSource.MIC;//声音源：麦克风

    private boolean recording=false;

    private Button btnStartRecord;
    private Button btnStopRecord;
    private Button btnPlayMusic;
    private AudioTrack audioTrack;
    private AudioRecord audioRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trystudytask3_activity_audio_track);

        btnStartRecord= (Button)findViewById(R.id.btnStartRecord);
        btnStopRecord= (Button)findViewById(R.id.btnStopRecord);
        btnPlayMusic = (Button)findViewById(R.id.btnPlayMusic);

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

        //播放音乐
        btnPlayMusic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                playMusicByStream();

            }
        });

    }
    //开始录音
    private void startAudioRecord(){
        int mMinBufferSize=AudioRecord.getMinBufferSize(sampleRate,channelCout,audioFormat);
        audioRecord = new AudioRecord(audioResource,sampleRate,channelCout,audioFormat,mMinBufferSize);

        File musicFile=new File(FileManager.getInstance().getAudioPath(),musicFileName);
        musicFile.mkdirs();
        if(musicFile.exists()){
            musicFile.delete();
        }

        audioRecord.startRecording();
        recording=true;

        try {
            FileOutputStream fos=new FileOutputStream(musicFile);
            byte[] buffer=new byte[mMinBufferSize];

            while(recording){
                int read = audioRecord.read(buffer, 0, mMinBufferSize);
                if(read!=AudioRecord.ERROR_INVALID_OPERATION){
                    fos.write(buffer);
                }
            }
            fos.close();
        } catch (IOException e) {
        }


    }

    private void stopAudioRecord(){
        recording=false;
        if(audioRecord!=null){
            audioRecord.stop();
            audioRecord.release();
        }
    }

    //stream模式播放
    private void playMusicByStream(){
        final int minBufferSize = AudioTrack.getMinBufferSize(sampleRate,channelCout,audioFormat);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,channelCout,audioFormat,
                minBufferSize,
                AudioTrack.MODE_STREAM);
        audioTrack.play();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File musicFile=new File(FileManager.getInstance().getAudioPath(),musicFileName);
                    if(musicFile.exists()){
                        FileInputStream fis=new FileInputStream(musicFile);
                        byte[] tempBuffer = new byte[minBufferSize];
                        while (fis.available() > 0) {
                            int readCount = fis.read(tempBuffer);
                            if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                                    readCount == AudioTrack.ERROR_BAD_VALUE) {
                                continue;
                            }
                            if (readCount != 0 && readCount != -1) {
                                audioTrack.write(tempBuffer, 0, readCount);
                            }
                        }
                        //停止播放
                        audioTrack.stop();
                        //释放资源
                        audioTrack.release();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void releaseAudioTrack(){
        if(audioTrack!=null){
            audioTrack.stop();
            audioTrack.release();
        }
    }

    //static模式播放
    private void playMusicByStatic() {




    }

    @Override
    protected void onDestroy() {
        releaseAudioTrack();
        super.onDestroy();
    }
}
