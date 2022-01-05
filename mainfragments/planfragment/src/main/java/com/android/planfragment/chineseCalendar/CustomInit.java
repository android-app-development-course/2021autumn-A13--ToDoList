package com.android.planfragment.chineseCalendar;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.android.librarybase.basemethod.GetTime;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.eventBus.EventBusSelectTimeMidMsg;
import com.android.planfragment.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.qmuiteam.qmui.layout.QMUILinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CustomInit implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener{


    private TextView mTextMonthDay;
    private TextView mTextYear;
    private TextView mTextLunar;
    private TextView mTextCurrentDay;
    private CalendarView mCalendarView;
    private RelativeLayout mRelativeTool;
    private FrameLayout mFlToday;
    private int mYear;
    private CalendarLayout mCalendarLayout;
    private View mRootView;
    private Context mContext;
    public CustomInit(View view,Context context){
        mRootView=view;
        mContext=context;
    }
    private void setListener(){
        //点击月打开月视图和年视图
//        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mCalendarLayout.isExpand()) {
//                    mCalendarLayout.expand();
//                    return;
//                }
//                mCalendarView.showYearSelectLayout(mYear);
//                mTextLunar.setVisibility(View.GONE);
//                mTextYear.setVisibility(View.GONE);
//                mTextMonthDay.setText(String.valueOf(mYear));
//            }
//        });
        mFlToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
                mTextLunar.setText("今日");
            }
        });
    }
    public void customInitView(){
        mTextMonthDay = mRootView.findViewById(R.id.tv_month_day);
        mTextYear = mRootView.findViewById(R.id.tv_year);
        mTextLunar = mRootView.findViewById(R.id.tv_lunar);
        mRelativeTool = mRootView.findViewById(R.id.rl_tool);
        mCalendarView =  mRootView.findViewById(R.id.calendarView);
        mTextCurrentDay =  mRootView.findViewById(R.id.tv_current_day);
        mFlToday = mRootView.findViewById(R.id.fl_current);
        mCalendarLayout = mRootView.findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        /*
        * 设置监听器
        * */
        setListener();
    }
    public void customInitData(){

        //设置标记
/*

        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
*/
        setCalendarShi(mCalendarView.getCurYear(),mCalendarView.getCurMonth());
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    public void setCalendarShi(int year,int month){
        Log.e("刷新","刷新");

        List<CardBean>list = SQLiteTools.getAllCards(
                mContext,
                SharedStorage.isReverseTimeLine(mContext),
                SharedStorage.isStartTimeLine(mContext),
                SharedStorage.isFinishTimeline(mContext));
        Map<String, Calendar> map = new HashMap<>();
        Set<String>baohan_days =  new HashSet<>();
        for (int i=0;i<list.size();++i){
            long stime=list.get(i).getStart_time();
            long etime=list.get(i).getEnd_time();
            long one_day_second=60*60*24;
            for (int j=0;;++j){
                if(stime+j*one_day_second<=etime){
                    baohan_days.add(GetTime.getNowTimeStr(stime+j*one_day_second,"yyyy/MM/dd"));
                }
                else break;
            }
        }
//        for (String str:baohan_days){
//            String[] split = str.split("/");
//            int year = Integer.parseInt(split[0]);
//            int month = Integer.parseInt(split[1]);
//            int day = Integer.parseInt(split[2]);
//            map.put(getSchemeCalendar(year, month, day, 0xFFe69138, "事").toString(),
//                    getSchemeCalendar(year, month, day, 0xFFe69138, "事"));
//        }
//        mCalendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        if(calendar.getDay()!=mCalendarView.getCurDay())mTextLunar.setText(calendar.getLunar());
        else mTextLunar.setText("今日");

        mYear = calendar.getYear();

        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());


        long mTime = GetTime.timeToStamp(calendar.getYear() + "/" + calendar.getMonth() + "/" + calendar.getDay(),"yyyy/MM/dd");
        EventBus.getDefault().post(new EventBusSelectTimeMidMsg(true,mTime));

    }

    @Override
    public void onYearChange(int year) {

    }
}
