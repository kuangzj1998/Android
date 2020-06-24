package com.example.user.granddictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHandler extends SQLiteOpenHelper {
    private final String TAG="MySQLiteOpenHelper";
    public DbOpenHandler(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion){
        super(context,dbName,factory,dbVersion);
        Log.d(TAG,"MySQLiteOpenHelper");//这个方法自动产生数据库
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG,"OnCreate");
        db.execSQL("CREATE TABLE dict(_id integer primary key autoincrement, word varchar(64) unique, explanation text, level int default 0, modified_time timestamp)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        Log.d(TAG,"OnUpgrade");
    }
}
