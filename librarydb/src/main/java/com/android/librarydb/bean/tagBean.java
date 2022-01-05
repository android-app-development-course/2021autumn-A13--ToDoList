package com.android.librarydb.bean;

import cn.bmob.v3.BmobObject;

public class tagBean extends BmobObject {
    String tag_name;
    String accountName;
    int tag_color;
    
    @Override
    public String toString() {
        return "tagBean{" +
                "tag_name='" + tag_name + '\'' +
                ", accountName='" + accountName + '\'' +
                ", tag_color=" + tag_color +
                '}';
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getTag_color() {
        return tag_color;
    }

    public void setTag_color(int tag_color) {
        this.tag_color = tag_color;
    }
}
