package com.android.librarydb.eventBus;

public class EventBusCheckGetBackRefreshMidMsg {
    private boolean is_success;


    public EventBusCheckGetBackRefreshMidMsg(boolean is){
        is_success=is;
    }

    public boolean isIs_success() {
        return is_success;
    }
}
