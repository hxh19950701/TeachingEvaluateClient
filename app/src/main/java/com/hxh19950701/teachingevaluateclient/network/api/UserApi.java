package com.hxh19950701.teachingevaluateclient.network.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.service.User;
import com.hxh19950701.teachingevaluateclient.network.NetClient;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

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

    public static HttpHandler<String> registerStudent(String username, String password, ServiceCallback<User> callBack) {
        String action = "registerStudent";
        RequestParams requestParams = NetClient.buildRequestParams(action, "username", username, "password", password);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<User>>(){});
    }

    public static HttpHandler<String> registerTeacher(String username, String password, ServiceCallback<User> callBack) {
        String action = "registerTeacher";
        RequestParams requestParams = NetClient.buildRequestParams(action, "username", username, "password", password);
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
}
