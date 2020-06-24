package com.example.user.granddictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DictDb {
    private DbOpenHandler dbOpenHandler;

    public DictDb(Context context){
        this.dbOpenHandler=new DbOpenHandler(context,"dictionary.db",null,1);
        //delete("$all");
    }
    public void insert(String word,String exp,int lv){
        SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
        db.execSQL("INSERT INTO dict(word,explanation,level,modified_time) values(?,?,?,datetime('now','localtime'))",new Object[]{word,exp,lv});
        db.close();
    }
    public void delete(String word){
        SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
        if(word.equals("$all")) db.execSQL("DELETE FROM dict");
        else db.execSQL("DELETE FROM dict WHERE word=?",new Object[]{word});
        db.close();
    }
    public void update(String word,String exp,int lv){
        SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
        db.execSQL("UPDATE dict SET explanation=?,level=?,modified_time=datetime('now','localtime') WHERE word=?",new Object[]{exp,lv,word});
        db.close();
    }
    /*public void updateById(int id,String word,String exp,int lv){
        SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
        db.execSQL("UPDATE dict SET word=?,explanation=?,level=?,modified_time=datetime('now','localtime') WHERE _id=?",new Object[]{word,exp,lv,id});
        db.close();
    }*/
    public Cursor query(String cond) {
        SQLiteDatabase db=dbOpenHandler.getReadableDatabase();
        Cursor cursor = (cond.isEmpty()) ?
                db.rawQuery("select * from dict",null):
                db.rawQuery("select * from dict where word like ? or level like ?",new String[]{"%"+cond+"%","%"+cond+"%"},null);
        return cursor;
    }
    public long getCount() { //得到记录总数
        SQLiteDatabase db=dbOpenHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from dict", null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count;
    }
}
