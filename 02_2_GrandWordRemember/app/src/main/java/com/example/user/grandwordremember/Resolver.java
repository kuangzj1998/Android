package com.example.user.grandwordremember;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Resolver {
    private Uri uri ;
    private Context context;
    private ContentValues cv;
    private ContentResolver resolver;

    Resolver(Context context){
        this.context=context;
        uri = Uri.parse("content://com.example.user.granddictionary.DictProvider/dict");
        resolver = context.getContentResolver();
        cv=new ContentValues();
    }
    public Cursor query(String word) {
        //Cursor c =
        /*if(c!=null && c.moveToNext()){
            Log.d("测试","num:"+c.getString(0)+" name:"+c.getString(1));
        }*/
        return resolver.query(uri,new String[]{"word","explanation","level"}, "word=?", new String[]{word}, null);
    }
    public Cursor query_all(){
        return resolver.query(uri,new String[]{"word","explanation","level"},null,null,"word ASC");
    }
    public void insert(String word,String exp,int lv){
        cv.clear();
        cv.put("word",word);
        cv.put("explanation",exp);
        cv.put("level",lv);
        cv.put("modified_time",System.currentTimeMillis() / 1000);
        uri = resolver.insert(uri,cv);  // null--错误
        if(uri==null) Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show();
    }
    public void update(String word,String exp,int lv) {
        cv.clear();
        cv.put("word",word);
        cv.put("explanation",exp);
        cv.put("level",lv);
        cv.put("modified_time",System.currentTimeMillis() / 1000);
        int count = resolver.update(uri, cv, "word=?", new String[]{word});// 影响的记录数
    }
    public void delete(String word) {
        int count = resolver.delete(uri, "word=?", new String[]{word}); // 影响的记录数
    }
}
