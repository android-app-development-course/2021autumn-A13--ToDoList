package com.android.librarydb.eventBus;

public class EventBusLoginOrLogoutMidMsg {
    private boolean is_success;
    private boolean is_login;
    public EventBusLoginOrLogoutMidMsg(boolean is,boolean is_login){
        is_success=is;
        this.is_login=is_login;
    }

    public boolean isIs_success() {
        return is_success;
    }

    public boolean isIs_login() {
        return is_login;
    }
}
