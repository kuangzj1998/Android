package com.example.user.granddictionary;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadDict {
    private DictDb dictDb;
    private Context context;
    private int MAX_PROGRESS = 100;
    public int word_count = 100;
    public int get_word = 0;
    public int cur = 0;
    public static ProgressDialog pd;
    DownloadDict(Context context,DictDb dictDb){
        this.context=context;this.dictDb=dictDb;
    }
    static String getInputStreamText(InputStream is) throws Exception {
        InputStreamReader isr = new InputStreamReader(is, "utf8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb=new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
    public String getJSON() throws Exception{
        String apiUrl = "http://103.26.79.35:8080/dict/";
        URL url= new URL(apiUrl);
        URLConnection open = url.openConnection();
        InputStream inputStream = open.getInputStream();
        return getInputStreamText(inputStream);
    }
    public void start(Handler handler){
        showProgress(handler);
        //final wordList=null;
        new Thread() {
            @Override
            public void run() {
                try {
                    String data=getJSON();
                    JSONArray wordList =new JSONArray(data);
                    word_count=wordList.length();
                    for(int i=0;i<word_count;i++) {
                        JSONObject oj = wordList.getJSONObject(i);
                        String word = oj.getString("word");
                        Cursor cur = dictDb.query(word);
                        if (cur.getCount() == 0)
                            dictDb.insert(word, oj.getString("explanation"), oj.getInt("level"));
                        else dictDb.update(word, oj.getString("explanation"), oj.getInt("level"));
                        cur.close();
                        get_word++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    public void showProgress(final Handler handler) {
        MainActivity.progressStatus = 0;   // 将进度条的完成进度重设为0
        get_word = 0;
        pd = new ProgressDialog(MainActivity.mainactivity);
        pd.setMax(MAX_PROGRESS);
        pd.setTitle("下载词典");
        pd.setCancelable(false);   // 设置对话框不能用“取消”按钮关闭
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(false);          // 设置对话框的进度条是否显示进度
        pd.show();
        cur = 0;
        new Thread()   {
            public void run() {
                while (MainActivity.progressStatus < MAX_PROGRESS) {
                    MainActivity.progressStatus = MAX_PROGRESS  * get_word / word_count;   // 获取耗时操作的完成百分比
                    if(MainActivity.progressStatus > cur ){
                        cur = MainActivity.progressStatus;
                        handler.sendEmptyMessage(100); // 发送空消息到Handler
                    }

                }
                if (MainActivity.progressStatus >= MAX_PROGRESS) {  // 如果任务已经完成
                    pd.dismiss();   // 关闭对话框
                }
            }
        }.start();
    }
}
