package com.android.commonrecyclelist;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.commonrecyclelist.Adapter.CommonAdapter;
import com.android.librarybase.basepage.BaseFragment;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarybase.constant.ARouterFragmentPath;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.eventBus.EventBusSelectTagMidMsg;
import com.android.librarydb.eventBus.EventBusSelectTimeMidMsg;
import com.android.librarydb.eventBus.EventBusSortVerticalCommonMidMsg;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


@Route(path = ARouterFragmentPath.MonthList.FUNC_COMMON_LIST)
public class CommonVerticalFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private CommonAdapter mAdapter;
    private String mNowSelectTag;
    @Subscribe
    @Override
    protected int getLayoutId() {
        EventBus.getDefault().register(this);//注册订阅者
        return R.layout.fragment_common_vertical_list;
    }

    @Override
    protected void initView() {
        mRecyclerView=mRootView.findViewById(R.id.common_vertical_list);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void initData() {
        mNowSelectTag="";
        /**
         * 删除一条
         */
        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                /**
                 * 显示自定义对话框
                 */
                MaterialDialog dialog_layout = new MaterialDialog.Builder(mContext)
                        .content("是否删除？删除后可在回收站撤销")
                        .positiveText("确认")
                        .negativeText("取消")
                        .positiveColor(getResources().getColor(R.color.xui_config_color_red))
                        .negativeColor(getResources().getColor(R.color.main_green))
                        .show();
                /**
                 * 自定义确认按钮
                 */
                dialog_layout.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        dialog_layout.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView card_id=viewHolder.itemView.findViewById(R.id.card_id);
                                int card_id_int=Integer.parseInt(card_id.getText().toString().trim());
                                long start_time_long=SQLiteTools.getCard(mContext,card_id_int).getStart_time();
                                SQLiteTools.fakeDeleteCardDB(mContext, card_id_int);
                                mAdapter.remove(viewHolder.getAdapterPosition());
                                EventBus.getDefault().post(new EventBusSelectTimeMidMsg(true, start_time_long));
                                dialog_layout.dismiss();
                            }
                        });
                        dialog_layout.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAdapter.notifyDataSetChanged();
                                dialog_layout.dismiss();
                            }
                        });
                    }
                });

            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                /**
                 * 删除
                 */
                TextView card_id=selected.itemView.findViewById(R.id.card_id);
                if(action == mAdapter.mDeleteAction){
                    /**
                     * 显示自定义对话框
                     */
                    MaterialDialog dialog_layout = new MaterialDialog.Builder(mContext)
                            .content("是否删除？删除后可在回收站撤销")
                            .positiveText("确认")
                            .negativeText("取消")
                            .positiveColor(getResources().getColor(R.color.xui_config_color_red))
                            .negativeColor(getResources().getColor(R.color.main_green))
                            .show();
                    /**
                     * 自定义确认按钮
                     */
                    dialog_layout.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            dialog_layout.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView card_id=selected.itemView.findViewById(R.id.card_id);
                                    int card_id_int=Integer.parseInt(card_id.getText().toString().trim());
                                    long start_time_long=SQLiteTools.getCard(mContext,card_id_int).getStart_time();
                                    SQLiteTools.fakeDeleteCardDB(mContext, card_id_int);
                                    mAdapter.remove(selected.getAdapterPosition());
                                    EventBus.getDefault().post(new EventBusSelectTimeMidMsg(true, start_time_long));
                                    dialog_layout.dismiss();
                                }
                            });
                            dialog_layout.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog_layout.dismiss();
                                }
                            });
                        }
                    });
                }else{
                    swipeAction.clear();
                }
            }
        });
        swipeAction.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        mAdapter = new CommonAdapter(mRootView.getContext());
        mRecyclerView.setAdapter(mAdapter);
        onDataLoaded();
    }
    private void onDataLoaded() {
        List<CardBean>data = SQLiteTools.getSelectTagCards(mContext,mNowSelectTag,SharedStorage.isReverseVerticalCommon(mContext), SharedStorage.isStartVerticalCommon(mContext),SharedStorage.isFinishVerticalCommon(mContext));
        mAdapter.setData(data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusCardMidMsg eventBusCardMidMsg){
        if(eventBusCardMidMsg.isIs_success()){
            mAdapter.setData(SQLiteTools.getSelectTagCards(mContext,mNowSelectTag,SharedStorage.isReverseVerticalCommon(mContext), SharedStorage.isStartVerticalCommon(mContext),SharedStorage.isFinishVerticalCommon(mContext)));
            mAdapter.notifyDataSetChanged();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusTagReceive(EventBusSelectTagMidMsg eventBusTagMidMsg){
        if(eventBusTagMidMsg.isIs_success()){
            mNowSelectTag=eventBusTagMidMsg.getTag_name();
            mAdapter.setData(SQLiteTools.getSelectTagCards(mContext,mNowSelectTag,SharedStorage.isReverseVerticalCommon(mContext), SharedStorage.isStartVerticalCommon(mContext),SharedStorage.isFinishVerticalCommon(mContext)));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusSortReceive(EventBusSortVerticalCommonMidMsg eventBusSortVerticalCommonMidMsg){
        if(eventBusSortVerticalCommonMidMsg.isIs_success()){
            mAdapter.setData(SQLiteTools.getSelectTagCards(mContext,mNowSelectTag,SharedStorage.changeAndIsReverseVerticalCommon(mContext), SharedStorage.isStartVerticalCommon(mContext),SharedStorage.isFinishVerticalCommon(mContext)));
            mAdapter.notifyDataSetChanged();
        }
    }





}
