package com.example.user.grandwordremember;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class PrefActivity extends AppCompatActivity {
    String color;
    int num;
    boolean if_stat;
    DlgHandleWord dlgHandleWord;
    public Handler handler = new Handler(){
        public void handleMessage(final Message msg){
            switch(msg.what){
                case 105:
                    String getStr = msg.getData().getString("re");
                    if (getStr != null && !getStr.equals("")) {
                        color=getStr;
                    }
                    final TextView answer1=(TextView)findViewById(R.id.answer1);
                    answer1.setText(color);
                    break;
                case 205:
                    int re = msg.getData().getInt("re");
                    if(re<0) re=0;
                    num=re;
                    final TextView answer2=(TextView)findViewById(R.id.answer2);
                    answer2.setText(String.valueOf(num));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionbar=this.getSupportActionBar();
        actionbar.setTitle("学霸背单词");
        actionbar.setSubtitle("系统设置");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        color= intent.getStringExtra("color");
        num=intent.getIntExtra("num",30);
        if_stat=intent.getBooleanExtra("if_stat",true);
        dlgHandleWord=new DlgHandleWord(PrefActivity.this);
        //
        final TextView answer1=(TextView)findViewById(R.id.answer1);
        final TextView answer2=(TextView)findViewById(R.id.answer2);
        final TextView answer3=(TextView)findViewById(R.id.answer3);
        final CheckBox checkBox=(CheckBox)findViewById(R.id.checkBox);
        final TextView clickbutton1=(TextView)findViewById(R.id.textView52);
        final TextView clickbutton2=(TextView)findViewById(R.id.textView55);
        //
        answer1.setText(color);
        answer2.setText(String.valueOf(num));
        answer3.setText(if_stat?"是":"否");
        checkBox.setChecked(if_stat);
        //
        clickbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgHandleWord.choose_color(color,handler);
            }
        });
        clickbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgHandleWord.set_test_num(handler);
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if_stat=!if_stat;
                answer3.setText(if_stat?"是":"否");
                checkBox.setChecked(if_stat);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("color",color);
                intent.putExtra("num",num);
                intent.putExtra("if_stat",if_stat);
                setResult(10, intent);
                finish();
                return true;
        }
        return true;
    }

}