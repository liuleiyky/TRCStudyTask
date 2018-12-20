package com.liul.trc_study_task.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liul.trc_study_task.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Notification notification=new Notification.Builder(this).build();

    }
}
