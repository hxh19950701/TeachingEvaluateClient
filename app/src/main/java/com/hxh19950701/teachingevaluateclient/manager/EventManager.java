package com.hxh19950701.teachingevaluateclient.manager;

import org.greenrobot.eventbus.EventBus;

public class EventManager {

    private EventManager() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static final EventBus eventBus = EventBus.getDefault();

    public static EventBus getEventBus() {
        return eventBus;
    }

    public static boolean isRegistered(Object subscriber) {
        return getEventBus().isRegistered(subscriber);
    }

    public static void register(Object subscriber) {
        getEventBus().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        getEventBus().unregister(subscriber);
    }

    public static void postEvent(Object event){
        getEventBus().post(event);
    }

    public static void postStickyEvent(Object event){
        getEventBus().postSticky(event);
    }
}
