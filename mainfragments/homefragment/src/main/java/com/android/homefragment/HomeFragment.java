package com.android.homefragment;

import android.content.DialogInterface;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.alibaba.android.arouter.facade.annotation.Route;

import com.android.librarybase.basepage.BaseFragment;
import com.android.librarybase.constant.ARouterFragmentPath;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean__.TagBean;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarydb.eventBus.EventBusCheckHomeEmptyMidMsg;
import com.android.librarydb.eventBus.EventBusSelectTagMidMsg;
import com.android.librarydb.eventBus.EventBusSortVerticalCommonMidMsg;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullRefreshView;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path= ARouterFragmentPath.Home.FRAG_HOME)
public class HomeFragment extends BaseFragment {

    private LinearLayout more_labels;
    private TextView more_labels_text;
    private XUISimplePopup mMenuPopup;
    private QMUIPullRefreshLayout qmuipulllayout;

    private FrameLayout fl_sort ,fl_set;
    private TextView now_start,now_reverse,now_finish;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Subscribe
    @Override
    protected void initView() {
        EventBus.getDefault().register(this);//注册订阅者'
        qmuipulllayout = mRootView.findViewById(R.id.qmuipulllayout);
        more_labels=mRootView.findViewById(R.id.more_labels);
        more_labels_text=mRootView.findViewById(R.id.more_labels_text);
        fl_sort=mRootView.findViewById(R.id.fl_sort);
        fl_set=mRootView.findViewById(R.id.fl_set);
        now_start = mRootView.findViewById(R.id.now_is_start);
        now_reverse = mRootView.findViewById(R.id.now_is_reverse);
        now_finish = mRootView.findViewById(R.id.now_finish);
        now_start.setText(SharedStorage.isStartVerticalCommon(mContext)?"开始":"结束");
        now_reverse.setText(SharedStorage.isReverseVerticalCommon(mContext)?"倒序":"正序");
        String temp_finish="全部";
        if (SharedStorage.isFinishVerticalCommon(mContext)==1)temp_finish="已完成";
        else if(SharedStorage.isFinishVerticalCommon(mContext)==0)temp_finish="未完成";
        now_finish.setText(temp_finish);

        fl_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusSortVerticalCommonMidMsg(true));
                now_reverse.setText(SharedStorage.isReverseVerticalCommon(mContext)?"倒序":"正序");
            }
        });

        fl_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 显示自定义对话框
                 */
                MaterialDialog dialog_layout = new MaterialDialog.Builder(mContext)
                        .customView(R.layout.common_vertical_set_dialog, true)
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
                SmoothCheckBox is_finish_plan_box=dialog_layout.findViewById(R.id.is_finish_plan);
                SmoothCheckBox un_finish_plan_box=dialog_layout.findViewById(R.id.un_finish_plan);
                SmoothCheckBox all_plan_box=dialog_layout.findViewById(R.id.all_plan);
                if(SharedStorage.isStartVerticalCommon(mContext))start_box.setCheckedSilent(true);
                else end_box.setCheckedSilent(true);
                if(SharedStorage.isFinishVerticalCommon(mContext)==1)is_finish_plan_box.setCheckedSilent(true);
                else if(SharedStorage.isFinishVerticalCommon(mContext)==0) un_finish_plan_box.setCheckedSilent(true);
                else if(SharedStorage.isFinishVerticalCommon(mContext)==-1) all_plan_box.setCheckedSilent(true);
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
                                SharedStorage.changeStartVerticalCommon(mContext,start_box.isChecked());
                                int change_finish=-1;
                                if(is_finish_plan_box.isChecked())change_finish=1;
                                else if(un_finish_plan_box.isChecked())change_finish=0;
                                SharedStorage.changeFinishVerticalCommon(mContext,change_finish);
                                if (change_finish==-1)now_finish.setText("全部");
                                else if (change_finish==0)now_finish.setText("未完成");
                                else if (change_finish==1)now_finish.setText("已完成");
                                EventBus.getDefault().post(new EventBusCardMidMsg(true));
                                dialog_layout.dismiss();
                                now_start.setText(SharedStorage.isStartVerticalCommon(mContext)?"开始":"结束");
                            }
                        });
                    }
                });
            }
        });

        List<AdapterItem>menuItems=new ArrayList<>();
        menuItems.add(new AdapterItem(("所有")));
        List<TagBean>lists = SQLiteTools.getAllTags(mContext);
        for (int i=0;i<lists.size();++i){
            menuItems.add(new AdapterItem(lists.get(i).getTag_name()));
        }
        //==========================设置上次点击的标签===========================

        more_labels_text.setText(SharedStorage.getNowTagInHome(mContext));
        EventBus.getDefault().post(new EventBusSelectTagMidMsg(true,SharedStorage.getNowTagInHome(mContext)));
        mMenuPopup = new XUISimplePopup(getContext(), menuItems)
                .create((adapter, item, position) -> {
                }).setOnPopupItemClickListener(new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        more_labels_text.setText(item.getTitle().toString());
                        SharedStorage.setNowTagInHome(mContext,item.getTitle().toString());
                        EventBus.getDefault().post(new EventBusSelectTagMidMsg(true,item.getTitle().toString().trim()));
                    }
                });
        more_labels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPopup.showDown(v);
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
                        EventBus.getDefault().post(new EventBusCardMidMsg(true));
                        qmuipulllayout.finishRefresh();
                    }
                }, 1000);
            }
        });
    }


    protected void initData() {
        EventBus.getDefault().post(new EventBusSelectTagMidMsg(true,more_labels_text.getText().toString().trim()));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusCheckHomeEmptyMidMsg eventBusCheckHomeEmptyMidMsg){
        if(eventBusCheckHomeEmptyMidMsg.isIs_success()){
            LinearLayout empty_text=mRootView.findViewById(R.id.empty_text);
            if(!eventBusCheckHomeEmptyMidMsg.isIs_empty()){
                empty_text.setVisibility(View.GONE);
            }
            else{
                empty_text.setVisibility(View.VISIBLE);
            }
        }
    }
}
