package com.example.user.granddictionary;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    DictDb dictDb;
    DlgHandleWord dw;
    ListView listView;
    TextView word, word_exp;
    ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    ArrayList<Map<String, Object>> list_backup;
    SimpleAdapter adapter;
    private boolean if_show_mean = false;
    public static int progressStatus = 0;  // 记录进度对话框的完成百分比
    public static MainActivity mainactivity;
////////////////////////////////////////////////////////////////////////////////////////////////
    private LinearLayout mGallery;
    private LayoutInflater mInflater;

    public Handler alert_handler = new Handler() {
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 100:
                    DownloadDict.pd.setProgress(progressStatus);
                    break;
                case 200://搜索单词  ok  except db
                    String getStr = msg.getData().getString("re");
                    if (getStr != null && !getStr.equals("")) {
                        Toast.makeText(MainActivity.mainactivity, getStr, Toast.LENGTH_SHORT).show();
                        list.clear();
                        for (int n = 0; n < list_backup.size(); n++) {
                            String s = (String) list_backup.get(n).get("word");
                            if (s.contains(getStr)) {
                                list.add(list_backup.get(n));
                            }
                        }
                        Collections.sort(list, new Comparator<Map<String, Object>>() {
                            @Override
                            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                String s1 = o1.get("word").toString().toLowerCase();
                                String s2 = o2.get("word").toString().toLowerCase();
                                return s1.compareTo(s2);
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 300://删除单词 ok except db
                    int getDel = msg.getData().getInt("re");
                    if (getDel >= 0 && getDel < list.size()) {
                        int n = search_list((String) list.get(getDel).get("word"), (String) list.get(getDel).get("explanation"), (int) list.get(getDel).get("level"));
                        list_backup.remove(n);
                        list.remove(getDel);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 400://增加单词  ok except db
                    String[] getWordData = msg.getData().getStringArray("re");
                    if (getWordData != null) {
                        int lv;
                        if (getWordData[2].equals("")) lv = 0;
                        else lv = Integer.parseInt(getWordData[2]);
                        if (lv < 0) lv = 0;
                        if (lv > 8) lv = 8;
                        boolean cover = getWordData[3].equals("t");
                        if (cover) {
                            for (int n = 0; n < list_backup.size(); n++) {
                                if (list_backup.get(n).get("word").equals(getWordData[0])) {
                                    list_backup.remove(n);
                                    break;
                                }
                            }
                            for (int n = 0; n < list.size(); n++) {
                                if (list.get(n).get("word").equals(getWordData[0])) {
                                    list.remove(n);
                                    break;
                                }
                            }
                        }
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("word", getWordData[0]);
                        map.put("explanation", getWordData[1]);
                        map.put("level", lv);
                        list.add(map);
                        list_backup.add(map);
                        Collections.sort(list, new Comparator<Map<String, Object>>() {
                            @Override
                            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                String s1 = o1.get("word").toString().toLowerCase();
                                String s2 = o2.get("word").toString().toLowerCase();
                                return s1.compareTo(s2);
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 500://修改单词  ok except db
                    String[] reWordData = msg.getData().getStringArray("re");
                    if (reWordData != null) {
                        int lv;
                        if (reWordData[3].equals("")) lv = 0;
                        else lv = Integer.parseInt(reWordData[3]);
                        if (lv < 0) lv = 0;
                        if (lv > 8) lv = 8;
                        int id = Integer.parseInt(reWordData[0]);
                        int n = search_list((String) list.get(id).get("word"), (String) list.get(id).get("explanation"), (int) list.get(id).get("level"));
                        Map<String, Object> map = list.get(id);
                        map.clear();
                        map.put("word", reWordData[1]);
                        map.put("explanation", reWordData[2]);
                        map.put("level", lv);
                        Map<String, Object> map1 = list_backup.get(n);
                        map1.clear();
                        map1.put("word", reWordData[1]);
                        map1.put("explanation", reWordData[2]);
                        map1.put("level", lv);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainactivity = this;
        ActionBar actionbar = this.getSupportActionBar();
        actionbar.setTitle("简明英文词典");
        actionbar.setSubtitle("中山大学");
        dictDb = new DictDb(MainActivity.this);
        listView = (ListView) findViewById(R.id.list_view);
        word = (TextView) findViewById(R.id.item_word);
        word_exp = (TextView) findViewById(R.id.item_describe);
        dw = new DlgHandleWord(MainActivity.this);
        /** 参数：context(上下文对象), datasource(数据源), itemlayout(每个Item的布局页面),
         *         from String[] 数据源中key的数组, to int[] 布局页面中id的数组  **/
        mInflater = LayoutInflater.from(this);
        mGallery = (LinearLayout) findViewById(R.id.horizontalScrollViewItemContainer);
        for (int i = 0; i <= 27; i++) {
            View view = mInflater.inflate(R.layout.horizontalscroll_view,mGallery, false);
            final TextView textView = (TextView)view.findViewById(R.id.char_button);
            if(i==0||i==27) textView.setText(" ");
            else textView.setText(String.valueOf((char)(64+i)));
            textView.setBackgroundColor(Color.rgb(176,128,128));
            final int tv_id=i;
            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(list.size()!=0) list.clear();
                    //遍历textview设置颜色
                    for(int j=0;j<=27;j++){
                        View v= mGallery.getChildAt(j);
                        TextView stv = (TextView)v.findViewById(R.id.char_button);
                        stv.setBackgroundColor(Color.rgb(176,128,128));
                    }
                    if(tv_id!=0&&tv_id!=27){
                        view.setBackgroundColor(Color.rgb(0,187,187));
                        for(int n=0;n<list_backup.size();n++){
                            String s=(String) list_backup.get(n).get("word");
                            if(s.toUpperCase().charAt(0)==(char)(64+tv_id)) list.add(list_backup.get(n));
                        }
                        show_all_mean(1);
                        return ;
                    }
                    for(int n=0;n<list_backup.size();n++){
                        list.add(list_backup.get(n));
                    }
                    show_all_mean(1);//1是指使用从头开始的模式
                }
            });
            mGallery.addView(view);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "short Click: " + list.get(position).get("word").toString(), Toast.LENGTH_SHORT).show();
                if(!if_show_mean) {
                    word.setVisibility(View.VISIBLE);
                    word_exp.setVisibility(View.VISIBLE);
                    word.setText((String) list.get((int) id).get("word"));
                    word_exp.setText((String) list.get((int) id).get("explanation"));
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, int position, final long id) {
                final String s = list.get(position).get("word").toString();
                final PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popmenu_delete:
                                dw.pop_menu_delete(alert_handler, s, id);
                                break;
                            case R.id.popmenu_update:
                                dw.pop_menu_rewrite(alert_handler, id);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;  //true: 只执行长按事件(ShortClick事件失效)
            }
        });
        get_all_words();
        //show_all_mean(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //Toast.makeText(this, "你点击了“搜索”按键！", Toast.LENGTH_SHORT).show();
                dw.actionbar_search(alert_handler);
                return true;
            case R.id.action_add:
                //Toast.makeText(this, "你点击了“添加”按键！", Toast.LENGTH_SHORT).show();
                dw.actionbar_add(alert_handler);
                return true;
            case R.id.action_download_word:
                //Toast.makeText(this, "你点击了“下载单词”按键！", Toast.LENGTH_SHORT).show();
                DownloadDict downloadDict = new DownloadDict(MainActivity.this, dictDb);
                downloadDict.start(alert_handler);
                return true;
            case R.id.action_show_mean:
                //Toast.makeText(this, "你点击了“显示词义”按键！", Toast.LENGTH_SHORT).show();
                if_show_mean = !if_show_mean;
                item.setChecked(if_show_mean);
                show_all_mean(0);
                word.setVisibility(View.GONE);
                word_exp.setVisibility(View.GONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void get_all_words() {
        Cursor cursor = dictDb.query("");
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("word", cursor.getString(cursor.getColumnIndex("word")));
                map.put("explanation", cursor.getString(cursor.getColumnIndex("explanation")));
                map.put("level", cursor.getInt(cursor.getColumnIndex("level")));
                list.add(map);
            } while (cursor.moveToNext());
            cursor.close();
            Collections.sort(list, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    String s1 = o1.get("word").toString().toLowerCase();
                    String s2 = o2.get("word").toString().toLowerCase();
                    return s1.compareTo(s2);
                }
            });
            list_backup = new ArrayList<Map<String, Object>>(list);
        }
    }

    public void show_all_mean(int mode) {
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        if (mode == 1) index = 0;
        if (if_show_mean) {
            adapter = new SimpleAdapter(this, list, R.layout.list_item_detail, new String[]{"word", "explanation"}, new int[]{R.id.detail_word, R.id.detail_describe});
            listView.setAdapter(adapter);
            listView.setSelectionFromTop(index, top);
        } else {
            adapter = new SimpleAdapter(this, list, R.layout.list_item, new String[]{"word", "explanation"}, new int[]{R.id.detail_word, R.id.detail_describe});
            listView.setAdapter(adapter);
            listView.setSelectionFromTop(index, top);
        }
    }

    public int search_list(String w, String e, int l) {
        for (int n = 0; n < list_backup.size(); n++) {
            String a = (String) list_backup.get(n).get("word");
            String b = (String) list_backup.get(n).get("explanation");
            int c = (int) list_backup.get(n).get("level");
            if (w.equals(a) && e.equals(b) && l == c) return n;
        }
        return -1;
    }
}


