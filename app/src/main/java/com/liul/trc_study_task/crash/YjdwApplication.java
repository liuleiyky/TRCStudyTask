package com.liul.trc_study_task.crash;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YjdwApplication extends MultiDexApplication {

    private static final String PATH=Environment.getExternalStorageDirectory().getPath()+"/crash/";
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        //设置线程默认的异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //导出异常信息到sd卡
                dumpExceptionToSDCard(e);

                //上传异常信息到服务器
                uploadExceptionToServer();

                //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己去结束自己
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(YjdwApplication.this, "程序发现异常,已记录到crash日志中.", Toast.LENGTH_SHORT)
                                .show();
                        Looper.loop();
                    }
                }.start();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if(mDefaultHandler !=null)
                    mDefaultHandler.uncaughtException(t,e);
                else
                    android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    private void dumpExceptionToSDCard(Throwable e) {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return;
        }
        File dir=new File(PATH);
        if(!dir.exists())
            dir.mkdirs();
        long currentTimeMillis = System.currentTimeMillis();
        String currentTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTimeMillis));
        File file=new File(PATH+currentTime);

        try {
//            PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(file)));
            PrintWriter pw=new PrintWriter(file);
            pw.println(currentTime);
            dumpPhoneInfo(pw);
            pw.println();
            e.printStackTrace(pw);
            pw.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.println("App Version:"+packageInfo.versionName+"_"+packageInfo.versionCode);

        //android版本号
        pw.println("OS Version："+ Build.VERSION.RELEASE+"_"+Build.VERSION.SDK_INT);
        //手机制造商
        pw.println("Vendor："+Build.MANUFACTURER);
        //手机型号
        pw.println("Model："+Build.MODEL);
        //CPU架构
        pw.println("CPU ABI："+Build.CPU_ABI);

    }

    private void uploadExceptionToServer() {

    }

}
