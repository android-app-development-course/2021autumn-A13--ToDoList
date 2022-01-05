package com.android.librarydb.eventBus;

import com.android.librarydb.bean__.CardBean;

public class EventBusCardMidMsg {
    private boolean is_success;
    private CardBean cardBean;
    public EventBusCardMidMsg(boolean is){
        is_success=is;
    }
    public EventBusCardMidMsg(boolean is,CardBean cardBean){
        is_success=is;
        this.cardBean=cardBean;
    }

    public boolean isIs_success() {
        return is_success;
    }
}
