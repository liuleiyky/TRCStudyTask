package com.liul.trc_study_task.contentprivider;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.liul.trc_study_task_common.FileManager;

import java.io.File;

public abstract class BaseDBHelper extends SQLiteOpenHelper {
    public BaseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        File parentFile=new File(name).getParentFile();
        if(!parentFile.exists())
            parentFile.mkdirs();
    }



}
