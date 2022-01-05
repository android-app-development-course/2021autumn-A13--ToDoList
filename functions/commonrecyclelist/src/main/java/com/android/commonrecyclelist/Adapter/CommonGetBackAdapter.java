package com.android.commonrecyclelist.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.commonrecyclelist.R;
import com.android.librarybase.basemethod.Color;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.eventBus.EventBusCheckGetBackEmptyMidMsg;
import com.android.librarydb.eventBus.EventBusCheckHomeEmptyMidMsg;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xuexiang.xui.widget.button.SmoothCheckBox;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CommonGetBackAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder>{

    private List<CardBean> mData = new ArrayList<>();
    final private Context mContext;
    final public QMUISwipeAction mDeleteAction,mGetBackAction;

    public CommonGetBackAdapter(Context context){
        mContext = context;
//        ARouter.getInstance().inject(mContext);
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textColor(mContext.getResources().getColor(R.color.white))
                .paddingStartEnd(QMUIDisplayHelper.dp2px(mContext, 16));

        mDeleteAction = builder.icon(mContext.getDrawable(R.drawable.delete)).build();
        mGetBackAction = builder.icon(mContext.getDrawable(R.drawable.getback)).build();
    }

    public void setData(@Nullable List<CardBean> list) {
        mData.clear();
        if(list != null){
            mData.addAll(list);
        }
        notifyDataSetChanged();
//        Log.v("eventBusCheckGetBackEmptyMidMsg", String.valueOf(mData.isEmpty()));
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                EventBus.getDefault().post(new EventBusCheckGetBackEmptyMidMsg(true,mData.isEmpty()));
            }
        };
        timer.schedule(timer_task,700);//1秒后
    }

    public void remove(int pos){
        mData.remove(pos);
        notifyItemRemoved(pos);
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                EventBus.getDefault().post(new EventBusCheckGetBackEmptyMidMsg(true,mData.isEmpty()));
            }
        };
        timer.schedule(timer_task,700);//1秒后
    }

    public void add(int pos, CardBean item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                EventBus.getDefault().post(new EventBusCheckGetBackEmptyMidMsg(true,mData.isEmpty()));
            }
        };
        timer.schedule(timer_task,700);//1秒后
    }

    public void prepend(@NonNull List<CardBean> items){
        mData.addAll(0, items);
        notifyDataSetChanged();
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                EventBus.getDefault().post(new EventBusCheckGetBackEmptyMidMsg(true,mData.isEmpty()));
            }
        };
        timer.schedule(timer_task,700);//1秒后
    }

    public void append(@NonNull List<CardBean> items){
        mData.addAll(items);
        notifyDataSetChanged();
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                EventBus.getDefault().post(new EventBusCheckGetBackEmptyMidMsg(true,mData.isEmpty()));
            }
        };
        timer.schedule(timer_task,700);//1秒后
    }

    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false);
        final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
        vh.addSwipeAction(mGetBackAction);
        vh.addSwipeAction(mDeleteAction);
        view.setEnabled(false);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QMUISwipeViewHolder holder, int position) {

        TextView day_title = holder.itemView.findViewById(R.id.day_title);
        day_title.setText(mData.get(position).getDay_title());

        TextView month_day_time = holder.itemView.findViewById(R.id.month_day_time);
        month_day_time.setText(mData.get(position).getMonth_day_time());

        TextView card_id = holder.itemView.findViewById(R.id.card_id);
        card_id.setText(String.valueOf(mData.get(position).getCard_id()));

        TextView tag_name = holder.itemView.findViewById(R.id.tag_name);
        tag_name.setText(String.valueOf(mData.get(position).getTag_name()));

        CardView tag = holder.itemView.findViewById(R.id.tag);
        tag.setCardBackgroundColor(mContext.getResources().getColor(Color.getTagLightColor(mData.get(position).getTag_color())));


        SmoothCheckBox checkBox_ = holder.itemView.findViewById(R.id.checkbox);
        checkBox_.setCheckedSilent(mData.get(position).isIs_finish());
        if (mData.get(position).isIs_finish()){
            finishChangeView(holder.itemView);
        }
        else {
            unFinishChangeView(holder.itemView);
        }
        checkBox_.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void finishChangeView(View view){
        LinearLayout day_msg = view.findViewById(R.id.day_msg);
        LinearLayout checkboxlayout = view.findViewById(R.id.checkboxlayout);
        TextView day_title = view.findViewById(R.id.day_title);
        day_title.setPaintFlags(Paint. STRIKE_THRU_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
        day_msg.setAlpha(0.4f);
        checkboxlayout.setAlpha(0.4f);
    }

    private void unFinishChangeView(View view){
        LinearLayout day_msg = view.findViewById(R.id.day_msg);
        LinearLayout checkboxlayout = view.findViewById(R.id.checkboxlayout);
        TextView day_title = view.findViewById(R.id.day_title);
        day_title.setPaintFlags(0); // 取消中划线
        day_msg.setAlpha(1f);
        checkboxlayout.setAlpha(1f);
    }
}
