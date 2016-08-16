package com.hxh19950701.teachingevaluateclient.application;

import android.app.Application;

import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.ViewUtils;

public class TeachingEvaluateClientApplication extends Application {

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
    }

    public static void initServerURL() {
        serverURL = PrefUtils.getString("serverURL", "http://192.168.2.103:8080/TeachingEvaluateServer");
        departmentManagerURL = serverURL + "/departmentManager.action";
        userManagerURL = serverURL + "/userManager.action";
        studentManagerURL = serverURL + "/studentManager.action";
        courseManager = serverURL + "/courseManager.action";
        evaluateManager = serverURL + "/evaluateManager.action";
    }

    public static void initUtils() {
        PrefUtils.init(application);
        ViewUtils.init(application);
    }

    public static String getDepartmentManagerURL() {
        return departmentManagerURL;
    }

    public static String getUserManagerURL() {
        return userManagerURL;
    }

    public static String getStudentManagerURL() {
        return studentManagerURL;
    }

    public static String getServerURL() {
        return serverURL;
    }

    public static String getCourseManager() {
        return courseManager;
    }

    public static String getEvaluateManager() {
        return evaluateManager;
    }

    public static Application getApplication() {
        return application;
    }
}