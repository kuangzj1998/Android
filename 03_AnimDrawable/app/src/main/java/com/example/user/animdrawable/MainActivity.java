package com.example.user.animdrawable;

import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    static float DeviceHeight, DeviceWidth;
    static ArrayList<Pos>paint_path;
    Pos start,end,register;
    ImageView img,record;
    PathView Base;
    ArrayList<Pos>trace;
    boolean recording=false,forbidden=false;
    int replaying=-1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.bird);
        Base = (PathView) findViewById(R.id.Base);
        AnimationDrawable Bird = (AnimationDrawable) img.getBackground();
        Bird.start();
        record = (ImageView) findViewById(R.id.record);
        AnimationDrawable Rec = (AnimationDrawable) record.getBackground();
        Rec.start();
        start=new Pos(0f,0f,true);
        end=new Pos(0f,0f,true);
        register=new Pos(0f,0f,true);
        Base.post(new Runnable() {
            @Override
            public void run() {
                DeviceHeight = (float) Base.getHeight();
                DeviceWidth = (float) Base.getWidth();
            }
        });
        trace=new ArrayList<Pos>();
        paint_path=new ArrayList<Pos>();
        img.bringToFront();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recording_start:
                record.setVisibility(View.VISIBLE);
                trace.clear();
                recording=true;
                trace.add(new Pos(end));
                break;
            case R.id.recording_end:
                record.setVisibility(View.GONE);
                recording=false;
                break;
            case R.id.replay_start:
                //Toast.makeText(MainActivity.this, String.valueOf(DeviceHeight) + " " + String.valueOf(DeviceWidth), Toast.LENGTH_SHORT).show();
                if(replaying!=-1) {Toast.makeText(MainActivity.this,"正在回放", Toast.LENGTH_SHORT).show(); break;}
                if(recording) {Toast.makeText(MainActivity.this,"正在录像", Toast.LENGTH_SHORT).show(); break;}
                if(trace.size()<=0) {Toast.makeText(MainActivity.this,"没有录像", Toast.LENGTH_SHORT).show(); break;}
                register.setPos(end);
                if(trace.size()==1) {
                    replaying=0;forbidden=true;
                    set_position(trace.get(0));
                    TranslateAnimation no_act = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                    no_act.setDuration(3000);
                    no_act.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            set_position(register);
                            replaying=-1;
                            forbidden=false;
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    img.startAnimation(no_act);
                    break;
                }
                start.setPos(trace.get(0));
                replaying=1;
                end.setPos(trace.get(1));
                paint_path.clear();
                paint_path.add(new Pos(trace.get(0)));
                double rate=Euclidean_Distance(start,end)/Euclidean_Distance(new Pos(0f,0f,true),new Pos(DeviceHeight,DeviceWidth,true));
                long time=(long)(5000*rate);
                Translate(start.getX()/DeviceWidth, end.getX()/DeviceWidth, start.getY()/DeviceHeight, end.getY()/DeviceHeight,time);
                break;
            case R.id.replay_end:
                paint_path.clear();
                replaying=-1;
                Base.invalidate();
                img.clearAnimation();
                set_position(register);
                start.setPos(register);
                end.setPos(register);
                forbidden=false;
        }
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if(forbidden) break;
                end.setX(event.getX() - 210);
                end.setY(event.getY() - 240 - 150);
//                Toast.makeText(MainActivity.this, String.valueOf(endX) + " " + String.valueOf(endY), Toast.LENGTH_SHORT).show();
                if(recording) trace.add(new Pos(end));
                double rate=Euclidean_Distance(start,end)/Euclidean_Distance(new Pos(0f,0f,true),new Pos(DeviceHeight,DeviceWidth,true));
                long time=(long)(5000*rate);
                Translate(start.getX()/DeviceWidth, end.getX()/DeviceWidth, start.getY()/DeviceHeight, end.getY()/DeviceHeight,time);
                break;
        }
        return true;
    }
    public void Translate(float xs, float xe, float ys, float ye,long time) {
        forbidden=true;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
        lp.setMargins(0,0, 0, 0);
        img.setLayoutParams(lp);
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, xs, Animation.RELATIVE_TO_PARENT, xe, Animation.RELATIVE_TO_PARENT, ys, Animation.RELATIVE_TO_PARENT, ye);
        anim.setDuration(time);
        //anim.setRepeatMode();
        //anim.setRepeatCount(Animation.INFINITE);
        if(replaying>=1) anim.setStartOffset(500+1000);
        else anim.setStartOffset(500);
        anim.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.linear_interpolator));
        /*anim.setFillBefore(false);*/
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(start.getRight()) img.setRotationY(0f);
                else img.setRotationY(180f);
                end.setRight(start.getX() < end.getX());
                //Toast.makeText(MainActivity.this,String.valueOf(startX)+" "+String.valueOf(endX), Toast.LENGTH_SHORT).show();
                if(replaying>=1){
                    paint_path.add(new Pos(end));
                    Base.invalidate();
                }
                if (!end.getRight() && start.getRight()) {
                    ObjectAnimator oanim= ObjectAnimator.ofFloat(img, "rotationY", 0f, 180f);
                    oanim.setDuration(500);
                    if(replaying>=1) oanim.setStartDelay(1000);
                    oanim.setInterpolator(new LinearInterpolator());
                    oanim.start();
                }
                if (end.getRight() && !start.getRight()) {
                    ObjectAnimator oanim= ObjectAnimator.ofFloat(img, "rotationY", 180f, 0f);
                    oanim.setDuration(500);
                    if(replaying>=1) oanim.setStartDelay(1000);
                    oanim.setInterpolator(new LinearInterpolator());
                    oanim.start();
                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img.clearAnimation();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
                lp.setMargins((int) end.getX(), (int) end.getY(), 0, 0);
                img.setLayoutParams(lp);
                start.setPos(end);
                if(replaying>0){
                    replaying++;
                    if(replaying>=trace.size()){
                        replaying=-1;
                        return;
                    }
                    start.setPos(end);
                    end.setPos(trace.get(replaying));
                    double rate=Euclidean_Distance(start,end)/Euclidean_Distance(new Pos(0f,0f,true),new Pos(DeviceHeight,DeviceWidth,true));
                    long time=(long)(5000*rate);
                    Translate(start.getX()/DeviceWidth, end.getX()/DeviceWidth, start.getY()/DeviceHeight, end.getY()/DeviceHeight,time);
                }
                else forbidden=false;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        img.startAnimation(anim);
    }
    public double Euclidean_Distance(Pos s,Pos e){
        return Math.sqrt((s.getX()-e.getX())*(s.getX()-e.getX())+(s.getY()-e.getY())*(s.getY()-e.getY()));
    }
    public void set_position(Pos p){
        if(p.getRight()) img.setRotationY(0f);
        else img.setRotationY(180f);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
        lp.setMargins((int) p.getX(), (int) p.getY(), 0, 0);
        img.setLayoutParams(lp);
    }
}
