package com.example.user.animdrawable;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PathView extends View{
    Path path;
    Paint paint;

    public PathView(Context context, AttributeSet attrs){
        super(context,attrs);
        paint = new Paint();
        paint.setColor(Color.rgb(255,201,14));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(40);
        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER));
        path=new Path();
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(MainActivity.paint_path.size()<=1) return;
        path.reset();
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        path.moveTo(MainActivity.paint_path.get(0).getX()+210,MainActivity.paint_path.get(0).getY()+150);
        for(int n=1;n<MainActivity.paint_path.size();n++){
            Log.d("Path",String.valueOf(n));
            path.lineTo(MainActivity.paint_path.get(n).getX()+210,MainActivity.paint_path.get(n).getY()+150);
        }
        //invalidate();
        canvas.drawPath(path,paint);
    }
}