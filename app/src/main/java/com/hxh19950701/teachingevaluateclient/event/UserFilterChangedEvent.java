package com.hxh19950701.teachingevaluateclient.event;

public class UserFilterChangedEvent {

    public String username;
    public int identity;
    public int enable;

    public UserFilterChangedEvent(String username, int identity, int enable) {
        this.username = username;
        this.identity = identity;
        this.enable = enable;
    }
}