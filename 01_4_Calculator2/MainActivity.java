package com.example.user.calculator2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public TextView tv;
    public Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0;
    public Button btn_add,btn_sub,btn_mut,btn_div,btn_eql,btn_PAN,btn_dot,btn_del,btn_ce,btn_c;
    public int statu,oper,re;
    public String[] num=new String[2];
    public int[] sign=new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statu=0;oper=0;num[0]="0";num[1]="0";sign[0]=1;sign[1]=1;re=0;
        setbtn();
    }

    protected void show_num(){
        String head="";
        if(re>0) {head="Re:";re--;}
        if(statu<2){
            if(sign[statu]==1) tv.setText(head+num[statu]);
            else tv.setText(head+"-"+num[statu]);
        }
        else {
            String s="";
            if(oper==1) s="＋";if(oper==2) s="－";if(oper==3) s="×";if(oper==4) s="÷";
            if(sign[0]==1) tv.setText(head+num[0]+s);
            else tv.setText(head+"-"+num[0]+s);
        }
    }
    protected void clear_zero(int pos){
        if(num[pos].indexOf('.')==-1) return;
        int z=num[pos].length()-1;
        while(z!=0&&num[pos].charAt(z)=='0') z--;
        if(z!=num[pos].length()-1) num[pos]=num[pos].substring(0,z+1);
        if(num[pos].charAt(num[pos].length()-1)=='.') num[pos]=num[pos].substring(0,num[pos].length()-1);
    }
    protected void calculator(){
        clear_zero(0);
        clear_zero(1);
        Double a=Double.valueOf(num[0]);
        Double b=Double.valueOf(num[1]);
        a*=sign[0];b*=sign[1];
        //Toast.makeText(MainActivity.this,String.valueOf(a)+":"+String.valueOf(b),Toast.LENGTH_SHORT).show();
        if(oper==1) a+=b;
        if(oper==2) a-=b;
        if(oper==3) a*=b;
        if(oper==4) a/=b;
        if(a<0) {sign[0]=-1;a=-a;}
        else {sign[0]=1;}
        num[0]=String.valueOf(a);
        clear_zero(0);
        sign[1]=1;num[1]="0";oper=0;re++;
    }
    protected void setbtn(){
        tv=(TextView)findViewById(R.id.textView);
        btn_c=(Button)findViewById(R.id.C);
        btn_c.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                statu=0;oper=0;num[0]="0";num[1]="0";sign[0]=1;sign[1]=1;
                show_num();
            }
        });
        btn_ce=(Button)findViewById(R.id.CE);
        btn_ce.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    num[statu]="0";sign[statu]=1;
                }
                else{
                    statu=0;oper=0;num[0]="0";num[1]="0";sign[0]=1;sign[1]=1;
                }
                show_num();
            }
        });
        btn_0=(Button)findViewById(R.id.Zero);
        btn_0.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {return;}
                    else num[statu]+=String.valueOf(0);
                    show_num();
                }
            }
        });
        btn_1=(Button)findViewById(R.id.One);
        btn_1.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(1);}
                    else num[statu]+=String.valueOf(1);
                    show_num();
                }

            }
        });
        btn_2=(Button)findViewById(R.id.Two);
        btn_2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(2);}
                    else num[statu]+=String.valueOf(2);
                    show_num();
                }
            }
        });
        btn_3=(Button)findViewById(R.id.Three);
        btn_3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(3);}
                    else num[statu]+=String.valueOf(3);
                    show_num();
                }
            }
        });
        btn_4=(Button)findViewById(R.id.Four);
        btn_4.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(4);}
                    else num[statu]+=String.valueOf(4);
                    show_num();
                }
            }
        });
        btn_5=(Button)findViewById(R.id.Five);
        btn_5.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(5);}
                    else num[statu]+=String.valueOf(5);
                    show_num();
                }
            }
        });
        btn_6=(Button)findViewById(R.id.Six);
        btn_6.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(6);}
                    else num[statu]+=String.valueOf(6);
                    show_num();
                }
            }
        });
        btn_7=(Button)findViewById(R.id.Seven);
        btn_7.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(7);}
                    else num[statu]+=String.valueOf(7);
                    show_num();
                }
            }
        });
        btn_8=(Button)findViewById(R.id.Eight);
        btn_8.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(8);}
                    else num[statu]+=String.valueOf(8);
                    show_num();
                }
            }
        });
        btn_9=(Button)findViewById(R.id.Nine);
        btn_9.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) {num[statu]=String.valueOf(9);}
                    else num[statu]+=String.valueOf(9);
                    show_num();
                }
            }
        });
        btn_dot=(Button)findViewById(R.id.Dot);
        btn_dot.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].indexOf('.')==-1) {num[statu]+=".";}
                    else return;
                    show_num();
                }
            }
        });
        btn_PAN=(Button)findViewById(R.id.PAN);
        btn_PAN.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].equals("0")) return;
                    sign[statu]*=-1;
                    show_num();
                    //Toast.makeText(MainActivity.this,String.valueOf(statu)+":"+String.valueOf(sign[statu]),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_del=(Button)findViewById(R.id.Del);
        btn_del.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(num[statu].length()==1){
                        num[statu]="0";sign[statu]=1;
                    }
                    else{
                        num[statu]=num[statu].substring(0,num[statu].length()-1);
                        if(num[statu].equals("0")){sign[statu]=1;}
                    }
                }
                else{
                    statu=0;oper=0;num[0]="0";num[1]="0";sign[0]=1;sign[1]=1;
                }
                show_num();
            }
        });
        btn_add=(Button)findViewById(R.id.Add);
        btn_add.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(statu==1) calculator();
                    oper=1;
                    statu=2;
                    show_num();
                    statu=1;
                    return;
                }
                oper=1;
                show_num();
                statu=1;
            }
        });
        btn_sub=(Button)findViewById(R.id.Sub);
        btn_sub.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(statu==1) calculator();
                    oper=2;
                    statu=2;
                    show_num();
                    statu=1;
                    return;
                }
                oper=2;
                show_num();
                statu=1;
            }
        });
        btn_mut=(Button)findViewById(R.id.Multiply);
        btn_mut.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(statu==1) calculator();
                    oper=3;
                    statu=2;
                    show_num();
                    statu=1;
                    return;
                }
                oper=3;
                show_num();
                statu=1;
            }
        });
        btn_div=(Button)findViewById(R.id.Div);
        btn_div.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu<2){
                    if(statu==1) calculator();
                    oper=4;
                    statu=2;
                    show_num();
                    statu=1;
                    return;
                }
                oper=4;
                show_num();
                statu=1;
            }
        });
        btn_eql=(Button)findViewById(R.id.Equal);
        btn_eql.setOnClickListener(new Button.OnClickListener(){
            public void onClick(android.view.View vw){
                if(statu==1){
                    calculator();
                    statu=2;oper=0;
                    show_num();
                }
            }
        });
    }
}
