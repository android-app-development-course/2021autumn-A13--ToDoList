package com.android.functimeline;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basemethod.GetTime;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarydb.SQLite.OrderDBHelperCards;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.xuexiang.xui.utils.ResUtils.getResources;

public class TimeLineAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private List<CardBean> listItem;
    private Context mContext;



    private long mTime;
    public void setmTime(long mTime) {
        this.mTime = mTime;
    }
    //构造函数，传入数据
    public TimeLineAdapter(Context context, List<CardBean> listItem) {
        inflater = LayoutInflater.from(context);
        this.mContext=context;
        this.listItem = listItem;
    }


    //定义Viewholder
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle ,mDescribe , mTag ,mCardId;
        private ImageView mTagImg,mDescribeImg,mMore;

        public ViewHolder(View root) {
            super(root);

            mTitle = (TextView) root.findViewById(R.id.timeline_title);
            mDescribe = (TextView) root.findViewById(R.id.timeline_describe);
            mTag = (TextView) root.findViewById(R.id.timeline_tag);
            mTagImg = (ImageView) root.findViewById(R.id.timeline_tag_img);
            mDescribeImg = (ImageView) root.findViewById(R.id.timeline_describe_img);
            mMore = (ImageView)root.findViewById(R.id.more_actions);
            mCardId = root.findViewById(R.id.card_id);

        }

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view.isFocusable();
//                view.requestFocus();
//                view.requestFocusFromTouch();

            }
        });
        return viewHolder;

//        return new Viewholder(inflater.inflate(R.layout.list_cell, null));
//        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell, parent, false));
    }//在这里把ViewHolder绑定Item的布局

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder vh = (ViewHolder) holder;
        // 绑定数据到ViewHolder里面

        vh.mCardId.setText(String.valueOf(listItem.get(position).getCard_id()));
        vh.mTitle.setText(listItem.get(position).getDay_title());
        if(!listItem.get(position).getDescribe().isEmpty()){
            vh.mDescribe.setText(listItem.get(position).getDescribe());
        }
        else {
            vh.mDescribe.setText("写点什么吧");
        }
        vh.mTag.setText(listItem.get(position).getTag_name());

        if(GetTime.isToday(mTime)&&GetTime.isPlanGoing(listItem.get(position).getStart_time(),listItem.get(position).getEnd_time())){
            vh.mTagImg.setImageDrawable(vh.itemView.getContext().getDrawable(R.drawable.card_tag_white));
            vh.mDescribeImg.setImageDrawable(vh.itemView.getContext().getDrawable(R.drawable.card_describe_white));
            vh.mMore.setImageDrawable(vh.itemView.getContext().getDrawable(R.drawable.card_more_white));
            vh.mDescribe.setTextColor(getResources().getColor(R.color.main_edit_grad));
            vh.mTitle.setTextColor(getResources().getColor(R.color.white));
            vh.mTag.setTextColor(getResources().getColor(R.color.main_edit_grad));
        }
        else{
            vh.mTagImg.setImageDrawable(vh.itemView.getContext().getDrawable(R.drawable.card_tag_grad));
            vh.mDescribeImg.setImageDrawable(vh.itemView.getContext().getDrawable(R.drawable.card_describe_grad));
            vh.mMore.setImageDrawable(vh.itemView.getContext().getDrawable(R.drawable.card_more_grad));
            vh.mDescribe.setTextColor(getResources().getColor(R.color.main_grad));
            vh.mTitle.setTextColor(getResources().getColor(R.color.main_black));
            vh.mTag.setTextColor(getResources().getColor(R.color.main_grad));
        }

        if (listItem.get(position).isIs_finish()){
            finishChangeView(holder.itemView);
        }
        else {
            unFinishChangeView(holder.itemView);
        }

        ((ViewHolder) holder).mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBottomSheet(position,vh);
            }
        });
    }

    //返回Item数目
    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public void setData(@Nullable List<CardBean> list) {
        listItem.clear();
        if(list != null){
            listItem.addAll(list);
        }
        notifyDataSetChanged();
    }

    private void setFinish(View view){
        finishChangeView(view);
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
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
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    TextView tv_card_id=view.findViewById(R.id.card_id);
                    int card_id= Integer.parseInt(tv_card_id.getText().toString().trim());
                    SQLiteTools.updateCardDB(mContext,card_id, OrderDBHelperCards.IS_FINISH,false);
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

    private void finishChangeView(View view){
        LinearLayout ll_total = view.findViewById(R.id.ll_total);
        TextView day_title = view.findViewById(R.id.timeline_title);
        day_title.setPaintFlags(Paint. STRIKE_THRU_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
        ll_total.setAlpha(0.4f);
    }

    private void unFinishChangeView(View view){
        LinearLayout ll_total = view.findViewById(R.id.ll_total);
        TextView day_title = view.findViewById(R.id.timeline_title);
        day_title.setPaintFlags(0); // 设置中划线并加清晰
        ll_total.setAlpha(1f);
    }

    private void newBottomSheet(int position,ViewHolder vh){
        String finish_str;
        if(listItem.get(position).isIs_finish())finish_str="取消完成计划";
        else finish_str="完成计划";
        new QMUIBottomSheet.BottomListSheetBuilder(mContext)
                .setGravityCenter(true)
                .setTitle(listItem.get(position).getDay_title())
                .addItem(finish_str)
                .addItem("编辑")
                .addItem("删除")
                .setAddCancelBtn(true)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int pos, String tag) {
                        dialog.dismiss();
//                                Toast.makeText(MainActivity.this, "Item " + (position + 1), Toast.LENGTH_SHORT).show();
                        if(pos == 0){
                            boolean is_finish_=listItem.get(position).isIs_finish();
                            if(is_finish_)unFinishChangeView(vh.itemView);
                            else  finishChangeView(vh.itemView);
//                            listItem.get(position).setIs_finish(!is_finish_);
                            SQLiteTools.updateCardDB(mContext,listItem.get(position).getCard_id(),"is_finish",!is_finish_);
                            EventBus.getDefault().post(new EventBusCardMidMsg(true));
                            notifyDataSetChanged();
                        }
                        else if (pos==1){

                            Timer timer=new Timer();
                            TimerTask timer_task=new TimerTask() {
                                @Override
                                public void run(){
                                    ARouter.getInstance().build(ARouterActivityPath.PlanDetail.PLAN_DETAIL)
                                            .withInt("card_id",listItem.get(position).getCard_id()).navigation();
                                }
                            };
                            timer.schedule(timer_task,500);//1秒后
                        }
                        else if(pos==2){
                            Handler mHandler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        if (msg.what == 1) {
                                            /**
                                             * 显示自定义对话框
                                             */
                                            MaterialDialog dialog_layout = new MaterialDialog.Builder(mContext)
                                                    .content("是否删除")
                                                    .positiveText("确认")
                                                    .negativeText("取消")
                                                    .positiveColor(getResources().getColor(R.color.main_green))
                                                    .negativeColor(getResources().getColor(R.color.xui_config_color_red))
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
                                                            TextView card_id=vh.itemView.findViewById(R.id.card_id);
                                                            int card_id_int=Integer.parseInt(card_id.getText().toString().trim());
                                                            long start_time_long=SQLiteTools.getCard(mContext,card_id_int).getStart_time();
                                                            SQLiteTools.fakeDeleteCardDB(mContext, card_id_int);
                                                            listItem.remove(vh.getAdapterPosition());
                                                            EventBus.getDefault().post(new EventBusCardMidMsg(true));
                                                            notifyDataSetChanged();
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
                                        }
                                    }
                                };
                                Timer timer=new Timer();
                                TimerTask timer_task=new TimerTask() {
                                    @Override
                                    public void run(){
                                        Message updateTimeMessage = Message.obtain();
                                        updateTimeMessage.what = 1;
                                        mHandler.sendMessage(updateTimeMessage);
                                    }
                                };
                                timer.schedule(timer_task,500);//1秒后

                        }
                    }
                })
                .build()
                .show();
    }

}
