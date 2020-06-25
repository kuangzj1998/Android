package com.example.user.grandwordremember;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
* DbOpenHandler：打开本地数据库   			DlgHandleWord：包含所有对话框操作的类
MainActivity：主界面类          			PrefActivity：配置界面类
Resolver：用于访问ContentProvider的ContentResolver类
SettingFragment：配置用的Fragment的类   	StatActivity：统计界面类
TestActivity：测验界面类   	TestAdapter：用作测验界面ListView的Adapter类（继承SimpleAdapter）
TestDb：本地数据库操作类   	TestRec：本地统计记录类   WordDb：（远程）词典操作类
WordRec：单词记录类
*/
public class MainActivity extends AppCompatActivity {
    String color="Red";
    int num=30;
    boolean if_stat=true;
    TestDb testDb;
    Resolver resolver;
    DlgHandleWord dlgHandleWord;
    ArrayList<String>stat=new ArrayList<String>();
    public Handler handler = new Handler() {
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 100://增加单词
                    String[] getWordData = msg.getData().getStringArray("re");
                    if (getWordData != null) {
                        int lv;
                        if (getWordData[2].equals("")) lv = 0;
                        else lv = Integer.parseInt(getWordData[2]);
                        if (lv < 0) lv = 0;
                        if (lv > 8) lv = 8;
                        boolean cover = getWordData[3].equals("t");
                        //先查询
                        Cursor cursor= resolver.query(getWordData[0]);
                        if (cover) {
                            //update or insert
                            if(cursor!=null&&cursor.getCount()==0){
                                resolver.insert(getWordData[0],getWordData[1],lv);
                            }
                            if(cursor!=null&&cursor.getCount()!=0){
                                resolver.update(getWordData[0],getWordData[1],lv);
                            }
                        }
                        else {
                            if(cursor!=null&&cursor.getCount()==0){
                                resolver.insert(getWordData[0],getWordData[1],lv);
                            }
                            if(cursor!=null&&cursor.getCount()!=0){
                                Toast.makeText(MainActivity.this,"The word already exists",Toast.LENGTH_SHORT).show();
                                resolver.delete(getWordData[0]);
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionbar=getSupportActionBar();
        actionbar.setTitle("学霸背单词");
        actionbar.setSubtitle("--快速记忆法");
        resolver=new Resolver(MainActivity.this);
        dlgHandleWord=new DlgHandleWord(MainActivity.this);
        testDb=new TestDb(MainActivity.this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_enter_test:
                intent = new Intent(this,TestActivity.class);
                intent.putExtra("color",color);
                intent.putExtra("num",num);
                intent.putExtra("if_stat",if_stat);
                startActivity(intent);
                return true;
            case R.id.action_new_word:
                dlgHandleWord.actionbar_add(handler);
                return true;
            case R.id.action_study_count:
                get_all_words();
                intent = new Intent(this,StatActivity.class);
                intent.putExtra("stat",stat);
                startActivity(intent);
                return true;
            case R.id.action_system_setting:
                intent = new Intent(this,PrefActivity.class);
                intent.putExtra("color",color);
                intent.putExtra("num",num);
                intent.putExtra("if_stat",if_stat);
                startActivityForResult(intent,5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==5&&resultCode==10){
            color= data.getStringExtra("color");
            num=data.getIntExtra("num",30);
            if_stat=data.getBooleanExtra("if_stat",true);
            Toast.makeText(MainActivity.this,color+" "+String.valueOf(num)+" "+String.valueOf(if_stat),Toast.LENGTH_SHORT).show();
        }
    }
    public void get_all_words() {
        if(stat.size()!=0) stat.clear();
        Cursor cursor = testDb.query("");
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                String w=cursor.getString(cursor.getColumnIndex("word"));
                int lv=cursor.getInt(cursor.getColumnIndex("level"));
                int tc=cursor.getInt(cursor.getColumnIndex("test_count"));
                int cc=cursor.getInt(cursor.getColumnIndex("correct_count"));
                stat.add(w+"&"+String.valueOf(lv)+"&"+String.valueOf(tc)+"&"+String.valueOf(cc));
            } while (cursor.moveToNext());
            cursor.close();
            Collections.sort(stat, new Comparator<String>() {
                @Override
                public int compare(String o1,String o2) {
                    int a=o1.indexOf('&'),b=o2.indexOf('&');
                    String s1 = o1.substring(0,a).toLowerCase();
                    String s2 = o2.substring(0,b).toLowerCase();
                    return s1.compareTo(s2);
                }
            });
        }
    }
}