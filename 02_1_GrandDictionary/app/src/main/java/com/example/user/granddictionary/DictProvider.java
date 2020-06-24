package com.example.user.granddictionary;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class DictProvider extends ContentProvider {
    public static final String AUTHORITY="com.example.user.granddictionary.DictProvider";
    private static final int MATCH_NUM=77;
    private static UriMatcher uriMatcher;
    private DbOpenHandler dbOpenHandler;
    public static final Uri NOTIFY_URI=Uri.parse("content://"+AUTHORITY);

    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"/dict",MATCH_NUM);
    }
    @Override
    public boolean onCreate()  {
        System.out.println("===onCreate is called===");
        this.dbOpenHandler=new DbOpenHandler(getContext(),"dictionary.db",null,1);
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri){    // 返回本ContentProvider所提供数据的MIME类型
        System.out.println("~~getType is called~~"+sh(uri));
        return null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String where, String[] whereArgs, String sortOrder) {
        if (uriMatcher.match(uri)==MATCH_NUM){
            SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
            Log.d("GD","query");
            return db.query("dict",projection,where,whereArgs,null,null,sortOrder,null);
        }
        return null;
    }
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues cv){
        System.out.println("===insert iscalled==="+cv.get("name")+sh(uri));
        if (uriMatcher.match(uri)==MATCH_NUM){
            SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
            Log.d("GD","insert");
            long rowID = db.insert("dict", null, cv);
            if(rowID > 0) {db.close();return ContentUris.withAppendedId(NOTIFY_URI, rowID);}

            //notifyChange();
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs){
        System.out.println("===delete is called==="+sh(uri));
        if (uriMatcher.match(uri)==MATCH_NUM){
            SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
            Log.d("GD","delete");
            int ret = db.delete("dict",where,whereArgs);
            db.close();
            notifyChange();
            return ret;
        }
        return 0;
    }
    @Override
    public int update(@NonNull Uri uri, ContentValues cv, String where,String[] whereArgs){
        System.out.println("===update is called==="+sh(uri));
        if (uriMatcher.match(uri)==MATCH_NUM){
            Log.d("GD","update");
            SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
            int ret = db.update("dict",cv,where,whereArgs);
            db.close();
            notifyChange();
            return ret;
        }
        return 0;
    }
    private void notifyChange(){
        getContext().getContentResolver().notifyChange(NOTIFY_URI,null);
    }
    private String sh(Uri uri){
        return "\r\n-----"+uri+"-----\r\n";
    }
}
