package com.hxh19950701.teachingevaluateclient.internet.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.internet.NetClient;
import com.hxh19950701.teachingevaluateclient.internet.ServiceCallback;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

public class EvaluateApi {

    private static String URL = null;

    private EvaluateApi() {
        throw new UnsupportedOperationException();
    }

    public static void init(String URL) {
        EvaluateApi.URL = URL + "/evaluateManager.action";
    }

    public static HttpHandler<String> getAllTargets(ServiceCallback<List<EvaluateThirdTarget>> callBack) {
        String action = "getAllTargets";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<EvaluateThirdTarget>>>(){}.getType());
    }

    public static HttpHandler<String> commitEvaluate(int courseId, ServiceCallback<StudentCourseInfo> callBack) {
        String action = "commitEvaluate";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<StudentCourseInfo>>(){}.getType());
    }

    public static HttpHandler<String> updateItemScore(int courseId, int itemId, float score, ServiceCallback<StudentCourseEvaluate> callBack) {
        String action = "updateItemScore";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "", "itemId", itemId + "", "score", score + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<StudentCourseEvaluate>>(){}.getType());
    }

    public static HttpHandler<String> getStudentAllEvaluatedItemsByCourse(int courseId, ServiceCallback<List<StudentCourseEvaluate>> callBack) {
        String action = "getStudentAllEvaluatedItemsByCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<StudentCourseEvaluate>>>(){}.getType());
    }
}