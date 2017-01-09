package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.service.User;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.internet.ServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.exception.HttpException;

public class SplashActivity extends AppCompatActivity {

    private static final Class[] ACTIVITIES = new Class[Constant.IDENTITY_COUNT];

    static {
        ACTIVITIES[Constant.IDENTITY_STUDENT] = StudentMainUiActivity.class;
        ACTIVITIES[Constant.IDENTITY_TEACHER] = TeacherMainUiActivity.class;
        ACTIVITIES[Constant.IDENTITY_ADMINISTRATOR] = AdministratorMainUiActivity.class;
    }

    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_PROCEED = 2;
    private static final int LOGIN_NOT_START = 3;
    private static final int LOGIN_FAIL_DUE_WRONG_USERNAME = 4;
    private static final int LOGIN_FAIL_DUE_WRONG_PASSWORD = 5;
    private static final int LOGIN_FAIL_DUE_SERVER_ERROR = 6;
    private static final int LOGIN_FAIL_DUE_NETWORK_FAILURE = 7;

    private static final long SPLASH_DURATION = 2000L;
    private static final long DELAY_FINISH_DURATION = 300L;

    private int loginStatus = LOGIN_NOT_START;
    private boolean isTimeUp = false;
    private int identity = -1;

    private class SplashThread extends Thread {
        @Override
        public void run() {
            super.run();
            SystemClock.sleep(SPLASH_DURATION);
            isTimeUp = true;
            requireDismiss();
        }
    }

    private class DelayFinishThread extends Thread {
        @Override
        public void run() {
            super.run();
            SystemClock.sleep(DELAY_FINISH_DURATION);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PrefUtils.getBoolean(Constant.KEY_AUTO_LOGIN, false)) {
            loginAuto();
        } else {
            loginStatus = LOGIN_NOT_START;
        }
        new SplashThread().start();
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
                    case Constant.ERROR_NO_SUCH_USERNAME:
                    case Constant.ERROR_INVALID_USERNAME:
                        loginStatus = LOGIN_FAIL_DUE_WRONG_USERNAME;
                        PrefUtils.remove(Constant.KEY_USERNAME);
                        PrefUtils.remove(Constant.KEY_PASSWORD);
                        break;
                    case Constant.ERROR_INCORRECT_PASSWORD:
                    case Constant.ERROR_INVALID_PASSWORD:
                        loginStatus = LOGIN_FAIL_DUE_WRONG_PASSWORD;
                        PrefUtils.remove(Constant.KEY_PASSWORD);
                        break;
                }
            }

            @Override
            public void onException(String s) {
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
                case LOGIN_FAIL_DUE_WRONG_PASSWORD:
                case LOGIN_FAIL_DUE_SERVER_ERROR:
                case LOGIN_FAIL_DUE_NETWORK_FAILURE:
                case LOGIN_FAIL_DUE_WRONG_USERNAME:
                case LOGIN_NOT_START:
                    enterLogin();
                    break;
                case LOGIN_SUCCESS:
                    enterApp();
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    private void enterApp() {
        switch (identity) {
            case Constant.IDENTITY_STUDENT:
            case Constant.IDENTITY_TEACHER:
            case Constant.IDENTITY_ADMINISTRATOR:
                IntentUtils.startActivity(this, ACTIVITIES[identity], Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                new DelayFinishThread().start();
                break;
            default:
                enterLogin();
        }
    }

    private void enterLogin() {
        IntentUtils.startActivity(this, LoginActivity.class);
        new DelayFinishThread().start();
    }
}
