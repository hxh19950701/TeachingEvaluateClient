package com.hxh19950701.teachingevaluateclient.application;

import android.app.Application;
import android.util.Log;

import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.event.ServerUrlChangedEvent;
import com.hxh19950701.teachingevaluateclient.event.UserLoginSuccessfullyEvent;
import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.NetService;
import com.hxh19950701.teachingevaluateclient.service.DataUpdateService;
import com.hxh19950701.teachingevaluateclient.utils.ConnectivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.DisplayUtils;
import com.hxh19950701.teachingevaluateclient.utils.InputMethodUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.ToastUtils;
import com.tencent.bugly.Bugly;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        initUtils();
        initServerURL();
        initManager();
        startServices();
        EventManager.register(this);
        Bugly.init(getApplicationContext(), "ad61c01574", false);
    }

    public void initUtils() {
        PrefUtils.init(this);
        InputMethodUtils.init(this);
        DisplayUtils.init(this);
        ToastUtils.init(this);
        ConnectivityUtils.init(this);
    }

    public void initServerURL() {
        String domain = PrefUtils.getString(Constant.KEY_SERVER_DOMAIN, Constant.DEFAULT_SERVER_DOMAIN);
        NetService.init(domain);
    }

    public void initManager() {
        EvaluateTargetManager.setInitializeListener(EvaluateTargetManager.getDefaultInitializeListener());
        DepartmentInfoManager.setInitializeListener(DepartmentInfoManager.getDefaultInitializeListener());
    }

    public void startServices() {
        IntentUtils.startService(this, DataUpdateService.class);
    }

    public void stopServices() {
        IntentUtils.stopService(this, DataUpdateService.class);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopServices();
        EventManager.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onServerUrlChanged(ServerUrlChangedEvent event) {
        Log.d(TAG, "服务器地址改变，正在重新初始化API");
        initServerURL();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onUserLoginSuccess(UserLoginSuccessfullyEvent event) {
        Log.d(TAG, "用户" + event.getUsername() + "登录成功");
        PrefUtils.putString(Constant.KEY_USERNAME, event.getUsername());
        PrefUtils.putString(Constant.KEY_PASSWORD, event.isRememberPassword() ? event.getPassword() : "");
        PrefUtils.putBoolean(Constant.KEY_REMEMBER_PASSWORD, event.isRememberPassword());
        PrefUtils.putBoolean(Constant.KEY_AUTO_LOGIN, event.isAutoLoginEnable());
    }
}