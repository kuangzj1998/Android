package com.example.user.spacebattle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GameSurfaceView gameSurfaceView;
    MediaPlayer mp=null;
    boolean networkMode = false;
    String user_name = "";

    private MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if(mp!=null){
                mp.release();
                mp=null;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        gameSurfaceView = new GameSurfaceView(this);
        setContentView(gameSurfaceView);
        //背景音乐
        Intent intent = new Intent();
        intent.putExtra("Msg","Wish");
        intent.setAction("com.example.user.MyReceiver");
        sendBroadcast(intent);   //发送广播
        mp = new MediaPlayer();
        final View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.input_user,null);
        final EditText user_name_in = (EditText)view.findViewById(R.id.input_name);
        final EditText server_in = (EditText)view.findViewById(R.id.input_server);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = dialogBuilder
                .setIcon(R.drawable.sysu)
                .setTitle("输入用户")
                .setView(view)
                .setPositiveButton("连网", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user_name = user_name_in.getText().toString().trim();
                        String getserver = server_in.getText().toString().trim();
                        networkMode = true;
                        String report = "用户名："+user_name+"\n服务器："+getserver+"\n连网：是";
                        Toast.makeText(MainActivity.this,report,Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //alertDialog.dismiss();
                    }
                })
                .setNeutralButton("单机版", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user_name = user_name_in.getText().toString().trim();
                        String getserver = server_in.getText().toString().trim();
                        networkMode = true;
                        String report = "用户名："+user_name+"\n服务器："+getserver+"\n连网：否";
                        Toast.makeText(MainActivity.this,report,Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000,800);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //Toast.makeText(this,String.valueOf(event.getX())+" "+String.valueOf(event.getY()-240),Toast.LENGTH_SHORT).show();
                float dx = Global.r2Vx(event.getX());
                float dy = Global.r2Vy(event.getY()-240);
                String result = gameSurfaceView.gameObjects.getPressedButton(dx,dy);
                if(!result.equals("None")) {
                    System.out.println("按了["+result+"]按钮");
                    if(result.equals("开始")){
                        //if(GameObjects.sprites!=null) System.out.println(GameObjects.sprites.hmSprites.size());
                        if(GameObjects.myName.equals("")){
                            if(user_name.equals("")) user_name = String.valueOf(Global.ramdom(1000,9999));
                            GameObjects.sprites = new Sprites(MainActivity.this,user_name);
                        }
                        else if(GameObjects.mySprite==null){
                            String name = String.valueOf(Global.ramdom(1000,9999));
                            GameObjects.sprites.add(name,0,0,0,10,true,false);
                            GameObjects.sprites.myName = name;
                            GameObjects.myName = name;
                            GameObjects.mySprite = GameObjects.sprites.hmSprites.get(name);
                            Global.KILL = 0;
                        }
                        else if(!GameObjects.sprites.hmSprites.containsKey("other")){
                            GameObjects.sprites.add("other",Global.ramdom(0,Global.virtualW-200),Global.ramdom(0,Global.virtualH-200),Global.ramdom(0,360),8,true,true);
                            //GameObjects.sprites.add("other",100,100,Global.ramdom(0,360),15,false,true,true);
                        }
                    }
                    if(result.equals("开火")){
                        if(GameObjects.mySprite==null) break;
                        mp = MediaPlayer.create(MainActivity.this,R.raw.bullet);
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.setLooping(false);
                        mp.start();
                        mp.setVolume(14.0f, 14.0f); //声音调不了
                        mp.setOnCompletionListener(CompletionListener);
                        GameObjects.mySprite.shot();
                    }
                    if(result.equals("自动")){
                        if(GameObjects.mySprite==null) break;
                        GameObjects.mySprite.ai = !GameObjects.mySprite.ai;
                    }
                    if(result.equals("关闭")){
                        Intent intent = new Intent();
                        intent.putExtra("Msg","Stop");
                        intent.setAction("com.example.user.MyReceiver");
                        sendBroadcast(intent);   //发送广播
                        this.finish();
                    }
                }
                else {
                    if(GameObjects.mySprite==null) break;
                    GameObjects.mySprite.getDirection(GameObjects.mySprite.x,GameObjects.mySprite.y,dx,dy);

                }
                break;
        }
        return true;
    }
}
