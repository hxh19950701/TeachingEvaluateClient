package com.hxh19950701.teachingevaluateclient.internet.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.service.Teacher;
import com.hxh19950701.teachingevaluateclient.internet.NetClient;
import com.hxh19950701.teachingevaluateclient.internet.ServiceCallback;
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
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Boolean>>(){}.getType());
    }

    public static HttpHandler<String> register(String teacherId, String name, int sex, ServiceCallback<Teacher> callBack) {
        String action = "register";
        RequestParams requestParams = NetClient.buildRequestParams(action, "teacherId", teacherId, "name", name, "sex", sex + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Teacher>>(){}.getType());
    }

    public static HttpHandler<String> currentTeacher(ServiceCallback<Teacher> callBack) {
        String action = "currentTeacher";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Teacher>>(){}.getType());
    }
}