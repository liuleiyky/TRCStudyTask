//package com.liul.trc_study_task.contentprivider;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//public class WfxwProvider extends ContentProvider {
//
//    private SQLiteDatabase db;
//    private UriMatcher uriMatcher;
//    private final String AUTHORITY="com.liul.trc_study_task.WfxwProvider";
//    private final int CODE_WFXW=1;
//    private final int CODE_WFXWDMZL=2;
//    private final String TABLE_WFXW="wfxw";
//    private final String TABLE_WFXWDMZL="wfxwdmzl";
//
//    @Override
//    public boolean onCreate() {
//        WfxwDBHelper wfxwDBHelper = new WfxwDBHelper(getContext());
//        db = wfxwDBHelper.getReadableDatabase();
//
//        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(AUTHORITY,TABLE_WFXW,CODE_WFXW);
//        uriMatcher.addURI(AUTHORITY,TABLE_WFXWDMZL,CODE_WFXWDMZL);
//
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        Log.d("ContentProviderActivity","query");
////        String tables="";
////        int matchCode = uriMatcher.match(uri);
////        if(matchCode==CODE_WFXW){
////            tables=TABLE_WFXW;
////        }else if(matchCode==CODE_WFXWDMZL){
////            tables=TABLE_WFXWDMZL;
////        }
////        return db.query(tables,projection,selection,selectionArgs,null,null,sortOrder,null);
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public String getType(@NonNull Uri uri) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        return null;
//    }
//
//    @Override
//    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        return 0;
//    }
//
//    @Override
//    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
//        return 0;
//    }
//}
