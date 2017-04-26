package com.hxh19950701.teachingevaluateclient.network.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.Student;
import com.hxh19950701.teachingevaluateclient.network.NetClient;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

public class StudentApi {

    private static String URL = null;

    private StudentApi() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void init(String URL) {
        StudentApi.URL = URL + "/studentManager.action";
    }

    public static HttpHandler<String> hasExist(String studentId, ServiceCallback<Boolean> callBack) {
        String action = "hasExist";
        RequestParams requestParams = NetClient.buildRequestParams(action, "studentId", studentId);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Boolean>>(){});
    }

    public static HttpHandler<String> register(String studentId, String name, int sex, int clazzId, ServiceCallback<Student> callBack) {
        String action = "register";
        RequestParams requestParams = NetClient.buildRequestParams(action, "studentId", studentId, "name", name, "sex", sex + "", "clazzId", clazzId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Student>>(){});
    }

    public static HttpHandler<String> currentStudent(ServiceCallback<Student> callBack) {
        String action = "currentStudent";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Student>>(){});
    }

    public static HttpHandler<String> get(int uid, ServiceCallback<Student> callBack) {
        String action = "get";
        RequestParams requestParams = NetClient.buildRequestParams(action, "uid", uid + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Student>>() {});
    }
}