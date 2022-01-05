package com.android.todolist;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarydb.SQLite.SQLiteTools;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import cn.bmob.v3.Bmob;

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        boolean isDebug=true;
        if (isDebug) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this);

        //初始化QMUISwipeBackActivityManager，否则点击屏幕时就程序就会崩溃
        QMUISwipeBackActivityManager.init(this);

        SQLiteTools.insertTagDB(this.getBaseContext(),"其他",R.color.main_purple);
        //连接数据库
        Bmob.initialize(this,"37ce3db38d0d1f127ff5139527e82c0d");

        MultiDex.install(this);
//===test
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                accountRequest accountRequest = new accountRequest();
//                boolean login=false;
//                try {
//                    login = accountRequest.login("1232", "123");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if(login){
//                    Intent intent=new Intent(APP.this,MainPageActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }).start();
    }
}
