package com.android.librarydb.bean;

import cn.bmob.v3.BmobObject;

public class accountBean extends BmobObject {
    String name;
    String password;
    String sex;
    String head;

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getNikeName() {
        return nikeName;
    }

    String nikeName;

    @Override
    public String toString() {
        return "accountBean{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", head='" + head + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }

    String describe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

}
