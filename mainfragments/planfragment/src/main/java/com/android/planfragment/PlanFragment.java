package com.android.planfragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.librarybase.constant.ARouterFragmentPath;
import com.android.librarybase.basepage.BaseFragment;

import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarydb.eventBus.EventBusDialogSortTimeLineMidMsg;
import com.android.librarydb.eventBus.EventBusSelectTimeMidMsg;
import com.android.librarydb.eventBus.EventBusSortTimeLineMidMsg;
import com.android.planfragment.chineseCalendar.CustomInit;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path= ARouterFragmentPath.Plan.FRAG_PLAN)
public class PlanFragment extends BaseFragment implements
        View.OnClickListener {

    private FrameLayout fl_sort ,fl_set;
    private TextView now_start,now_reverse,now_finish;
    private CustomInit customInit;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_plan;
    }

    @Override
    protected void initView() {
//        EventBus.getDefault().register(this);
        fl_sort = mRootView.findViewById(R.id.fl_sort);
        fl_set = mRootView.findViewById(R.id.fl_set);
        now_start = mRootView.findViewById(R.id.now_is_start);
        now_reverse = mRootView.findViewById(R.id.now_is_reverse);
        now_finish = mRootView.findViewById(R.id.now_finish);
        customInit=new CustomInit(mRootView,mContext);
        customInit.customInitView();
        now_start.setText(SharedStorage.isStartTimeLine(mContext)?"开始":"结束");
        now_reverse.setText(SharedStorage.isReverseTimeLine(mContext)?"倒序":"正序");
        String temp_finish="全部";
        if (SharedStorage.isFinishTimeline(mContext)==1)temp_finish="已完成";
        else if(SharedStorage.isFinishTimeline(mContext)==0)temp_finish="未完成";
        now_finish.setText(temp_finish);
        fl_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusSortTimeLineMidMsg(true));
                now_reverse.setText(SharedStorage.isReverseTimeLine(mContext)?"倒序":"正序");
            }
        });

        fl_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 显示自定义对话框
                 */
                MaterialDialog dialog_layout = new MaterialDialog.Builder(mContext)
                        .customView(R.layout.timeline_set_dialog, true)
                        .title("排序设置")
                        .positiveText("确认")
                        .negativeText("取消")
                        .positiveColor(getResources().getColor(R.color.main_green))
                        .negativeColor(getResources().getColor(R.color.xui_config_color_red))
                        .show();
                /**
                 * 开始结束时间按钮
                 */
                SmoothCheckBox start_box=dialog_layout.findViewById(R.id.normal_start);
                SmoothCheckBox end_box=dialog_layout.findViewById(R.id.normal_end);
                SmoothCheckBox pre_box=dialog_layout.findViewById(R.id.all_day_pre);
                SmoothCheckBox last_box=dialog_layout.findViewById(R.id.all_day_last);
                SmoothCheckBox is_finish_plan_box=dialog_layout.findViewById(R.id.is_finish_plan);
                SmoothCheckBox un_finish_plan_box=dialog_layout.findViewById(R.id.un_finish_plan);
                SmoothCheckBox all_plan_box=dialog_layout.findViewById(R.id.all_plan);
                if(SharedStorage.isStartTimeLine(mContext))start_box.setCheckedSilent(true);
                else end_box.setCheckedSilent(true);
                Log.e("start", String.valueOf(SharedStorage.isStartTimeLine(mContext)));
                if(SharedStorage.isPreTimeLine(mContext))pre_box.setCheckedSilent(true);
                else last_box.setCheckedSilent(true);
                if(SharedStorage.isFinishTimeline(mContext)==1)is_finish_plan_box.setCheckedSilent(true);
                else if(SharedStorage.isFinishTimeline(mContext)==0) un_finish_plan_box.setCheckedSilent(true);
                else if(SharedStorage.isFinishTimeline(mContext)==-1) all_plan_box.setCheckedSilent(true);
                Log.e("start", String.valueOf(SharedStorage.isStartTimeLine(mContext)));
                start_box.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        end_box.setCheckedSilent(false);
                        if(!isChecked)start_box.setCheckedSilent(true);
                    }
                });
                end_box.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        start_box.setCheckedSilent(false);
                        if(!isChecked)end_box.setCheckedSilent(true);
                    }
                });
                pre_box.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        last_box.setCheckedSilent(false);
                        if(!isChecked)pre_box.setCheckedSilent(true);
                    }
                });
                last_box.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        pre_box.setCheckedSilent(false);
                        if(!isChecked)last_box.setCheckedSilent(true);
                    }
                });
                is_finish_plan_box.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        un_finish_plan_box.setCheckedSilent(false);
                        all_plan_box.setCheckedSilent(false);
                        if(!isChecked)is_finish_plan_box.setCheckedSilent(true);
                    }
                });
                un_finish_plan_box.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        is_finish_plan_box.setCheckedSilent(false);
                        all_plan_box.setCheckedSilent(false);
                        if(!isChecked)un_finish_plan_box.setCheckedSilent(true);
                    }
                });
                all_plan_box.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        is_finish_plan_box.setCheckedSilent(false);
                        un_finish_plan_box.setCheckedSilent(false);
                        if(!isChecked)all_plan_box.setCheckedSilent(true);
                    }
                });
                /**
                 * 自定义确认按钮
                 */
                dialog_layout.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        dialog_layout.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedStorage.changeStartTimeLine(mContext,start_box.isChecked());
//                                Log.e("start", String.valueOf(start_box.isChecked()));
                                SharedStorage.changePreTimeLine(mContext,pre_box.isChecked());
//                                Log.e("start", String.valueOf(pre_box.isChecked()));
                                int change_finish=-1;
                                if(is_finish_plan_box.isChecked())change_finish=1;
                                else if(un_finish_plan_box.isChecked())change_finish=0;
                                SharedStorage.changeFinishTimeline(mContext,change_finish);
                                EventBus.getDefault().post(new EventBusDialogSortTimeLineMidMsg(true));
                                dialog_layout.dismiss();
                                now_start.setText(SharedStorage.isStartTimeLine(mContext)?"开始":"结束");
                                if (change_finish==-1)now_finish.setText("全部");
                                else if (change_finish==0)now_finish.setText("未完成");
                                else if (change_finish==1)now_finish.setText("已完成");
                            }
                        });
                    }
                });
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        customInit.customInitData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //添加计划按钮
    }

    @Override
    public void onClick(View v) {

    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void EventBusReceive(EventBusCardMidMsg eventBusCardMidMsg){
//        if(eventBusCardMidMsg.isIs_success()){
//            customInit.setCalendarShi();
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(EventBusSelectTimeMidMsg event){
//        if (event.isIs_success()) {
//            customInit.setCalendarShi();
//        }
//    }


}
