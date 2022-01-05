package com.android.librarydb.eventBus;

public class EventBusSelectTimeMidMsg {
    private boolean is_success;
    private long mTime;
    public EventBusSelectTimeMidMsg(boolean is){
        is_success=is;
    }
    public EventBusSelectTimeMidMsg(boolean is, long mTime){
        is_success=is;
        this.mTime=mTime;
    }

    public boolean isIs_success() {
        return is_success;
    }

    public long getmTime() {
        return mTime;
    }
}
