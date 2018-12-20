package com.liul.trc_study_task.window;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PermissionManager {
    private List<String> permissionList;
    public static int REQUEST_PERMISSION=1000;

    private static PermissionManager mClient;
    private  PermissionManager(){}
    public static PermissionManager getInstance(){
        if(mClient==null){
            mClient=new PermissionManager();
        }
        return mClient;
    }

    public static boolean needCheckPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            return true;
        else
            return false;
    }

    /**
     * 检查所有权限
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasSelfAllPermission(Activity activity){
        if(permissionList==null){
            initPermissionList();
        }
        Iterator<String> iterator = permissionList.iterator();
        while(iterator.hasNext()){
            String info = iterator.next();
            if(hasPermission(activity,info)){
                iterator.remove();
            }else{
                return false;
            }
        }
        return true;
    }

    private void initPermissionList(){
        permissionList=new ArrayList<String>();
//        permissionList.add(Manifest.permission.CAMERA);
//        permissionList.add(Manifest.permission.CALL_PHONE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void requestPermissions(Activity activity){
        if(permissionList!=null&&permissionList.size()>0){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity,permissions,REQUEST_PERMISSION);
        }
    }

    /**
     * 检查单个权限
     * @param context
     * @param permission
     * @return
     */
    public boolean hasPermission(Context context,String permission){
        if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED)
            return false;
        else
            return true;
    }

    /**
     * 清楚权限列表（必须在所有权限开启以后才能执行此操作）
     */
    public void clearPermissionList(){
        if(permissionList!=null&&permissionList.size()>0){
            permissionList.clear();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasAlertWindowPermission(Context context){
        if(!Settings.canDrawOverlays(context))
            return false;

        return true;
    }

    public static void requestAlerWindowPermission(Activity activity){
        Intent intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:"+activity.getPackageName()));
        activity.startActivityForResult(intent,REQUEST_PERMISSION);
    }
}
