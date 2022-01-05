package com.android.librarybase.basemethod;

import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTime {
    public static int NOT_IN=-1,IS_NORMAL_IN=3,START_TO=0,START_TO_END=1,TO_END=2;
    public static long getNowTimeLong(){
        long timecurrentTimeMillis = System.currentTimeMillis();
        return timecurrentTimeMillis;
    }
    public static String getNowTimeStrJinTian(long timecurrentTimeMillis){
        String result="";
        Date date = new Date(timecurrentTimeMillis);
        if(isToday(timecurrentTimeMillis)){
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            result = "今天 "+format.format(date);
        }
        else if(isNowYear(timecurrentTimeMillis)){
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
            result = format.format(date);
        }
        else{
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            result = format.format(date);
        }
        return result;
    }
    public static long timeToStamp(String timers,String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timers);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }


    public static String getNowTimeStr(long timecurrentTimeMillis,String pattern){
        String result="";
        Date date = new Date(timecurrentTimeMillis);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        result = format.format(date);
        return result;
    }
    public static boolean isToday(long when) {
        return getNowTimeStr(getNowTimeLong(), "yyyy/MM/dd").equals(getNowTimeStr(when, "yyyy/MM/dd"));
    }

    public static boolean isSameDay(long cal_date,long when) {
        return GetTime.getNowTimeStr(cal_date, "yyyy/MM/dd").equals(GetTime.getNowTimeStr(when, "yyyy/MM/dd"));
    }

    /**
     * -1不包含 0偏右 1包含 2偏左 3属于
     * @param start_
     * @param end_
     * @param target
     * @return
     * @throws ParseException
     */
    public static int checkTime(long start_,long end_,long target) throws ParseException {
        String start=getNowTimeStr(start_,"yyyy/MM/dd HH:mm");
        String end=getNowTimeStr(end_,"yyyy/MM/dd HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date s=df.parse(start);
        Date e=df.parse(end);

        String target_date = getNowTimeStr(target,"yyyy/MM/dd HH:mm");
        String substring = target_date.substring(0, 10);
        String target_start = substring + " 00:00";
        String target_end = substring + " 23:59";

        Date target_s = df.parse(target_start);
        Date target_e = df.parse(target_end);
        //不属于
        if(s.after(target_e) || e.before(target_s))
            return -1;
            //偏右
        else if(s.after(target_s) && e.after(target_e))
            return 0;
            //偏左
        else if(s.before(target_s) && e.before(target_e))
            return 2;
            //包含
        else if(s.before(target_s) && e.after(target_e))
            return 1;
        //属于
        return 3;
    }

    public static boolean isPlanGoing(long start,long end){
        if(start<=getNowTimeLong()&&getNowTimeLong()<=end)
            return true;
        return false;
    }

    public static boolean isNowYear(long when) {
        Time time = new Time("GTM+8");
        time.set(when);

        int thenYear = time.year;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year);
    }
}
