package com.liul.trc_study_task;

import android.content.Intent;
import android.media.AudioTrack;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liul.trc_study_opengl.OpenGL20Activity;
import com.liul.trc_study_task_2.AudioRecordActivity;
import com.liul.trc_study_task_3.AudioTrackActivity;
import com.liul.trc_study_task_4.TRCStudyCameraActivity;
import com.liul.trc_study_task_common.FileManager;
import com.liul.trc_study_task_mediarecorder.MediaRecorderActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private final String appName="trcstudy";
    private Button btnAudioRecord;
    private Button btnPlay;
    private Button btnVideoRecord;
    private Button btnOpenGL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String sd= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+appName;
        FileManager.getInstance().createAppName(sd);

        btnAudioRecord = (Button) findViewById(R.id.btnAudioRecord);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnVideoRecord = (Button) findViewById(R.id.btnVideoRecord);
        btnOpenGL = (Button) findViewById(R.id.btnOpenGL);

        //录音
        btnAudioRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AudioRecordActivity.class));
            }
        });
        //播放音乐
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AudioTrackActivity.class));
            }
        });
        //录视频
        btnVideoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, TRCStudyCameraActivity.class));

                startActivity(new Intent(MainActivity.this, MediaRecorderActivity.class));
            }
        });
        //OpenGL绘制
        btnOpenGL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, OpenGL20Activity.class));
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
