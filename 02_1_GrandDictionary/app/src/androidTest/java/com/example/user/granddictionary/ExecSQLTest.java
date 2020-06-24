package com.example.user.granddictionary;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExecSQLTest {
    private static final String TAG="测试";
    DictDb ddb;
    long ind=0;
    @Before
    public void createDatabase() throws Throwable{
        Context appContext = InstrumentationRegistry.getTargetContext();
        ddb=new DictDb(appContext);
        ind=ddb.getCount();
        Log.d(TAG,"建数据库成功");
    }
    @Test
    public void testInserts() throws Throwable{
        int count=10;
        for(int i=0;i<count;i++) testInsert();
    }
    @Test
    public void testInsert() throws Throwable{
        ind++;
        ddb.insert("be"+ind,"学生"+ind,1);
        Log.d(TAG,"插入成功");
    }
    @Test
    public void testUpdate() throws Throwable{
        ddb.update("be3","17005",3);
        Log.d(TAG,"修改成功");
    }
    @Test
    public void testDelete() throws Throwable{
        ddb.delete("be2");
        Log.d(TAG,"删除成功");
    }
    @Test
    public void testGetCount() throws Throwable{
        Log.d(TAG,"总记录数："+ ddb.getCount());
    }
    @Test
    public void testQueryAll() throws Throwable{
        Cursor cursor=ddb.query("");
        while (cursor.moveToNext()){
            Log.d(TAG,cursor.getLong(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4));
        }
        cursor.close();
    }
    @Test
    public void clearall() throws Throwable{
        while (ddb.getCount()>0)
            ddb.delete("$all");
    }
}
