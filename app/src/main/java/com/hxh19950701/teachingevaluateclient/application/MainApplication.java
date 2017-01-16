package com.hxh19950701.teachingevaluateclient.application;

import android.app.Application;

import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.event.ServerUrlChangedEvent;
import com.hxh19950701.teachingevaluateclient.event.UserLoginSuccessEvent;
import com.hxh19950701.teachingevaluateclient.interfaces.ManagerInitializeListener;
import com.hxh19950701.teachingevaluateclient.network.NetService;
import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;
import com.hxh19950701.teachingevaluateclient.service.DataUpdateService;
import com.hxh19950701.teachingevaluateclient.utils.DisplayUtils;
import com.hxh19950701.teachingevaluateclient.utils.InputMethodUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainApplication extends Application {

    private ManagerInitializeListener evaluateTargetManagerInitializeListener = new ManagerInitializeListener() {

        @Override
        public void onSuccess(boolean fromCache) {
            EvaluateTargetManager.printAllTargets();
        }

        @Override
        public void onFailure(Exception initException, Exception updateException) {
            initException.printStackTrace();
            updateException.printStackTrace();
            ToastUtils.show("更新评价条目失败，软件可能工作不正常");
        }

    };

    private ManagerInitializeListener departmentInfoManagerInitializeListener = new ManagerInitializeListener() {

        @Override
        public void onSuccess(boolean fromCache) {
            DepartmentInfoManager.printAllClasses();
        }

        @Override
        public void onFailure(Exception initException, Exception updateException) {
            initException.printStackTrace();
            updateException.printStackTrace();
            ToastUtils.show("更新系部班级信息失败，软件可能工作不正常");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initUtils();
        initServerURL();
        initManager();
        startServices();
        EventBus.getDefault().register(this);
    }

    public void initUtils() {
        PrefUtils.init(this);
        InputMethodUtils.init(this);
        DisplayUtils.init(this);
        ToastUtils.init(this);
    }

    public void initServerURL() {
        String domain = PrefUtils.getString(Constant.KEY_SERVER_DOMAIN, Constant.DEFAULT_SERVER_DOMAIN);
        NetService.init(domain);
    }

    public void initManager() {
        EvaluateTargetManager.setInitializeListener(evaluateTargetManagerInitializeListener);
        DepartmentInfoManager.setInitializeListener(departmentInfoManagerInitializeListener);
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
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.BACKGROUND)
    public void onServerUrlChanged(ServerUrlChangedEvent event) {
        System.out.println("服务器地址改变，正在重新初始化API");
        initServerURL();
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.BACKGROUND)
    public void onUserLoginSuccess(UserLoginSuccessEvent event) {
        System.out.println("用户" + event.getUsername() + "登录成功");
        PrefUtils.putString(Constant.KEY_USERNAME, event.getUsername());
        PrefUtils.putString(Constant.KEY_PASSWORD, event.isRememberPassword() ? event.getPassword() : "");
        PrefUtils.putBoolean(Constant.KEY_REMEMBER_PASSWORD, event.isRememberPassword());
        PrefUtils.putBoolean(Constant.KEY_AUTO_LOGIN, event.isAutoLoginEnable());
    }
}