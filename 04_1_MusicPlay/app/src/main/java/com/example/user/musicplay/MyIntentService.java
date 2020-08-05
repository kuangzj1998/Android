package com.example.user.musicplay;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class MyIntentService extends IntentService {
    public MyIntentService() {
        //线程名称的字符串
        super("MyIntentService");
    }

    // IntentService会使用单独的线程来执行该方法的代码
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent==null) return;
        //System.out.println("---IntentService onStart---");
        String music = intent.getStringExtra("Music");
        if(music!=null){
            System.out.println(music);
            if(music.equals("Waka")){MainActivity.mp = MediaPlayer.create(MainActivity.mainactivity,R.raw.waka);}
            else if(music.equals("Summer")){MainActivity.mp = MediaPlayer.create(MainActivity.mainactivity,R.raw.summer);}
            else if(music.equals("Wish")){MainActivity.mp = MediaPlayer.create(MainActivity.mainactivity,R.raw.wish);}
            else return;
            MainActivity.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            MainActivity.mp.setLooping(true);
            MainActivity.mp.start();
            MainActivity.mp.setVolume(14.0f, 14.0f); //声音调不了
        }
        while (MainActivity.state==1){}
        //System.out.println("---IntentService完成任务---");
    }
}
