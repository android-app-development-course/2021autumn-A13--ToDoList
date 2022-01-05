package com.android.librarydb.bean__;

import android.util.Log;

import com.android.librarybase.basemethod.GetTime;

import cn.bmob.v3.BmobObject;

public class CardBean {

    //“//”后面是对应数据库的字段
    private int card_id;//card_id
    private String tag_name;//tag_name
    private String day_title;//card_name不同
    private long start_time;//start_time
    private long end_time;//end_time
    private String describe;//content不同
    private int tag_color;
    private boolean is_finish;//is_finish
    private boolean in_get_back;//in_get_back
    private String month_day_time;
    private boolean has_end_time;
    private boolean is_all_day;//is_all_day
    private int int_is_all_day;
    private int int_is_finish;
    private String sql_num;//sql_num云端数据库的自增id，不用写到数据库

    public String getSql_num() {
        return sql_num;
    }

    public void setSql_num(String sql_num) {
        this.sql_num = sql_num;
    }




    public String getMonth_day_time() {
        return month_day_time;
    }

    public CardBean(int card_id, String tag_name, String day_title, long start_time, long end_time, String describe, int tag_color, boolean is_finish ,boolean is_all_day) {
        this.card_id = card_id;
        this.tag_name = tag_name;
        this.day_title = day_title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.describe = describe;
        this.tag_color = tag_color;
        this.is_finish = is_finish;
        this.in_get_back = false;
        this.is_all_day=is_all_day;
        int_is_all_day=is_all_day?1:0;
        int_is_finish=is_finish?1:0;
        sql_num="";
        if(!is_all_day)this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日 HH:mm") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日 HH:mm");
        else this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日");
    }
    public CardBean(int card_id, String tag_name, String day_title, long start_time, long end_time, String describe, int tag_color, boolean is_finish ,boolean is_all_day,String sql_num){
        this.card_id = card_id;
        this.tag_name = tag_name;
        this.day_title = day_title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.describe = describe;
        this.tag_color = tag_color;
        this.is_finish = is_finish;
        this.in_get_back = false;
        this.is_all_day=is_all_day;
        this.sql_num=sql_num;
        int_is_all_day=is_all_day?1:0;
        int_is_finish=is_finish?1:0;
        if(!is_all_day)this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日 HH:mm") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日 HH:mm");
        else this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日");
    }

    public CardBean(){

    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getDay_title() {
        return day_title;
    }

    public void setDay_title(String day_title) {
        this.day_title = day_title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getTag_color() {
        return tag_color;
    }

    public void setTag_color(int tag_color) {
        this.tag_color = tag_color;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }


    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
        if(!is_all_day)this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日 HH:mm") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日 HH:mm");
        else this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日");
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
        if(!is_all_day)this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日 HH:mm") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日 HH:mm");
        else this.month_day_time = GetTime.getNowTimeStr(start_time, "MM月dd日") + " - " + GetTime.getNowTimeStr(end_time, "MM月dd日");
    }
    public boolean isIs_finish() {
        return is_finish;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
        int_is_finish=is_finish?1:0;
    }
    @Override
    public String toString() {
        return "CardBean{" +
                "card_id=" + card_id +
                ", tag_name='" + tag_name + '\'' +
                ", day_title='" + day_title + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", describe='" + describe + '\'' +
                ", tag_color=" + tag_color +
                ", is_finish=" + is_finish +
                ", month_day_time='" + month_day_time + '\'' +
                '}';
    }

    public boolean isHas_end_time() {
        return has_end_time;
    }

    public boolean isIn_get_back() {
        return in_get_back;
    }

    public void setIn_get_back(boolean in_get_back) {
        this.in_get_back = in_get_back;
    }

    public boolean isIs_all_day() {
        return is_all_day;
    }

    public void setIs_all_day(boolean is_all_day) {
        int_is_all_day=is_all_day?1:0;
        this.is_all_day = is_all_day;
    }

    public int getInt_is_all_day() {
        return int_is_all_day;
    }

    public int getInt_is_finish() {
        return int_is_finish;
    }
}
