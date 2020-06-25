package com.example.user.animdrawable;

public class Pos {
    boolean right;
    float X,Y;
    Pos(float a,float b,boolean r){X=a;Y=b;right=r;}
    Pos(Pos copy){X=copy.getX();Y=copy.getY();right=copy.getRight();}
    public void setX(float x) {
        if(x<0) x=0;
        if(x>MainActivity.DeviceWidth-420) x=MainActivity.DeviceWidth-420;
        X = x;
    }
    public void setY(float y) {
        if(y<0) y=0;
        if(y>MainActivity.DeviceHeight-300) y=MainActivity.DeviceHeight-300;
        Y = y;
    }
    public void setRight(boolean r){right=r;}
    public float getX() {return X;}
    public float getY() {return Y;}
    public boolean getRight() {return right;}
    public void set(float x,float y,boolean r){X=x;Y=y;right=r;}
    public void setPos(Pos copy){X=copy.getX();Y=copy.getY();right=copy.getRight();}
}
