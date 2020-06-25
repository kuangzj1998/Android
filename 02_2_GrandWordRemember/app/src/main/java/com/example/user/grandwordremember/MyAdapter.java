package com.example.user.grandwordremember;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class MyAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Map<String,Object>>list;
    private String color;
    private int[]answer;
    private boolean show_answer;
    public MyAdapter(Context context,ArrayList<Map<String,Object>>list,String color,int[]answer,boolean show_answer){
        this.context=context;
        this.list=list;
        this.color=color;
        this.answer=answer;
        this.show_answer=show_answer;
    }
    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public Object getItem(int position){
        return list.get(position);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        TextView tv;
        RadioGroup rg;
        RadioButton rb1,rb2,rb3,rb4;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.set_test, null, true);
        }
        tv=(TextView)convertView.findViewById(R.id.test_word);
        rb1=(RadioButton)convertView.findViewById(R.id.click_answer1);
        rb2=(RadioButton)convertView.findViewById(R.id.click_answer2);
        rb3=(RadioButton)convertView.findViewById(R.id.click_answer3);
        rb4=(RadioButton)convertView.findViewById(R.id.click_answer4);
        tv.setText((String)list.get(position).get("word"));
        rb1.setText((String)list.get(position).get("as1"));
        rb2.setText((String)list.get(position).get("as2"));
        rb3.setText((String)list.get(position).get("as3"));
        rb4.setText((String)list.get(position).get("as4"));
        rb1.setBackgroundColor(Color.WHITE);
        rb2.setBackgroundColor(Color.WHITE);
        rb3.setBackgroundColor(Color.WHITE);
        rb4.setBackgroundColor(Color.WHITE);
        if(show_answer){
            switch (answer[position]){
                case 0:
                    rb1.setBackgroundColor(Color.GRAY);
                    break;
                case 1:
                    rb2.setBackgroundColor(Color.GRAY);
                    break;
                case 2:
                    rb3.setBackgroundColor(Color.GRAY);
                    break;
                case 3:
                    rb4.setBackgroundColor(Color.GRAY);
                    break;
            }
        }
        switch (color){
            case "Red":
                tv.setTextColor(Color.RED);
                break;
            case "Blue":
                tv.setTextColor(Color.BLUE);
                break;
            case "Green":
                tv.setTextColor(Color.GREEN);
                break;
            case "Black":
                tv.setTextColor(Color.BLACK);
                break;
        }
        rg=(RadioGroup)convertView.findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(null);
        rg.clearCheck();
        switch (TestActivity.my_answer[position]){
            case 0:
                rg.check(R.id.click_answer1);break;
            case 1:
                rg.check(R.id.click_answer2);break;
            case 2:
                rg.check(R.id.click_answer3);break;
            case 3:
                rg.check(R.id.click_answer4);break;
            default:
                break;
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.click_answer1:
                        TestActivity.my_answer[position]=0;
                        break;
                    case R.id.click_answer2:
                        TestActivity.my_answer[position]=1;
                        break;
                    case R.id.click_answer3:
                        TestActivity.my_answer[position]=2;
                        break;
                    case R.id.click_answer4:
                        TestActivity.my_answer[position]=3;
                        break;
                    default:
                        TestActivity.my_answer[position]=-1;
                }
            }
        });
        return convertView;
    }
}
