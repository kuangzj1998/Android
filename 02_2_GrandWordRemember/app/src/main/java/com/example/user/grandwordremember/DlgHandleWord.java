package com.example.user.grandwordremember;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DlgHandleWord {
    private Context context;
    private AlertDialog alertDialog = null;
    private AlertDialog.Builder dialogBuilder = null;
    DlgHandleWord(Context context){this.context=context;}

    public void actionbar_add(final Handler handler){
        final View view = LayoutInflater.from(context).inflate(R.layout.insert_word,null);
        dialogBuilder = new AlertDialog.Builder(context);
        final EditText et_word=(EditText)view.findViewById(R.id.add_input_word);
        final EditText et_explan=(EditText)view.findViewById(R.id.add_input_explanation);
        final EditText et_level=(EditText)view.findViewById(R.id.add_input_level);
        final CheckBox cb=(CheckBox)view.findViewById(R.id.checkbox);
        alertDialog = dialogBuilder
                .setIcon(R.drawable.dict)
                .setTitle("增加单词")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String getword=et_word.getText().toString().trim();
                        String getexplan=et_explan.getText().toString().trim();
                        String getlv=et_level.getText().toString().trim();
                        String getcheck=cb.isChecked()?"t":"f";
                        //Toast.makeText(MainActivity.mainactivity,Getstr,Toast.LENGTH_SHORT).show();
                        Message msg = new Message();
                        msg.what=100;
                        Bundle bundle = new Bundle();//对数据包装后用MSG发送
                        // 要发送的是一个字符串数组，第一个参数是指定数据的名，当接收数据时可以用这个名来选择获取哪个数据，很方便，第二个参数是发送的数组
                        bundle.putStringArray("re",new String[]{getword,getexplan,getlv,getcheck});
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000,1700);
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setPadding(0,140,0,0);
    }
    public void choose_color(String color,final Handler handler){
        final View view= LayoutInflater.from(context).inflate(R.layout.set_word_color,null);
        dialogBuilder = new AlertDialog.Builder(context);
        final RadioButton rd1,rd2,rd3,rd4;
        rd1=(RadioButton)view.findViewById(R.id.set_red);
        rd2=(RadioButton)view.findViewById(R.id.set_blue);
        rd3=(RadioButton)view.findViewById(R.id.set_green);
        rd4=(RadioButton)view.findViewById(R.id.set_black);
        switch (color){
            case "Red":
                rd1.setChecked(true);
                break;
            case "Blue":
                rd2.setChecked(true);
                break;
            case "Green":
                rd3.setChecked(true);
                break;
            case "Black":
                rd4.setChecked(true);
                break;
        }
        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd1.setChecked(true);
                Message msg = new Message();
                msg.what=105;
                Bundle bundle = new Bundle();
                bundle.putString("re","Red");
                msg.setData(bundle);
                handler.sendMessage(msg);
                alertDialog.dismiss();
            }
        });
        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd1.setChecked(true);
                Message msg = new Message();
                msg.what=105;
                Bundle bundle = new Bundle();
                bundle.putString("re","Blue");
                msg.setData(bundle);
                handler.sendMessage(msg);
                alertDialog.dismiss();
            }
        });
        rd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd1.setChecked(true);
                Message msg = new Message();
                msg.what=105;
                Bundle bundle = new Bundle();
                bundle.putString("re","Green");
                msg.setData(bundle);
                handler.sendMessage(msg);
                alertDialog.dismiss();
            }
        });
        rd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd1.setChecked(true);
                Message msg = new Message();
                msg.what=105;
                Bundle bundle = new Bundle();
                bundle.putString("re","Black");
                msg.setData(bundle);
                handler.sendMessage(msg);
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder
                .setTitle("选择单词颜色")
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000,950);
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setPadding(0,80,0,0);
    }
    public void set_test_num(final Handler handler){
        final View view= LayoutInflater.from(context).inflate(R.layout.set_test_num,null);
        dialogBuilder = new AlertDialog.Builder(context);
        final EditText et=(EditText)view.findViewById(R.id.set_test_num_input);
        alertDialog = dialogBuilder
                .setTitle("设置测试量")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String getStr=et.getText().toString().trim();
                        //Toast.makeText(MainActivity.mainactivity,Getstr,Toast.LENGTH_SHORT).show();
                        Message msg = new Message();
                        msg.what=205;
                        Bundle bundle = new Bundle();
                        bundle.putInt("re",Integer.parseInt(getStr));
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        alertDialog.dismiss();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000,600);
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setPadding(0,140,0,0);
    }
}
