package com.example.user.spacebattle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    //final float PERIOD = 30;
    Context context;                // 带入Activity
    private SurfaceHolder holder;
    private boolean isRun=true;
    //static float count;
    public GameObjects gameObjects;

    public GameSurfaceView(Context context) {
        super(context);
        this.context = context;   //可以采用(MainActivity)context得到Activity
        // TODO Auto-generated constructor stub
        holder = this.getHolder();
        holder.addCallback(this);         // 给SurfaceView当前的持有者一个回调对象
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        holder.setFormat(PixelFormat.TRANSPARENT);

    }

    @Override    // 在surface的大小发生改变时激发
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override  // 在创建时激发，一般在这里调用画图线程
    public void surfaceCreated(SurfaceHolder holder) {
        isRun = true;
        Global.realW = getWidth();    // 获取屏幕宽度
        Global.realH = getHeight();   // 获取屏幕高度
        //System.out.println("Real Width: "+realW+"Real Height: "+realH);
        gameObjects = new GameObjects(this.context,holder);
        Thread myThread = new Thread(this); // 创建一个绘图线程
        myThread.start();
    }

    @Override  // 销毁时激发，一般在这里将画图的线程停止、释放
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRun = false;
    }

    @Override
    public void run() {
        long start = 0;    // 开始时间
        long loopTime = 0;  // 每次循环的实际执行时间
        while(isRun) {
            start = System.currentTimeMillis();
            gameObjects.draw(loopTime);
            sleep((float)System.currentTimeMillis()-start);  // 睡眠一段时间
            loopTime =  System.currentTimeMillis()-start;    // 本次循环的实际执行时间
        }
    }

    // 睡眠一段时间，使每次循环的时间为PERIOD
    void sleep(float runTime){
        try {
            float leftTime = Global.LOOP_TIME - runTime;  // 剩余时间
            Thread.sleep(leftTime>0 ? (int)leftTime : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



