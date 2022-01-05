package com.android.functimeline;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.librarybase.basemethod.GetTime;
import com.android.librarybase.basepage.BaseFragment;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarydb.eventBus.EventBusDialogSortTimeLineMidMsg;
import com.android.librarydb.eventBus.EventBusSelectTimeMidMsg;
import com.android.librarydb.eventBus.EventBusSortTimeLineMidMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@Route(path="/timeline/TimeLineFuncFragment")
public class TimeLineFuncFragment extends BaseFragment {
    private RecyclerView Rv;
    public List<CardBean> listItem;
    public TimeLineAdapter timeLineAdapter;
    private long mTime;
    private DividerItemDecoration dividerItemDecoration;
    private LinearLayout empty_text;


    @Subscribe
    @Override
    protected int getLayoutId() {
        EventBus.getDefault().register(this);//注册订阅者
        return R.layout.fragment_time_line_func;
    }

    @Override
    protected void initView() {
        Rv = mRootView.findViewById(R.id.my_recycler_view);
        empty_text = mRootView.findViewById(R.id.empty_text);
    }

    @Override
    protected void initData() {
        mTime=GetTime.getNowTimeLong();
        listItem = SQLiteTools.getSelectTimeCards(mContext, mTime,SharedStorage.isReverseTimeLine(mContext), SharedStorage.isStartTimeLine(mContext),SharedStorage.isPreTimeLine(mContext),SharedStorage.isFinishTimeline(mContext));
        setGoneVisibility();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        Rv.setLayoutManager(layoutManager);
        Rv.setHasFixedSize(true);

        //用自定义分割线类设置分割线
        dividerItemDecoration=new DividerItemDecoration(getActivity(),listItem,mTime);
        Rv.addItemDecoration(dividerItemDecoration);


        //为ListView绑定适配器
        timeLineAdapter = new TimeLineAdapter(getActivity(),listItem);
        Rv.setAdapter(timeLineAdapter);
        timeLineAdapter.setmTime(mTime);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusCardMidMsg eventBusCardMidMsg){
        if(eventBusCardMidMsg.isIs_success()){
            listItem=SQLiteTools.getSelectTimeCards(mContext,mTime,
                    SharedStorage.isReverseTimeLine(mContext),
                    SharedStorage.isStartTimeLine(mContext),
                    SharedStorage.isPreTimeLine(mContext),
                    SharedStorage.isFinishTimeline(mContext));
            dividerItemDecoration.setmTime(mTime);
            dividerItemDecoration.setmList(listItem);
            timeLineAdapter.setData(listItem);
            timeLineAdapter.setmTime(mTime);
            setGoneVisibility();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusSelectTimeMidMsg event){
        if (event.isIs_success()) {
            mTime = event.getmTime();
            listItem=SQLiteTools.getSelectTimeCards(mContext,mTime,
                    SharedStorage.isReverseTimeLine(mContext),
                    SharedStorage.isStartTimeLine(mContext),
                    SharedStorage.isPreTimeLine(mContext),
                    SharedStorage.isFinishTimeline(mContext));
            dividerItemDecoration.setmTime(mTime);
            dividerItemDecoration.setmList(listItem);
            timeLineAdapter.setData(listItem);
            timeLineAdapter.setmTime(mTime);
            setGoneVisibility();
        }
    }

    /**
     * 排序广播
     * @param eventBusSortTimeLineMidMsg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSort(EventBusSortTimeLineMidMsg eventBusSortTimeLineMidMsg){
        Log.e("sort","sorty");
        if(!eventBusSortTimeLineMidMsg.isIs_success())return;
        listItem = SQLiteTools.getSelectTimeCards(mContext,
                mTime,
                SharedStorage.changeAndIsReverseTimeLine(mContext),
                SharedStorage.isStartTimeLine(mContext),
                SharedStorage.isPreTimeLine(mContext),
                SharedStorage.isFinishTimeline(mContext));
        dividerItemDecoration.setmTime(mTime);
        dividerItemDecoration.setmList(listItem);
        timeLineAdapter.setData(listItem);
        timeLineAdapter.setmTime(mTime);
        setGoneVisibility();
    }
    /**
     * 对话框排序广播
     * @param eventBusDialogSortTimeLineMidMsg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDialogSort(EventBusDialogSortTimeLineMidMsg eventBusDialogSortTimeLineMidMsg){
        Log.e("sort","sorty");
        if(!eventBusDialogSortTimeLineMidMsg.isIs_success())return;
        listItem = SQLiteTools.getSelectTimeCards(mContext,
                mTime,
                SharedStorage.isReverseTimeLine(mContext),
                SharedStorage.isStartTimeLine(mContext),
                SharedStorage.isPreTimeLine(mContext),
                SharedStorage.isFinishTimeline(mContext));
        dividerItemDecoration.setmTime(mTime);
        dividerItemDecoration.setmList(listItem);
        timeLineAdapter.setData(listItem);
        timeLineAdapter.setmTime(mTime);
        setGoneVisibility();
    }
    private void setGoneVisibility(){
        if(listItem.size()!=0){
            Rv.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.GONE);
        }
        else{
            Rv.setVisibility(View.GONE);
            empty_text.setVisibility(View.VISIBLE);
        }
    }
}
