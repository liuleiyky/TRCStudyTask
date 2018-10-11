package com.liul.trc_study_task.thread;

import android.app.IntentService;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liul.trc_study_task.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadActivity extends AppCompatActivity {

    private TextView tvTest;
    private Button btnTest;
    private EditText etTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        tvTest = (TextView)findViewById(R.id.tvTest);
        byte[] bytes = "a".getBytes();//(0xff&bytes[0])  Integer.toHexString(0xff&bytes[0])
        int a=bytes[0];
        boolean flag=false;

        tvTest.setText("");
        etTest = (EditText)findViewById(R.id.etTest);
        btnTest = (Button)findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                String str = etTest.getText().toString().trim();
                tvTest.setText("");
            }
        });
//        HandlerThread handlerThread=new HandlerThread("HandlerThread");
//        handlerThread.start();
//        Handler handler=new Handler(handlerThread.getLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//            }
//        };
//
//        NetAsyncTask netAsyncTask=new NetAsyncTask();
//        netAsyncTask.execute("");
//
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
//        fixedThreadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//        cachedThreadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(4);
//        scheduledThreadPool.schedule(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }
//        ,3, TimeUnit.SECONDS);
//
//
//        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//        singleThreadExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

    }

    class NetAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
