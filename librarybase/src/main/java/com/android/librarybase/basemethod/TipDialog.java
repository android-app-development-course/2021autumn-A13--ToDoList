package com.android.librarybase.basemethod;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Timer;
import java.util.TimerTask;

public class TipDialog {


    public static void showLoadingDialog(Context mContext){
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                tipDialog.dismiss();
            }
        };
        timer.schedule(timer_task,1000);//1秒后
    }


    public static void showFinishDialog(Context mContext,String finish_text){
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(finish_text)
                .create();
        tipDialog.show();
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                tipDialog.dismiss();
            }
        };
        timer.schedule(timer_task,1000);//1秒后
    }
    public static void showUnFinishDialog(Context mContext,String finish_text){
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(finish_text)
                .create();
        tipDialog.show();
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                tipDialog.dismiss();
            }
        };
        timer.schedule(timer_task,1000);//1秒后
    }
}
