package com.liul.trc_study_task.crash;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liul.trc_study_task.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashCaughtActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_caught);

        Button btnCaughtCrash=(Button)findViewById(R.id.btnCaughtCrash);
        btnCaughtCrash.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                throw new RuntimeException("测试");
            }
        });


    }



}
