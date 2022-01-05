package com.android.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.constant.FilePath;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.todolist.Main.MainPageActivity;
import com.xuexiang.xui.XUI;

import java.util.Timer;
import java.util.TimerTask;

public class AppActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        return R.layout.activity_app;
    }

    @Override
    protected void initView() {
        TopBar.setTopBarColor(this);
        ARouter.getInstance().inject(this);
        //===================================欢迎页后跳转登录界面====================================
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
//            //==========================判断是否已登录===============================
//            if(!getSharedPreferences(FilePath.LOGIN_MSG_FILE,MODE_PRIVATE).getBoolean("is_login",false))
//            {
//                ARouter.getInstance().build(ARouterActivityPath.Login.PAGER_LOGIN).navigation(AppActivity.this,1);
////                    finish();
//            }
//            else {
//                startActivity(new Intent(AppActivity.this, MainPageActivity.class));
//                finish();
//            }
            startActivity(new Intent(AppActivity.this, MainPageActivity.class));
            finish();
            }
        };
        timer.schedule(timer_task,1000);//1秒后
//
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            //下面的1为startActivityForResult(intent, 1);中的1
            case 1:
                //这里的1为setResult(1, intent);中的1
                if (resultCode==1){
                    Intent intent = new Intent(AppActivity.this,MainPageActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;

        }
    }
}