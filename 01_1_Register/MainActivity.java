//  ./app/src/main/java/com/example/user/register/MainActivity.java
package com.example.user.register;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public String [] colleges={"计算机学院","材料学院","外语学院","管理学院","工学院","化工学院","电子学院"};
    EditText ed1,ed2;
    CheckBox cb1,cb2,cb3;
    RadioGroup radioGroup;
    Spinner spinner;
    Switch sw;
    Button btn;
    public String hold="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.editText01);
        ed2=(EditText)findViewById(R.id.editText02);
        cb1=(CheckBox)findViewById(R.id.checkBox01);
        cb2=(CheckBox)findViewById(R.id.checkBox02);
        cb3=(CheckBox)findViewById(R.id.checkBox03);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.custom_simple_list_item_1,colleges);
        spinner.setAdapter(arrayAdapter);

        sw=(Switch)findViewById(R.id.switch1);
        btn=(Button)findViewById(R.id.button);

        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View vw){
                String report="用户名："+ed1.getText()+"\n密码："+ed2.getText()+"\n";
                String cbLine="爱好：";
                if(cb1.isChecked()){
                    cbLine+=cb1.getText();
                }
                if(cb2.isChecked()){
                    if(cbLine.length()==3) cbLine+=cb2.getText();
                    else cbLine+=","+cb2.getText();
                }
                if(cb3.isChecked()){
                    if(cbLine.length()==3) cbLine+=cb3.getText();
                    else cbLine+=","+cb3.getText();
                }
                if(cbLine.length()==3) report+="爱好：无\n";
                else report+=cbLine+"\n";
                if(radioGroup.getCheckedRadioButtonId()==-1){
                    report+="年级：\n";
                }
                else{
                    RadioButton rb=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                    report+="年级："+rb.getText().toString()+"\n";
                }
                report+="学院："+spinner.getSelectedItem().toString()+"\n";
                if(sw.isChecked()){report+="全日制学生：是\n";}
                else {report+="全日制学生：否\n";}
                DisplayToast(report);
            }
        });
    }

    public void DisplayToast(String str){
        Toast toast= Toast.makeText(this,str,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,100);
        toast.show();
    }
}
