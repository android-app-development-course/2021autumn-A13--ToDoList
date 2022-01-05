package com.android.librarybase.topbarcolor;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.librarybase.R;

import static com.xuexiang.xui.utils.ResUtils.getResources;

public class TopBar {
    public static void setTopBarColor(Activity activity){
        int mode=getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //设置状态栏为白色字体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(mode==Configuration.UI_MODE_NIGHT_NO){
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            //先将状态栏调整为透明
                window.setStatusBarColor(activity.getResources().getColor(R.color.white));
            }
            else{
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            //先将状态栏调整为透明
                window.setStatusBarColor(activity.getResources().getColor(R.color.main_black));
            }

            Log.e("window", String.valueOf(getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK));
            Log.e("windowy", String.valueOf(Configuration.UI_MODE_NIGHT_YES));
            Log.e("windown", String.valueOf(Configuration.UI_MODE_NIGHT_NO));
        }
    }
}