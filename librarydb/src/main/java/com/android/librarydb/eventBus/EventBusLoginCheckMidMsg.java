package com.android.librarydb.eventBus;

public class EventBusLoginCheckMidMsg {
    private boolean has_username,wrong_pwd,login_success;
    public EventBusLoginCheckMidMsg(boolean has_username,boolean wrong_pwd,boolean login_success){
        this.has_username=has_username;
        this.wrong_pwd=wrong_pwd;
        this.login_success=login_success;
    }
    public boolean isHas_username() {
        return has_username;
    }

    public boolean isWrong_pwd() {
        return wrong_pwd;
    }

    public boolean isLogin_success() {
        return login_success;
    }

}
