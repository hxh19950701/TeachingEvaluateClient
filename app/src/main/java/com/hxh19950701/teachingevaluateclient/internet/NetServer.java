package com.hxh19950701.teachingevaluateclient.internet;

import android.app.Activity;
import android.content.Intent;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.ui.activity.LoginActivity;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestParams;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by hxh19950701 on 2016/6/1.
 */
public class NetServer {

    private NetServer(){

    }

    public static void login(String username, String password, BaseRequestCallBack<String> callBack) {
        BaseRequestParams requestParams = new BaseRequestParams();
        requestParams.addBodyParameter("action", "login");
        requestParams.addBodyParameter("username", username);
        requestParams.addBodyParameter("password", password);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.POST, TeachingEvaluateClientApplication.getUserManagerURL(), requestParams, callBack);
    }

    public static void logout() {
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("cookie", PrefUtils.getString("cookie", ""));
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET,
                TeachingEvaluateClientApplication.getUserManagerURL() + "?action=logout",
                requestParams, null);
    }

    public static void requireLoginAgain(Activity activity, String msg) {
        logout();
        Intent intent = new Intent(TeachingEvaluateClientApplication.getApplication(), LoginActivity.class);
        intent.putExtra("msg", msg);
        PrefUtils.putBoolean("AutoLogin", false);
        if (activity != null) {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            activity.finish();
        }
    }

}