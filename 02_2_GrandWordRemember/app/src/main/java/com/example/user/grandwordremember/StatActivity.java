package com.example.user.grandwordremember;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatActivity extends AppCompatActivity {
    ArrayList<String> stat;
    ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    SimpleAdapter adapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        ActionBar actionbar=this.getSupportActionBar();
        actionbar.setTitle("学霸背单词");
        actionbar.setSubtitle("测验统计");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //获得需要显示的Arraylist
        Intent intent = getIntent();
        stat= intent.getStringArrayListExtra("stat");
        for(int n=0;n<stat.size();n++){
            String hold=stat.get(n);
            String[] temp=hold.split("&");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("word",temp[0]);
            map.put("level",temp[1]);
            map.put("test_count",temp[2]);
            map.put("correct_count",temp[3]);
            list.add(map);
        }
        ListView listView=(ListView)findViewById(R.id.listView30);
        adapter = new SimpleAdapter(this, list, R.layout.set_stat, new String[]{"word","level","test_count","correct_count"}, new int[]{R.id.textView39,R.id.textView38,R.id.textView37,R.id.textView36});
        listView.setAdapter(adapter);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
