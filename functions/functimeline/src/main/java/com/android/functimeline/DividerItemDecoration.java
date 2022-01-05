package com.android.functimeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.librarybase.basemethod.GetTime;
import com.android.librarydb.bean__.CardBean;

import java.text.ParseException;
import java.util.List;

import static com.android.librarybase.Screen.ScreenDatas.dpToPx;
import static com.android.librarybase.Screen.ScreenDatas.getHeightPercent;
import static com.android.librarybase.Screen.ScreenDatas.getWidthPercent;
import static com.android.librarybase.Screen.ScreenDatas.spToPx;


public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;

    private long mTime;

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }
    // 写右边字的画笔(具体信息)
    private Paint mPaint;

    // 写左边时间字的画笔( 开始时间 + 结束时间)
    private Paint mPaint1;
    private Paint mPaint2;

    // 左 上偏移长度
    private int itemView_leftinterval;
    private int itemView_topinterval;
    private int itemView_bottominterval;
    private int itemView_rightinterval;

    // 轴点半径
    private int circle_radius;

    // 图标
    private Bitmap mIcon;

    //listItem
    private List<CardBean>mList;
    public void setmList(List<CardBean> mList) {
        this.mList = mList;
    }

    // 在构造函数里进行绘制的初始化，如画笔属性设置等
    public DividerItemDecoration(Context context, List<CardBean>mList,long mTime) {
        this.mList=mList;
        this.mTime=mTime;
        this.context=context;
        // 轴点画笔(红色)
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(R.color.main_timeline));
        mPaint.setStrokeWidth(3.0F);
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setTextSize(2);

        // 左边时间文本画笔(蓝色)
        // 此处设置了两只分别设置 时分 & 年月
        mPaint1 = new Paint();
        mPaint1.setColor(context.getResources().getColor(R.color.main_black));
        mPaint1.setTextSize(spToPx(context,15));

        mPaint2 = new Paint();
        mPaint2.setColor(context.getResources().getColor(R.color.main_grad));
        mPaint2.setTextSize(spToPx(context,11));


        // 赋值ItemView的左偏移长度为200
        itemView_leftinterval = (int)getWidthPercent(context,0.21f);
        // 赋值ItemView的上偏移长度为50
        itemView_topinterval = dpToPx(context,10);
        itemView_rightinterval = 0;
        itemView_bottominterval = dpToPx(context,19);

        // 赋值轴点圆的半径为10
        circle_radius = dpToPx(context,8);

        // 获取图标资源
        // mIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);

    }

    // 重写getItemOffsets（）方法
    // 作用：设置ItemView 左 & 上偏移长度
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // 设置ItemView的左 & 上偏移长度分别为200 px & 50px,即此为onDraw()可绘制的区域
        outRect.set(itemView_leftinterval, itemView_topinterval, itemView_rightinterval, itemView_bottominterval);

    }

    // 重写onDraw（）
    // 作用:在间隔区域里绘制时光轴线 & 时间文本
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // 获取RecyclerView的Child view的个数
        int childCount = parent.getChildCount();

        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (int i = 0; i < childCount; i++) {

            // 获取每个Item对象
            CardView child = (CardView) parent.getChildAt(i);

            /**
             * 绘制轴点
             */
            // 轴点 = 圆 = 圆心(x,y)
            float centerx = child.getLeft() - itemView_leftinterval / 4;
            float centery = child.getTop() + circle_radius ;
            // 绘制轴点圆
            c.drawCircle(centerx, centery, circle_radius, mPaint);


            //如果当前时间在该事件的区间内，则在其中画实心圆，并更改该事件的背景颜色。
//            if (i == 0) {
//                mPaint.setStyle(Paint.Style.FILL);
//                c.drawCircle(centerx, centery, circle_radius / 3 * 2, mPaint);
//                mPaint.setStyle(Paint.Style.STROKE);
//                child.setBackground(context.getResources().getDrawable(R.drawable.view_focused));
//                TextView tv_title=child.findViewById(R.id.timeline_title);
//                TextView tv_describe=child.findViewById(R.id.timeline_describe);
//                TextView tv_tag=child.findViewById(R.id.timeline_tag);
//                ImageView img_tag=child.findViewById(R.id.timeline_tag_img);
//                ImageView img_describe=child.findViewById(R.id.timeline_describe_img);
//                ImageView img_more=child.findViewById(R.id.more_actions);
//
//
//                tv_title.setTextColor(context.getResources().getColor(R.color.white));
//                tv_describe.setTextColor(context.getResources().getColor(R.color.white));
//                tv_tag.setTextColor(context.getResources().getColor(R.color.white));
//                img_tag.setImageDrawable(context.getResources().getDrawable(R.drawable.card_tag_white));
//                img_describe.setImageDrawable(context.getResources().getDrawable(R.drawable.card_describe_white));
//                img_more.setImageDrawable(context.getResources().getDrawable(R.drawable.card_more_white));
//
//            }



            /**
             * 绘制下半轴线
             */
            // 上端点坐标(x,y)
            float bottomLine_up_x = centerx;
            float bottom_up_y = centery + circle_radius + dpToPx(context,4);

            // 下端点坐标(x,y)
            float bottomLine_bottom_x = centerx;
            float bottomLine_bottom_y = child.getBottom() + itemView_bottominterval +dpToPx(context,10) - dpToPx(context,4);


            //绘制下半部轴线
            if (i != childCount - 1)
                c.drawLine(bottomLine_up_x, bottom_up_y, bottomLine_bottom_x, bottomLine_bottom_y, mPaint);


            /**
             * 绘制左边时间文本
             */
            // 获取每个Item的位置
            int index = parent.getChildAdapterPosition(child);
            // 设置文本起始坐标
            float Text_x = child.getLeft() - itemView_leftinterval ;
//                    + dpToPx(context,10);
            float Text_y = child.getTop() + itemView_topinterval + dpToPx(context,2);


            //==============符合当前时间段的====================
            //====是今天的才是绿色=======
//            Log.v("mTime",GetTime.getNowTimeStr(mTime,"yyyy/MM/dd HH:mm"));
//            Log.v("today",GetTime.getNowTimeStr(GetTime.getNowTimeLong(),"yyyy/MM/dd HH:mm"));
            if(GetTime.isToday(mTime)&&GetTime.isPlanGoing(mList.get(index).getStart_time(),mList.get(index).getEnd_time())){
                mPaint.setStyle(Paint.Style.FILL);
                c.drawCircle(centerx, centery, circle_radius / 3 * 2, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);
                child.setCardBackgroundColor(context.getResources().getColor(R.color.main_timeline_card));
            }
            else{
                child.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            }

            // 根据Item位置设置时间文本
            int baohanguanxi=-1;
            try {
                baohanguanxi=GetTime.checkTime(mList.get(index).getStart_time(),mList.get(index).getEnd_time(),mTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(baohanguanxi==1||mList.get(index).isIs_all_day()){
                c.drawText("全天", Text_x, Text_y, mPaint1);
            }
            else if(baohanguanxi==0){
                String start_time= GetTime.getNowTimeStr(mList.get(index).getStart_time(),"HH:mm");
                String end_time= "23:59";
                c.drawText(start_time, Text_x, Text_y, mPaint1);
                c.drawText(end_time, Text_x, Text_y + getHeightPercent(context,0.03f), mPaint2);
            }
            else if(baohanguanxi==2){
                String start_time= "00:00";
                String end_time= GetTime.getNowTimeStr(mList.get(index).getEnd_time(),"HH:mm");
                c.drawText(start_time, Text_x, Text_y, mPaint1);
                c.drawText(end_time, Text_x, Text_y + getHeightPercent(context,0.03f), mPaint2);
            }
            else if (baohanguanxi==3){
                String start_time= GetTime.getNowTimeStr(mList.get(index).getStart_time(),"HH:mm");
                String end_time= GetTime.getNowTimeStr(mList.get(index).getEnd_time(),"HH:mm");
                c.drawText(start_time, Text_x, Text_y, mPaint1);
                c.drawText(end_time, Text_x, Text_y + getHeightPercent(context,0.03f), mPaint2);
            }
//            String start_time= GetTime.getNowTimeStr(mList.get(index).getStart_time(),"HH:mm");
//            String end_time= GetTime.getNowTimeStr(mList.get(index).getEnd_time(),"HH:mm");
//            c.drawText(start_time, Text_x, Text_y, mPaint1);
//            c.drawText(end_time, Text_x, Text_y + getHeightPercent(context,0.03f), mPaint2);
        }
    }

}
