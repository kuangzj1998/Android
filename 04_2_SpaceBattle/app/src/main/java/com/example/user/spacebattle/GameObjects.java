package com.example.user.spacebattle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import org.w3c.dom.Text;

public class GameObjects {
    private Context context;
    private SurfaceHolder holder;
    public Buttons buttons;
    static public Sprites sprites = null;
    static public Sprite mySprite = null;  // 本玩家精灵
    static public String myName = "";    // 本玩家精灵的名字
    static public Bullets bullets = null;
    private Paint BG,TEXT;
    private Bitmap bitmap;
    GameObjects(Context context,SurfaceHolder holder){
        this.context = context;
        this.holder = holder;
        buttons = new Buttons(context);
        buttons.pos();
        bullets = new Bullets(context);
        BG = new Paint();
        TEXT = new Paint();
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.cloud);
        TEXT.setColor(Color.RED);
        TEXT.setTextSize(Global.v2R(60));
    }
    void draw(long loopTime){
        Canvas canvas=null;
        try{
            canvas = holder.lockCanvas();
            //GameSurfaceView.count = GameSurfaceView.count + loopTime/1000.0f;   //每秒加1
            drawBackground(canvas);
            buttons.draw(canvas);
            if(sprites!=null) sprites.draw(canvas,loopTime);
            bullets.draw(canvas,loopTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(canvas!= null) {
                holder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。
            }
        }
    }
    private void drawBackground(Canvas canvas){
        if(canvas==null) return;
        canvas.drawBitmap(bitmap,0,0,BG);
        canvas.drawText("击杀数",Global.v2Rx(900),Global.v2Ry(80),TEXT);//文本的坐标是左下角
        if(Global.KILL>=10) canvas.drawText(String.valueOf(Global.KILL),Global.v2Rx(980),Global.v2Ry(200),TEXT);//文本的坐标是左下角
        else canvas.drawText(String.valueOf(Global.KILL),Global.v2Rx(1040),Global.v2Ry(200),TEXT);//文本的坐标是左下角
    }
    String getPressedButton(float x,float y){
        return buttons.getPressedButton(x,y);
    }
}
