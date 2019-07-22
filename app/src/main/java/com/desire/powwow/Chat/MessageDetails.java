package com.desire.powwow.Chat;

public class MessageDetails {

    public static int Type = 1;
    public static int Type2 = 2;
    String msg,UserName;
    int id;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
