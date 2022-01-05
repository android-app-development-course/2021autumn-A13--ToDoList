package com.android.commonrecyclelist.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.commonrecyclelist.R;
import com.android.librarybase.basemethod.Color;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarydb.SQLite.OrderDBHelperCards;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
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

public class CommonAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder>{

    private List<CardBean> mData = new ArrayList<>();
    final private Context mContext;
    final public QMUISwipeAction mDeleteAction;

    public CommonAdapter(Context context){
        mContext = context;
//        ARouter.getInstance().inject(mContext);
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textColor(mContext.getResources().getColor(R.color.white))
                .paddingStartEnd(QMUIDisplayHelper.dp2px(mContext, 16));

        mDeleteAction = builder.icon(mContext.getDrawable(R.drawable.delete)).build();
    }

    public void setData(@Nullable List<CardBean> list) {
        mData.clear();
        if(list != null){
            mData.addAll(list);
        }
        notifyDataSetChanged();
        EventBus.getDefault().post(new EventBusCheckHomeEmptyMidMsg(true,mData.isEmpty()));
    }

    public void remove(int pos){
        mData.remove(pos);
        notifyItemRemoved(pos);
        EventBus.getDefault().post(new EventBusCheckHomeEmptyMidMsg(true,mData.isEmpty()));
    }

    public void add(int pos, CardBean item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
        EventBus.getDefault().post(new EventBusCheckHomeEmptyMidMsg(true,mData.isEmpty()));
    }

    public void prepend(@NonNull List<CardBean> items){
        mData.addAll(0, items);
        notifyDataSetChanged();
        EventBus.getDefault().post(new EventBusCheckHomeEmptyMidMsg(true,mData.isEmpty()));
    }

    public void append(@NonNull List<CardBean> items){
        mData.addAll(items);
        notifyDataSetChanged();
        EventBus.getDefault().post(new EventBusCheckHomeEmptyMidMsg(true,mData.isEmpty()));
    }

    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false);
        final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
        vh.addSwipeAction(mDeleteAction);
        LinearLayout day_msg = view.findViewById(R.id.day_msg);
        LinearLayout checkboxlayout = view.findViewById(R.id.checkboxlayout);
        SmoothCheckBox smoothCheckBox=view.findViewById(R.id.checkbox);

        day_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,
//                        "click position=" + vh.getAdapterPosition(),
//                        Toast.LENGTH_SHORT).show();
                TextView tv_card_id=view.findViewById(R.id.card_id);
                String card_id="-1";
                card_id=tv_card_id.getText().toString().trim();
                ARouter.getInstance().build(ARouterActivityPath.PlanDetail.PLAN_DETAIL)
                        .withInt("card_id",Integer.parseInt(card_id))
                        .navigation();
            }
        });
        smoothCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            synchronized public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked){
                    setFinish(view);
                }
                else{
                    setUnFinish(view);
                }
            }
        });
        checkboxlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            synchronized public void onClick(View v) {
                if(!smoothCheckBox.isChecked()) {
                    smoothCheckBox.setChecked(true, true);
                    setFinish(view);
                }
                else {
                    smoothCheckBox.setChecked(false, true);
                    setUnFinish(view);
                }
            }
        });
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

//        TextView pos = holder.itemView.findViewById(R.id.card_id);
//        pos.setText(String.valueOf(position));

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
//        Log.e("IS_FINISH01", mData.get(position).getCard_id()+String.valueOf(SQLiteTools.getCard(mContext,mData.get(position).getCard_id()).isIs_finish()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void setFinish(View view){
        finishChangeView(view);
        LinearLayout checkboxlayout=view.findViewById(R.id.checkboxlayout);
        SmoothCheckBox checkbox=view.findViewById(R.id.checkbox);
        checkboxlayout.setEnabled(false);
        checkbox.setEnabled(false);
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    LinearLayout checkboxlayout=view.findViewById(R.id.checkboxlayout);
                    SmoothCheckBox checkbox=view.findViewById(R.id.checkbox);
                    checkboxlayout.setEnabled(true);
                    checkbox.setEnabled(true);
                    TextView tv_card_id=view.findViewById(R.id.card_id);
                    int card_id= Integer.parseInt(tv_card_id.getText().toString().trim());
                    SQLiteTools.updateCardDB(mContext,card_id, OrderDBHelperCards.IS_FINISH,true);
                    EventBus.getDefault().post(new EventBusCardMidMsg(true));
                }
            }
        };
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                Message updateTimeMessage = Message.obtain();
                updateTimeMessage.what = 0;
                mHandler.sendMessage(updateTimeMessage);
            }
        };
        timer.schedule(timer_task,700);//1秒后



    }

    private void setUnFinish(View view){
        unFinishChangeView(view);
        LinearLayout checkboxlayout=view.findViewById(R.id.checkboxlayout);
        SmoothCheckBox checkbox=view.findViewById(R.id.checkbox);
        checkboxlayout.setEnabled(false);
        checkbox.setEnabled(false);
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    TextView tv_card_id=view.findViewById(R.id.card_id);
                    int card_id= Integer.parseInt(tv_card_id.getText().toString().trim());
                    SQLiteTools.updateCardDB(mContext,card_id, OrderDBHelperCards.IS_FINISH,false);
                    LinearLayout checkboxlayout=view.findViewById(R.id.checkboxlayout);
                    SmoothCheckBox checkbox=view.findViewById(R.id.checkbox);
                    checkboxlayout.setEnabled(true);
                    checkbox.setEnabled(true);
                    EventBus.getDefault().post(new EventBusCardMidMsg(true));
                }
            }
        };
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run(){
                Message updateTimeMessage = Message.obtain();
                updateTimeMessage.what = 0;
                mHandler.sendMessage(updateTimeMessage);
            }
        };
        timer.schedule(timer_task,500);//1秒后





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
