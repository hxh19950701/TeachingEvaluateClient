package com.hxh19950701.teachingevaluateclient.application;

import android.app.Application;
import android.widget.Toast;

import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.internet.NetService;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;
import com.hxh19950701.teachingevaluateclient.utils.DisplayUtils;
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
        DisplayUtils.init(this);
    }

    public void initServerURL() {
        String domain = PrefUtils.getString(Constant.KEY_SERVER_DOMAIN, Constant.DEFAULT_SERVER_DOMAIN);
        NetService.init(domain);
    }

    public void initManager() {
        EvaluateTargetManager.setInitializeListener(new EvaluateTargetManager.InitializeListener() {
            @Override
            public void onSuccess(boolean fromCache) {
                Toast.makeText(MainApplication.this, "更新评价条目成功", Toast.LENGTH_SHORT).show();
                EvaluateTargetManager.printAllTargets();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainApplication.this, "更新评价条目失败", Toast.LENGTH_SHORT).show();
            }
        });
        EvaluateTargetManager.init(this);
    }
}