package com.android.minefragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.librarybase.basepage.BaseActivity;

public class MineActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        findViewById(R.id.fan_kui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this,FanKuiActivity.class));
            }
        });
        findViewById(R.id.set_labels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this,SetLabelsActivity.class));
            }
        });
        findViewById(R.id.get_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this,GetBackActivity.class));
            }
        });
        findViewById(R.id.to_personal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this,PersonalActivity.class));
            }
        });
    }

    @Override
    protected void initData() {

    }


}