package com.android.functimeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.librarybase.basepage.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeLineFuncActivity extends BaseActivity {

    private RecyclerView Rv;
    private ArrayList<HashMap<String,Object>> listItem;
    private TimeLineAdapter timeLineAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_time_line_func;
    }

    @Override
    protected void initView() {
        Rv = findViewById(R.id.my_recycler_view);

    }

    @Override
    protected void initData() {
//        listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
//
//        HashMap<String, Object> map1 = new HashMap<String, Object>();
//        HashMap<String, Object> map2 = new HashMap<String, Object>();
//        HashMap<String, Object> map3 = new HashMap<String, Object>();
//        HashMap<String, Object> map4 = new HashMap<String, Object>();
//        HashMap<String, Object> map5 = new HashMap<String, Object>();
//        HashMap<String, Object> map6 = new HashMap<String, Object>();
//        HashMap<String, Object> map7 = new HashMap<String, Object>();
//        HashMap<String, Object> map8 = new HashMap<String, Object>();
//
//        map1.put("ItemTitle", "大学物理");
//        map1.put("ItemText", "北 404");
//        listItem.add(map1);
//
//        map2.put("ItemTitle", "高等数学");
//        map2.put("ItemText", "北 505");
//        listItem.add(map2);
//
//        map3.put("ItemTitle", "六级词汇");
//        map3.put("ItemText", "需学新词100个\n" +
//                "需复习单词50个");
//        listItem.add(map3);
//
//        map4.put("ItemTitle", "中国顺丰已收入");
//        map4.put("ItemText", "下一站广州华南理工大学");
//        listItem.add(map4);
//
//        map5.put("ItemTitle", "中国顺丰派件中");
//        map5.put("ItemText", "等待派件");
//        listItem.add(map5);
//
//        map6.put("ItemTitle", "华南理工大学已签收");
//        map6.put("ItemText", "收件人:Carson");
//        listItem.add(map6);
//
//        map7.put("ItemTitle", "华南理工大学已签收");
//        map7.put("ItemText", "收件人:Carson");
//        listItem.add(map7);
//
//        map8.put("ItemTitle", "华南理工大学已签收");
//        map8.put("ItemText", "收件人:Carson");
//        listItem.add(map8);
//
//        //使用线性布局
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        Rv.setLayoutManager(layoutManager);
//        Rv.setHasFixedSize(true);
//
//        //用自定义分割线类设置分割线
//        Rv.addItemDecoration(new DividerItemDecoration(this));
//
//        //为ListView绑定适配器
//        timeLineAdapter = new TimeLineAdapter(this,listItem);
//        Rv.setAdapter(timeLineAdapter);
    }

}