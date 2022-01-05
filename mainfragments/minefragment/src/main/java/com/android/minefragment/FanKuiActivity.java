package com.android.minefragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.topbarcolor.TopBar;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.toast.XToast;

public class FanKuiActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {

        return R.layout.activity_fan_kui;
    }

    @Override
    protected void initView() {
        TopBar.setTopBarColor(FanKuiActivity.this);
        TitleBar add_plan_top_bar=findViewById(R.id.fun_kui_top_bar);
        add_plan_top_bar.setLeftClickListener(new View.OnClickListener() {
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

    public void sendSuccess(View view) {
        MultiLineEditText send_text=findViewById(R.id.send_text);
        if(send_text.isEmpty()) {
            XToast.warning(XUI.getContext(), "内容为空").show();
            return;
        }
        XToast.success(XUI.getContext(), "保存成功").show();
        finish();
    }
}