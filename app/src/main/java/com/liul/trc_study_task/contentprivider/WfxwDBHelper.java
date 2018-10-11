package com.liul.trc_study_task.contentprivider;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.liul.trc_study_task_common.FileManager;

import java.io.File;

public class WfxwDBHelper extends BaseDBHelper {

    private String string;

    public WfxwDBHelper(Context context) {
        super(context, FileManager.getInstance().getDDWfxwPath(), null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getWfgd(String wfxw){
        String wfgd="";
        SQLiteDatabase db = getReadableDatabase();
        String sql="select wfgd from wfxw where wfxw="+wfxw+"";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor!=null){
            wfgd= cursor.getString(0);
        }
        return wfgd;
    }
}
