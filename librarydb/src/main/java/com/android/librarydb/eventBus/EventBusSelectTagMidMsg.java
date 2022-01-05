package com.android.librarydb.eventBus;

public class EventBusSelectTagMidMsg {
    private boolean is_success;

    public String getTag_name() {
        return tag_name;
    }

    private String tag_name;
    public EventBusSelectTagMidMsg(boolean is){
        is_success=is;
    }
    public EventBusSelectTagMidMsg(boolean is, String tag_name){
        is_success=is;
        this.tag_name=tag_name;
    }

    public boolean isIs_success() {
        return is_success;
    }


}
