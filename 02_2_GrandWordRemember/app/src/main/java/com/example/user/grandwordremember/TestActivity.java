package com.example.user.grandwordremember;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {
    static public int[] my_answer;
    String color;
    int num;
    boolean if_stat;
    ListView listView;
    Button submit;
    TextView test_word;
    MyAdapter adapter;
    Resolver resolver;
    TestDb testDb;
    ArrayList<Map<String,Object>> data_word;
    ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    int[] correct_answer;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar actionbar=this.getSupportActionBar();
        actionbar.setTitle("学霸背单词");
        actionbar.setSubtitle("单词测试");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        color= intent.getStringExtra("color");
        num=intent.getIntExtra("num",30);
        if_stat=intent.getBooleanExtra("if_stat",true);
        resolver=new Resolver(TestActivity.this);
        testDb=new TestDb(TestActivity.this);
        listView=(ListView)findViewById(R.id.test_word_listView);
        submit=(Button)findViewById(R.id.button_submit);
        submit.setVisibility(View.GONE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.INVISIBLE);
                //判断正确
                //if_stat入记录数据库
                if(if_stat){
                    for(int n=0;n<num;n++){
                        if(my_answer[n]==correct_answer[n]) testDb.insert((String)data_word.get(n).get("word"),(int)data_word.get(n).get("level"),1,1);
                        else testDb.insert((String)data_word.get(n).get("word"),(int)data_word.get(n).get("level"),1,0);
                    }
                }
                else {
                    for(int n=0;n<num;n++) testDb.insert((String)data_word.get(n).get("word"),(int)data_word.get(n).get("level"),0,0);
                }
                adapter=new MyAdapter(TestActivity.this,list,color,correct_answer,true);
                listView.setAdapter(adapter);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_start_test:
                make_question();
                submit.setVisibility(View.VISIBLE);
                adapter=new MyAdapter(this,list,color,correct_answer,false);
                listView.setAdapter(adapter);
                return true;
        }
        return true;
    }
    public void make_question(){
        //获取数据,num个,编制题目
        int has=(int)testDb.getCount();
        correct_answer=new int [num];
        my_answer=new int[num];
        for(int n=0;n<num;n++) my_answer[n]=-1;
        Cursor cursor=resolver.query_all();
        if(cursor!=null){
            //Toast.makeText(TestActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            cursor.moveToFirst();
            data_word=new ArrayList<Map<String, Object>>();
            while (has!=0){
                has--;
                cursor.moveToNext();
            }
            for(int n=0;n<num;n++){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("word", cursor.getString(cursor.getColumnIndex("word")));
                map.put("explanation", cursor.getString(cursor.getColumnIndex("explanation")));
                map.put("level", cursor.getInt(cursor.getColumnIndex("level")));
                data_word.add(map);
                cursor.moveToNext();
            }
            cursor.close();
            for(int n=0;n<num;n++){
                double d = Math.random();
                int i = (int)(d*4);
                correct_answer[n]=i;
                int[]num_as={-1,-1,-1,-1};
                num_as[i]=n;
                for(int j=0;j<4;j++){
                    if(j!=i){
                        while(true){
                            double dd = Math.random();
                            int rnd = (int)(dd*num);
                            boolean flag=true;
                            for(int k=0;k<4;k++) if(num_as[k]==rnd) flag=false;
                            if(flag){
                                num_as[j]=rnd;
                                break;
                            }
                        }
                    }
                }
                Map<String,Object>data=new HashMap<String,Object>();
                data.put("word",data_word.get(n).get("word"));
                data.put("as1",data_word.get(num_as[0]).get("explanation"));
                data.put("as2",data_word.get(num_as[1]).get("explanation"));
                data.put("as3",data_word.get(num_as[2]).get("explanation"));
                data.put("as4",data_word.get(num_as[3]).get("explanation"));
                list.add(data);
            }
        }
    }
}
