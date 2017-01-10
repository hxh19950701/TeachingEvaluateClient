package com.hxh19950701.teachingevaluateclient.event;

public class ServerUrlChangedEvent {

    private String newUrl;

    public ServerUrlChangedEvent(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }
}
