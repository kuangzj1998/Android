package com.example.user.musicplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    int PLAYING =1;
    int STOP = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg=intent.getStringExtra("Msg");
        if(msg!=null){
            if(msg.equals("Stop")){
                if(MainActivity.state==PLAYING){
                    MainActivity.mp.setLooping(false);
                    MainActivity.mp.stop();
                    MainActivity.state=STOP;
                }
            }
            else{
                try {
                    if(MainActivity.state==PLAYING) {
                        MainActivity.mp.stop();
                        MainActivity.mp.reset();
                        MainActivity.state=STOP;
                        Thread.sleep(500);
                    }
                    MainActivity.startIntentService(msg);
                    MainActivity.state=PLAYING;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
