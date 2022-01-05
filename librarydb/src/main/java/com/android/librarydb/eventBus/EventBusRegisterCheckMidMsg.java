package com.android.librarydb.eventBus;

public class EventBusRegisterCheckMidMsg {
    private boolean has_username;



    private boolean register_success;
    public EventBusRegisterCheckMidMsg(boolean has_username, boolean register_success){
        this.has_username=has_username;
        this.register_success=register_success;
    }
    public boolean isHas_username() {
        return has_username;
    }
    public boolean isRegister_success() {
        return register_success;
    }

}
