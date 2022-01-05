package com.android.commonrecyclelist;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.storage.SharedStorage;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

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
        SharedStorage.setNowTagInHomeWhenEmpty(this);



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
