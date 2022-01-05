package com.android.librarybase.Screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class ScreenDatas {
    /**
     * 获得屏幕高度
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:15:59
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:16:13
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 高度百分比
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:16:13
     */
    public static float getHeightPercent(Context context,float percent) {
        return getScreenHeight(context)*percent*1.0f;
    }

    /**
     * 宽度百分比
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:16:13
     */
    public static float getWidthPercent(Context context,float percent) {
        return getScreenWidth(context)*percent*1.0f;
    }

    /**
     * 获取屏幕密度
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:16:29
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
    /**
     * dp转px像素
     * @param context
     * @param dp
     * @return
     * by Hankkin at:2015-10-07 21:16:43
     */

    public static int dpToPx(Context context,float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    /**
     * sp转px像素
     * @param context
     * @param sp
     * @return
     * by Hankkin at:2015-10-07 21:16:43
     */
    public static int spToPx(Context context,float sp) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    /**
     * 获得状态栏的高度
     * @param context
     * @return
     * by Hankkin at:2015-10-07 21:16:43
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    /**
     * 获取当前屏幕截图，包含状态栏
     * @param activity
     * @return
     * by Hankkin at:2015-10-07 21:16:43
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     * @param activity
     * @return
     * by Hankkin at:2015-10-07 21:16:43
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }
}
