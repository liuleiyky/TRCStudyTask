package com.liul.trc_study_task.memoryleak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.liul.trc_study_task.R;

import java.lang.ref.WeakReference;

public class MemoryLeakActivity extends AppCompatActivity {
    private TextView textView;
    private WeakReference<MemoryLeakActivity> weakReference;
    private LoadHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);

        textView = (TextView)findViewById(R.id.textView);
        handler = new LoadHandler(this);

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    class LoadHandler extends Handler{
        public LoadHandler(MemoryLeakActivity activity) {
            weakReference = new WeakReference<MemoryLeakActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weakReference.get().textView.setText("");

        }
    }
}
