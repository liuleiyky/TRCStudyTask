package com.liul.trc_study_task.remoteview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.liul.trc_study_task.MyAppWidgetProvider1;
import com.liul.trc_study_task.R;
import com.liul.trc_study_task.scroll.ScrollActivity;

import java.util.Timer;
import java.util.TimerTask;

public class RemoteViewActivity extends AppCompatActivity {

    private Button btnSysNotication;
    private Button btnRemoteNotication;
    private Button btnWidget;
    private int a;
    private int b;
    private int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_view);

        btnSysNotication = (Button)findViewById(R.id.btnSysNotication);
        btnRemoteNotication = (Button)findViewById(R.id.btnRemoteNotication);
        btnWidget = (Button)findViewById(R.id.btnWidget);

        btnSysNotication.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RemoteViewActivity.this, ScrollActivity.class);
                intent.putExtra("data","通知"+(a));
                PendingIntent pendingIntent=PendingIntent.getActivity(RemoteViewActivity.this,
                        (b),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification=new Notification.Builder(RemoteViewActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("通知标题")
                        .setContentText("通知内容")
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.FLAG_AUTO_CANCEL)
                        .build();
                NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify((++c),notification);
            }
        });


        btnRemoteNotication.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //RemoteViews在通知栏上的应用
                Intent intent=new Intent(RemoteViewActivity.this, ScrollActivity.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(RemoteViewActivity.this,
                        0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification=new Notification.Builder(RemoteViewActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("通知标题")
                        .setContentText("通知内容")
                        .setDefaults(Notification.FLAG_AUTO_CANCEL)
                        .build();

                RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.activity_remote_view_notification);
                remoteViews.setTextViewText(R.id.tvTitle,"Title For RemoteViews");
                remoteViews.setTextViewText(R.id.tvContent,"Content For RemoteViews");
                remoteViews.setOnClickPendingIntent(R.id.tvContent,pendingIntent);
                notification.contentView=remoteViews;

                NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(1,notification);


            }
        });

        btnWidget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


            }
        });

//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
////                updateView();
//            }
//        },0,1000);

//        updateView();
    }

//    private int i;
//    private void updateView() {
//        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.appwidget_provider_info);
//        remoteViews.setTextViewText(R.id.btnAppWidget,""+(++i));
//
//        Intent intent=new Intent();
//        intent.setAction("click_action");
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
//        remoteViews.setOnClickPendingIntent(R.id.btnAppWidget,pendingIntent);
//
//        AppWidgetManager manager=AppWidgetManager.getInstance(getApplicationContext());
//        ComponentName componentName = new ComponentName(getApplicationContext(), MyAppWidgetProvider1.class);
//        manager.updateAppWidget(componentName, remoteViews);
//    }
}
