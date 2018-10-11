package com.liul.trc_study_task_common;

import android.os.Environment;

import java.io.File;

public class FileManager {
    private String appName="trcstudy";
    private final String PATH_AUDIO="audio";
    private final String PATH_VIDEO="video";

    private final String PATH_DD="dd/";
    private final String PATH_WFXW=PATH_DD+"wfxw.db";
    private static FileManager mClient=null;
    private FileManager(){}
    public static FileManager getInstance(){
        if(mClient==null)
            mClient=new FileManager();
        return mClient;
    }

    public boolean createAppName(String appPath){
        appName=appPath;
        File file=new File(appPath);
        if(!file.exists()){
            return file.mkdirs();
        }
        return true;
    }

    public String getAudioPath(){
        return appName+"/"+PATH_AUDIO;
    }

    public String getVideoPath(){
        return appName+"/"+PATH_VIDEO;
    }

    public String getDDWfxwPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+appName+"/"+PATH_WFXW;
    }
}
