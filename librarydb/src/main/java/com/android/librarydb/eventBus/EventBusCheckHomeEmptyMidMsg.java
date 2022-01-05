package com.android.librarydb.eventBus;

import com.android.librarydb.bean__.CardBean;

public class EventBusCheckHomeEmptyMidMsg {
    private boolean is_success;

    private boolean is_empty;

    public boolean isIs_empty() {
        return is_empty;
    }
    public EventBusCheckHomeEmptyMidMsg(boolean is,boolean is_empty){
        is_success=is;
        this.is_empty=is_empty;
    }

    public boolean isIs_success() {
        return is_success;
    }
}
