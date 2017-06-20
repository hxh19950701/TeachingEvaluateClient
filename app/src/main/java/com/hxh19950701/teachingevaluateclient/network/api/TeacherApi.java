package com.hxh19950701.teachingevaluateclient.network.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.Code;
import com.hxh19950701.teachingevaluateclient.bean.response.Teacher;
import com.hxh19950701.teachingevaluateclient.network.NetClient;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

public class TeacherApi {

    private static String URL = null;

    private TeacherApi() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void init(String URL) {
        TeacherApi.URL = URL + "/teacherManager.action";
    }

    public static HttpHandler<String> hasExist(String teacherId, ServiceCallback<Boolean> callBack) {
        String action = "hasExist";
        RequestParams requestParams = NetClient.buildRequestParams(action, "teacherId", teacherId);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Boolean>>(){});
    }

    public static HttpHandler<String> register(String teacherId, String name, int sex, ServiceCallback<Teacher> callBack) {
        String action = "register";
        RequestParams requestParams = NetClient.buildRequestParams(action, "teacherId", teacherId, "name", name, "sex", sex + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Teacher>>(){});
    }

    public static HttpHandler<String> currentTeacher(ServiceCallback<Teacher> callBack) {
        String action = "currentTeacher";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Teacher>>(){});
    }

    public static HttpHandler<String> get(int uid, ServiceCallback<Teacher> callBack) {
        String action = "getTeacherByUid";
        RequestParams requestParams = NetClient.buildRequestParams(action, "uid", uid + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Teacher>>() {});
    }

    public static HttpHandler<String> createCode(ServiceCallback<Code> callBack) {
        String action = "createCode";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Code>>() {});
    }
}