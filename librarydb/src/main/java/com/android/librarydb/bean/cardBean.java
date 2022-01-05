package com.android.librarydb.bean;

import cn.bmob.v3.BmobObject;

public class cardBean extends BmobObject {
    private int card_id;//card_id
    private String tag_name;//tag_name
    private String day_title;//card_name不同
    private long start_time;//start_time
    private long end_time;//end_time
    private String describe;//content不同
    private boolean is_finish;//is_finish
    private boolean in_get_back;//in_get_back
    private boolean is_all_day;//is_all_day
    private String accountName;


    @Override
    public String toString() {
        return "cardBean{" +
                "card_id=" + card_id +
                ", tag_name='" + tag_name + '\'' +
                ", day_title='" + day_title + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", describe='" + describe + '\'' +
                ", is_finish=" + is_finish +
                ", in_get_back=" + in_get_back +
                ", is_all_day=" + is_all_day +
                ", accountName='" + accountName + '\'' +
                '}';
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
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

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isIs_finish() {
        return is_finish;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
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
        this.is_all_day = is_all_day;
    }

}
