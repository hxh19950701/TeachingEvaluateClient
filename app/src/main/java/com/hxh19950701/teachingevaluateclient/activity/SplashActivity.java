package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.exception.HttpException;

public class SplashActivity extends AppCompatActivity {

    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_PROCEED = 2;
    private static final int LOGIN_NOT_START = 3;
    private static final int LOGIN_FAIL_DUE_WRONG_USERNAME = 4;
    private static final int LOGIN_FAIL_DUE_WRONG_PASSWORD = 5;
    private static final int LOGIN_FAIL_DUE_SERVER_ERROR = 6;
    private static final int LOGIN_FAIL_DUE_NETWORK_FAILURE = 7;
    private static final int LOGIN_FAIL_DUE_UNKNOWN = 8;
    private static final long SPLASH_DURATION = 1500L;

    private final Thread splashTimer = new Thread(() -> {
        SystemClock.sleep(SPLASH_DURATION);
        isTimeUp = true;
        requireDismiss();
    });

    private int loginStatus = LOGIN_NOT_START;
    private boolean isTimeUp = false;
    private int identity = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashTimer.start();
        if (PrefUtils.getBoolean(Constant.KEY_AUTO_LOGIN, false)) {
            loginAuto();
        } else {
            loginStatus = LOGIN_NOT_START;
        }
    }

    private void loginAuto() {
        loginStatus = LOGIN_PROCEED;
        String username = PrefUtils.getString(Constant.KEY_USERNAME, "");
        String password = PrefUtils.getString(Constant.KEY_PASSWORD, "");
        UserApi.login(username, password, new ServiceCallback<User>() {

            @Override
            public void onAfter() {
                requireDismiss();
            }

            @Override
            public void onSuccess(ResponseData<User> data) {
                switch (data.getCode()) {
                    case Constant.CODE_SUCCESS:
                        identity = data.getData().getIdentity();
                        loginStatus = LOGIN_SUCCESS;
                        break;
                    case Constant.CODE_NO_SUCH_USERNAME:
                    case Constant.CODE_INVALID_USERNAME:
                        loginStatus = LOGIN_FAIL_DUE_WRONG_USERNAME;
                        PrefUtils.remove(Constant.KEY_USERNAME);
                        PrefUtils.remove(Constant.KEY_PASSWORD);
                        break;
                    case Constant.CODE_INCORRECT_PASSWORD:
                    case Constant.CODE_INVALID_PASSWORD:
                        loginStatus = LOGIN_FAIL_DUE_WRONG_PASSWORD;
                        PrefUtils.remove(Constant.KEY_PASSWORD);
                        break;
                    default:
                        loginStatus = LOGIN_FAIL_DUE_UNKNOWN;
                        break;
                }
            }

            @Override
            public void onJsonSyntaxException(String s) {
                loginStatus = LOGIN_FAIL_DUE_SERVER_ERROR;
            }

            @Override
            public void onFailure(HttpException e, String s) {
                loginStatus = LOGIN_FAIL_DUE_NETWORK_FAILURE;
            }
        });
    }

    private boolean requireDismiss() {
        if (isTimeUp && loginStatus != LOGIN_PROCEED) {
            switch (loginStatus) {
                case LOGIN_SUCCESS:
                    enterApp();
                    break;
                default:
                    enterLogin();
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    private void enterApp() {
        try {
            ActivityUtils.enterApp(this, identity);
        } catch (IllegalArgumentException e) {
            enterLogin();
        }
    }

    private void enterLogin() {
        IntentUtils.startActivity(this, LoginActivity.class,
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    @Override
    public void onBackPressed() {
        //不允许用户按返回键退出
    }
}
