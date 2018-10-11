package com.liul.trc_study_task.window;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.liul.trc_study_task.R;
import com.liul.trc_study_task.mvp.BookShowActivity;

import java.util.ArrayList;
import java.util.List;

public class WindowActivity extends AppCompatActivity {

    private Button btnCall;//,Manifest.permission.CALL_PHONE  Manifest.permission.SYSTEM_ALERT_WINDOW
//    private final String[] PERMISSIONS={Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE};
    private List<PermissionInfo> permssionList=new ArrayList<PermissionInfo>();
    private List<String> quesPermssionList=new ArrayList<String>();
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查危险权限
//                if(PermissionManager.getInstance().hasSelfAllPermission(WindowActivity.this)){
//                    //有相应权限
//                    Toast.makeText(WindowActivity.this,"具备相应权限",Toast.LENGTH_SHORT).show();
//                }else{
//                    PermissionManager.getInstance().requestPermissions(WindowActivity.this);
//                }
                //特殊权限需要引导用户跳转到设置页面去开启开关
//                if(PermissionManager.hasAlertWindowPermission(WindowActivity.this)){
//                    showFloatingButton();
//                }else{
//                    PermissionManager.requestAlerWindowPermission(WindowActivity.this);
//                }

                startActivity(new Intent(WindowActivity.this, BookShowActivity.class));
            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            showFloatingButton();
        }
    }
    private int lastX;
    private int lastY;
    private void showFloatingButton() {
        final Button floatingButton=new Button(this);
        floatingButton.setText("悬浮窗");
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT
                ,WindowManager.LayoutParams.WRAP_CONTENT
                ,1999
                ,WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                , PixelFormat.TRANSPARENT);
        layoutParams.gravity=Gravity.LEFT|Gravity.TOP;
        layoutParams.x=100;
        layoutParams.y=300;
        windowManager = getWindowManager();
        windowManager.addView(floatingButton, layoutParams);

        floatingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                int rawX=(int)event.getRawX();
//                int rawY=(int)event.getRawY();
                int x=(int)event.getX();
                int y=(int)event.getY();
                Log.d("WindowActivity",(int)floatingButton.getLeft()+","+(int)floatingButton.getTranslationX());
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x=layoutParams.x+(x-lastX);
                        layoutParams.y=layoutParams.y+(y-lastY);
                        windowManager.updateViewLayout(floatingButton,layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                lastX=x;
                lastY=y;
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(PermissionManager.getInstance().hasSelfAllPermission(WindowActivity.this)){
//            Toast.makeText(this,"所有权限开启成功",Toast.LENGTH_SHORT).show();
//            //所有权限开启成功
//
//        }else{
//            //弹窗提示继续权限检查或退出程序
//            PermissionManager.getInstance().requestPermissions(WindowActivity.this);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PermissionManager.REQUEST_PERMISSION){
            if(PermissionManager.hasAlertWindowPermission(WindowActivity.this)){
                Toast.makeText(this,"系统悬浮穿权限开启成功",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
