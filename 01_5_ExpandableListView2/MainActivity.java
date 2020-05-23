package com.example.user.expandablelistview2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private String[] comp={"惠州候鸟","深圳光明"};
    private int[] icon={R.drawable.hzhn,R.drawable.szgm};
    private String[][] card={{"迎奥卡","候鸟都市精英卡"},{"二十年卡","十年卡"}};
    private String[][] price={{"29800元","58000元"},{"13.8万元","8.8万元"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView=(ExpandableListView)findViewById(R.id.ExpandableListView);
        ExpandableListAdapter adapter=new ExpandableListAdapter(this,comp,card,price);
        expandableListView.setAdapter(adapter);
        //expand_list_id.expandGroup(0);
        //关闭数组某个数组，可以通过该属性来实现全部展开和只展开一个列表功能
        //expand_list_id.collapseGroup(0);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                Toast.makeText(MainActivity.this,comp[groupPosition],Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //子视图的点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(MainActivity.this,card[groupPosition][childPosition],Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //用于当组项折叠时的通知。
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(MainActivity.this,"折叠:"+comp[groupPosition],Toast.LENGTH_SHORT).show();
            }
        });
        //用于当组项折叠时的通知。
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(MainActivity.this,"展开:"+comp[groupPosition],Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private String[] group;
        private String[][] child1;
        private String[][] child2;

        class GroupViewHolder{
            TextView parent_textview_id;
        }
        class ChildViewHolder{
            TextView child_card;
            TextView child_price;
            ImageView ICON;
        }

        public ExpandableListAdapter(Context context,String[]group,String[][] child1,String child2[][]){
            this.context=context;
            this.group=group;
            this.child1=child1;
            this.child2=child2;
        }

        @Override
        public int getGroupCount(){return group.length;}

        @Override
        public int getChildrenCount(int i) {return child1[i].length;}

        @Override
        public Object getGroup(int i) {return group[i];}

        @Override
        public Object getChild(int i,int j){return child1[i][j]+"+"+child2[i][j];}

        @Override
        public long getGroupId(int i) {return i;}

        @Override
        public long getChildId(int i, int i1) {return i1;}

        @Override
        public boolean isChildSelectable(int i, int i1) {return true;}

        @Override
        //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
        public boolean hasStableIds() {return true;}

        @Override
        public View getGroupView(int groupPosition,boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder groupViewHolder;
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandablelistview_groups,parent,false);
                groupViewHolder= new GroupViewHolder();
                groupViewHolder.parent_textview_id =(TextView)convertView.findViewById(R.id.parent_text);
                convertView.setTag(groupViewHolder);
            }
            else {
                groupViewHolder = (GroupViewHolder)convertView.getTag();
            }
            groupViewHolder.parent_textview_id.setText(group[groupPosition]);
            return convertView;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandablelistview_child,parent,false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.ICON=(ImageView)convertView.findViewById(R.id.pic);
                childViewHolder.child_card = (TextView)convertView.findViewById(R.id.card_name);
                childViewHolder.child_price = (TextView)convertView.findViewById(R.id.price);
                convertView.setTag(childViewHolder);
            }
            else{
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.ICON.setImageResource(icon[groupPosition]);
            childViewHolder.child_card.setText(child1[groupPosition][childPosition]);
            childViewHolder.child_price.setText(child2[groupPosition][childPosition]);
            return convertView;
        }
    }
}
