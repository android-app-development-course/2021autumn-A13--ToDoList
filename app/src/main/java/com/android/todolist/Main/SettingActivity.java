package com.android.todolist.Main;


import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basemethod.TipDialog;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarydb.eventBus.EventBusLoginOrLogoutMidMsg;
import com.android.todolist.R;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.toast.XToast;

import org.greenrobot.eventbus.EventBus;

@Route(path = ARouterActivityPath.Setting.SETTINGACTIVITY)
public class SettingActivity extends BaseActivity {

    boolean card_beifen, tag_beifen,card_huoqu,tag_huoqu;

    private RelativeLayout btn_logout;
    private RelativeLayout beifen_plans,get_beifen_plans;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        TopBar.setTopBarColor(SettingActivity.this);
        TitleBar add_plan_top_bar = findViewById(R.id.settings_top_bar);
        beifen_plans=findViewById(R.id.beifen_plans);
        get_beifen_plans=findViewById(R.id.get_beifen_plans);
        btn_logout=findViewById(R.id.btn_logout);
        if (SharedStorage.isLogin(this)){
            btn_logout.setVisibility(View.VISIBLE);
        }
        else{
            btn_logout.setVisibility(View.GONE);
        }
        add_plan_top_bar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        //=====================退出登录==========================
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusLoginOrLogoutMidMsg(true,false));
                setResult(2);
                finish();
            }
        });

        beifen_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedStorage.isLogin(SettingActivity.this)){
                    XToast.error(SettingActivity.this,"请登录后再备份").show();
                    return;
                }
                TipDialog.showLoadingDialog(SettingActivity.this);
                SQLiteTools.addTagsToYunSql(SettingActivity.this);
                SQLiteTools.addCardsToYunSql(SettingActivity.this);

                EventBus.getDefault().post(new EventBusCardMidMsg(true));
            }
        });

        get_beifen_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedStorage.isLogin(SettingActivity.this)){
                    XToast.error(SettingActivity.this,"请登录后再获取已备份的信息").show();
                    return;
                }
                TipDialog.showLoadingDialog(SettingActivity.this);
                SQLiteTools.getAllTagsFromYunToSQLite(SettingActivity.this);
                SQLiteTools.getAllCardsFromYunToSQLite(SettingActivity.this);
//                XToast.success(SettingActivity.this,"获取云端的计划和标签成功").show();

                EventBus.getDefault().post(new EventBusCardMidMsg(true));
            }
        });

    }

    @Override
    protected void initData() {
        reset();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void EventBeifenBusReceive(EventBusBeifenMidMsg eventBusBeifenMidMsg){
//        if(eventBusBeifenMidMsg.getType()==EventBusBeifenMidMsg.beifen_card)card_beifen=true;
//        else if(eventBusBeifenMidMsg.getType()==EventBusBeifenMidMsg.beifen_tag)tag_beifen=true;
//        if (card_beifen&&tag_beifen){
//            reset();
//            TipDialog.showFinishDialog(SettingActivity.this,"备份完成");
//        }
//        else
//    }

    private void reset(){
        card_beifen=false;
        card_huoqu=false;
        tag_huoqu=false;
        tag_beifen =false;
    }
}