package com.liul.trc_study_task.contentprivider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.liul.trc_study_task.R;
import com.liul.trc_study_task.window.PermissionManager;
import com.liul.trc_study_task_common.FileManager;

import java.io.File;

public class ContentProviderActivity extends AppCompatActivity {
    private boolean successs=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);

//        Uri uri=Uri.parse("content://com.liul.trc_study_task.WfxwProvider/wfxwdmz");
//        Cursor cursor = getContentResolver().query(uri, new String[]{"wfgd"}, "wfxw = ?", new String[]{"60310"}, null);
//        Cursor cursor = getContentResolver().query(uri, new String[]{"mc"}, "dm = ?", new String[]{"3"}, null);
//        cursor.moveToFirst();
//        Log.d("ContentProviderActivity",cursor.getString(0));

    }


}
