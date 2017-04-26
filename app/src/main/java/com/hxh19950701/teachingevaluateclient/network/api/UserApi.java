package com.hxh19950701.teachingevaluateclient.network.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.LoginRecord;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.network.NetClient;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

public class UserApi {

    private static String URL = null;

    private UserApi() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void init(String URL) {
        UserApi.URL = URL + "/userManager.action";
    }

    public static HttpHandler<String> hasExist(String username, ServiceCallback<Boolean> callBack) {
        String action = "hasExist";
        RequestParams requestParams = NetClient.buildRequestParams(action, "username", username);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Boolean>>(){});
    }

    public static HttpHandler<String> register(String username, String password, ServiceCallback<User> callBack) {
        String action = "register";
        RequestParams requestParams = NetClient.buildRequestParams(action, "username", username, "password", password);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>(){});
    }

    public static HttpHandler<String> register(String username, String password, int identity, ServiceCallback<User> callBack) {
        String action = "register";
        RequestParams requestParams = NetClient.buildRequestParams(action, "username", username, "password", password, "identity", identity+"");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>(){});
    }

    public static HttpHandler<String> login(String username, String password, ServiceCallback<User> callBack) {
        String action = "login";
        RequestParams requestParams = NetClient.buildRequestParams(action, "username", username, "password", password);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>(){});
    }

    public static HttpHandler<String> logout(ServiceCallback<User> callBack) {
        String action = "logout";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>(){});
    }

    public static HttpHandler<String> modifyPassword(String currentPassword, String newPassword, ServiceCallback<User> callBack) {
        String action = "modifyPassword";
        RequestParams requestParams = NetClient.buildRequestParams(action, "password", currentPassword, "newPassword", newPassword);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>() {});
    }

    public static HttpHandler<String> getAllUser(ServiceCallback<List<User>> callBack) {
        String action = "getAllUser";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<User>>>() {});
    }

    public static HttpHandler<String> modifyEnable(int uid, boolean enable, ServiceCallback<User> callBack) {
        String action = "modifyEnable";
        RequestParams requestParams = NetClient.buildRequestParams(action,"uid",uid + "", "enable", enable + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>() {});
    }

    public static HttpHandler<String> resetPassword(int uid, ServiceCallback<User> callBack) {
        String action = "resetPassword";
        RequestParams requestParams = NetClient.buildRequestParams(action,"uid", uid + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>() {});
    }

    public static HttpHandler<String> getUser(int uid, ServiceCallback<User> callBack) {
        String action = "getUser";
        RequestParams requestParams = NetClient.buildRequestParams(action,"uid", uid + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>() {});
    }

    public static HttpHandler<String> getRecords(int uid, ServiceCallback<List<LoginRecord>> callBack) {
        String action = "getRecords";
        RequestParams requestParams = NetClient.buildRequestParams(action,"uid", uid + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<LoginRecord>>>() {});
    }
}