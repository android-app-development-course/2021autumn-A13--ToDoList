package com.android.minefragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarydb.eventBus.EventBusCheckGetBackEmptyMidMsg;
import com.android.librarydb.eventBus.EventBusCheckGetBackRefreshMidMsg;
import com.android.librarydb.eventBus.EventBusCheckHomeEmptyMidMsg;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GetBackActivity extends BaseActivity {

    private QMUIPullRefreshLayout qmuipulllayout;
    @Override
    protected int getLayoutId() {

        return R.layout.activity_get_back;
    }

    @Subscribe
    @Override
    protected void initView() {
        qmuipulllayout = findViewById(R.id.qmuipulllayout);
        EventBus.getDefault().register(this);//注册订阅者
        TopBar.setTopBarColor(GetBackActivity.this);
        TitleBar add_plan_top_bar=findViewById(R.id.get_back_top_bar);
        add_plan_top_bar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        qmuipulllayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                qmuipulllayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new EventBusCheckGetBackRefreshMidMsg(true));
                        qmuipulllayout.finishRefresh();
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusCheckGetBackEmptyMidMsg eventBusCheckGetBackEmptyMidMsg){
        if(eventBusCheckGetBackEmptyMidMsg.isIs_success()){
            LinearLayout empty_text=findViewById(R.id.empty_text00);
            LinearLayout loading_text=findViewById(R.id.loading_text00);
            if(!eventBusCheckGetBackEmptyMidMsg.isIs_empty()){
                loading_text.setVisibility(View.GONE);
                empty_text.setVisibility(View.GONE);
            }
            else{
                loading_text.setVisibility(View.GONE);
                empty_text.setVisibility(View.VISIBLE);
            }
        }
    }
}