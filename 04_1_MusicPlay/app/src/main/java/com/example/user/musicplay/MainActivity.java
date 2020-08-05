package com.example.user.musicplay;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static MediaPlayer mp=null;
    static int state = 0;
    static Context mainactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainactivity=MainActivity.this;

        mp = new MediaPlayer();
        final Spinner spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                String str = (String) spinner.getSelectedItem();
                Intent intent = new Intent("com.example.user.MyReceiver");
                intent.putExtra("Msg", str);
                sendBroadcast(intent); //发送广播
                /*Toast toast=Toast.makeText(MainActivity.this,str, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,360);
                toast.show();*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    static public void startIntentService(String msg){
        // 创建需要启动的IntentService的Intent
        Intent intent = new Intent(mainactivity, MyIntentService.class);
        // 启动IntentService
        intent.putExtra("Music",msg);
        mainactivity.startService(intent);
    }
}
