package com.android.loginpage;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

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
    }
}
