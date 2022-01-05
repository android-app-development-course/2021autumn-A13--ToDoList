package com.android.librarydb.eventBus;

public class EventBusSetNewTagMidMsg {
    private boolean is_success;
    public EventBusSetNewTagMidMsg(boolean is){
        is_success=is;
    }

    public boolean isIs_success() {
        return is_success;
    }
}
