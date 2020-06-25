package com.example.user.grandwordremember;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TestDb {
    private DbOpenHandler dbOpenHandler;

    public TestDb(Context context){
        this.dbOpenHandler=new DbOpenHandler(context,"remember.db",null,1);
        //delete("$all");
    }
    //word varchar(64) unique,level int default 0, test_count int default 0, correct_count int default 0,last_test_time timestamp
    public void insert(String word,int lv,int test_count,int correct_count){
        SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
        db.execSQL("INSERT INTO words(word,level,test_count,correct_count,last_test_time) values(?,?,?,?,datetime('now','localtime'))",new Object[]{word,lv,test_count,correct_count});
        db.close();
    }
    public void delete(String word){
        SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
        if(word.equals("$all")) db.execSQL("DELETE FROM words");
        else db.execSQL("DELETE FROM words WHERE word=?",new Object[]{word});
        db.close();
    }
    public void update(String word,int lv,int test_count,int correct_count){
        SQLiteDatabase db=dbOpenHandler.getWritableDatabase();
        db.execSQL("UPDATE words SET level=?,test_count=?,correct_count=?,last_test_time=datetime('now','localtime') WHERE word=?",new Object[]{lv,test_count,correct_count,word});
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
                db.rawQuery("select * from words",null):
                db.rawQuery("select * from words where word like ? or level like ?",new String[]{"%"+cond+"%","%"+cond+"%"},null);
        return cursor;
    }
    public long getCount() { //得到记录总数
        SQLiteDatabase db=dbOpenHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from words", null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count;
    }
}
