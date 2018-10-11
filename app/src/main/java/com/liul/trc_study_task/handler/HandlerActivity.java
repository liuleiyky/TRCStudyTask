package com.liul.trc_study_task.handler;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liul.trc_study_task.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HandlerActivity extends AppCompatActivity {

    private final int MSG_WHAT_NETREQUEST=10001;
    private final int MSG_WHAT_NATIVEFLIE=10002;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_WHAT_NETREQUEST:
                    tvBook.setText((String)msg.obj);
                    break;
                case MSG_WHAT_NATIVEFLIE:

                    break;
            }
        }
    };
    private Button btnHandler;
    private TextView tvBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        tvBook = (TextView)findViewById(R.id.tvBook);
        btnHandler = (Button)findViewById(R.id.btnHandler);
        btnHandler.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000*5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = Message.obtain();
                        message.what=MSG_WHAT_NETREQUEST;
                        message.obj="Android开发艺术探索";
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });


    }

}
