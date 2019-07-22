package com.desire.powwow.Notifications;

public class FcmDataMode {
    String fcmId, name;

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FcmDataMode(String fcmId, String name) {
        this.fcmId = fcmId;
        this.name = name;
    }
}
