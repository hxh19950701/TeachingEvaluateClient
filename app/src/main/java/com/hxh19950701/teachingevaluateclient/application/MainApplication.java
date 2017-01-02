package com.hxh19950701.teachingevaluateclient.application;

import android.app.Application;

import com.hxh19950701.teachingevaluateclient.internet.NetService;
import com.hxh19950701.teachingevaluateclient.utils.InputMethodUtils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.ViewUtils;

public class MainApplication extends Application {

    private static String serverURL;

    private static String departmentManagerURL;
    private static String userManagerURL;
    private static String studentManagerURL;
    private static String courseManager;
    private static String evaluateManager;

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initUtils();
        initServerURL();
        NetService.init(serverURL);
    }

    public void initServerURL() {
        serverURL = PrefUtils.getString("serverURL", "http://192.168.191.1:8080/TeachingEvaluateServer");
        departmentManagerURL = serverURL + "/departmentManager.action";
        userManagerURL = serverURL + "/userManager.action";
        studentManagerURL = serverURL + "/studentManager.action";
        courseManager = serverURL + "/courseManager.action";
        evaluateManager = serverURL + "/evaluateManager.action";
    }

    public void initUtils() {
        PrefUtils.init(this);
        ViewUtils.init(this);
        InputMethodUtils.init(this);
    }

    public static Application getApplication() {
        return application;
    }
}