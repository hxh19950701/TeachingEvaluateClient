package com.hxh19950701.teachingevaluateclient.application;

import android.app.Application;

import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.internet.NetService;
import com.hxh19950701.teachingevaluateclient.utils.InputMethodUtils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initUtils();
        initServerURL();
        initManager();
    }

    public void initUtils() {
        PrefUtils.init(this);
        InputMethodUtils.init(this);
    }

    public void initServerURL() {
        String domain = PrefUtils.getString(Constant.KEY_SERVER_DOMAIN, Constant.DEFAULT_SERVER_DOMAIN);
        NetService.init(domain);
    }

    public void initManager() {
    }
}