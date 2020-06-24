package com.example.user.granddictionary;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DlgHandleWord {
    private Context context;
    private AlertDialog alertDialog = null;
    private AlertDialog.Builder dialogBuilder = null;
    DlgHandleWord(Context context){this.context=context;}
    public void actionbar_search(final Handler handler){
        final View view= LayoutInflater.from(context).inflate(R.layout.search_word,null);
        dialogBuilder = new AlertDialog.Builder(context);
        final EditText et=(EditText)view.findViewById(R.id.search_input);
        alertDialog = dialogBuilder
                .setIcon(R.drawable.dict)
                .setTitle("查询单词")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String getStr=et.getText().toString().trim();
                        //Toast.makeText(MainActivity.mainactivity,Getstr,Toast.LENGTH_SHORT).show();
                        Message msg = new Message();
                        msg.what=200;
                        Bundle bundle = new Bundle();
                        bundle.putString("re",getStr);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        alertDialog.dismiss();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.what=200;
                        Bundle bundle = new Bundle();//对数据包装后用MSG发送
                        bundle.putString("re","");
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        alertDialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000,600);
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setPadding(0,140,0,0);
    }
    public void actionbar_add(final Handler handler){
        final View view = LayoutInflater.from(MainActivity.mainactivity).inflate(R.layout.insert_word,null);
        dialogBuilder = new AlertDialog.Builder(MainActivity.mainactivity);
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
                        msg.what=400;
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
    public void pop_menu_rewrite(final Handler handler,final long id){
        final View view= LayoutInflater.from(MainActivity.mainactivity).inflate(R.layout.modify_word,null);
        dialogBuilder = new AlertDialog.Builder(MainActivity.mainactivity);
        final EditText et_word=(EditText)view.findViewById(R.id.rewrite_input_word);
        final EditText et_explan=(EditText)view.findViewById(R.id.rewrite_input_explanation);
        final EditText et_level=(EditText)view.findViewById(R.id.rewrite_input_level);
        alertDialog = dialogBuilder
                .setIcon(R.drawable.dict)
                .setTitle("修改单词")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String getword=et_word.getText().toString().trim();
                        String getexplan=et_explan.getText().toString().trim();
                        String getlv=et_level.getText().toString().trim();
                        Message msg = new Message();
                        msg.what=500;
                        Bundle bundle = new Bundle();//对数据包装后用MSG发送
                        // 要发送的是一个字符串数组，第一个参数是指定数据的名，当接收数据时可以用这个名来选择获取哪个数据，很方便，第二个参数是发送的数组
                        bundle.putStringArray("re",new String[]{String.valueOf(id),getword,getexplan,getlv});
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
        alertDialog.getWindow().setLayout(1000,1620);
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setPadding(0,140,0,0);
    }
    public void pop_menu_delete(final Handler handler,final String s,final long id){
        final View view= LayoutInflater.from(MainActivity.mainactivity).inflate(R.layout.search_word,null);
        dialogBuilder = new AlertDialog.Builder(MainActivity.mainactivity);
        alertDialog = dialogBuilder
                .setIcon(R.drawable.dict)
                .setTitle("删除单词")
                .setMessage("是否确定要输出单词'"+s+"'?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.what=300;
                        Bundle bundle = new Bundle();//对数据包装后用MSG发送
                        //bundle.putString("re",s);
                        bundle.putInt("re",(int)id);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                })
                .create();             // 创建AlertDialog对象
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setPadding(0,140,0,0);
    }
}
