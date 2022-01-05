package com.android.minefragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.topbarcolor.TopBar;
import com.xuexiang.xui.widget.actionbar.TitleBar;

@Route(path = "/Help/HelpActivity")
public class HelpActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initView() {
        TopBar.setTopBarColor(HelpActivity.this);
        TitleBar help_top_bar = findViewById(R.id.help_top_bar);
        help_top_bar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }
}