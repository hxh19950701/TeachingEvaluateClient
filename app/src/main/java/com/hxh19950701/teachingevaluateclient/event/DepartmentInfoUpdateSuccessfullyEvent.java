package com.hxh19950701.teachingevaluateclient.event;

public class DepartmentInfoUpdateSuccessfullyEvent {

    public boolean fromCache;

    public DepartmentInfoUpdateSuccessfullyEvent(boolean fromCache) {
        this.fromCache = fromCache;
    }
}
