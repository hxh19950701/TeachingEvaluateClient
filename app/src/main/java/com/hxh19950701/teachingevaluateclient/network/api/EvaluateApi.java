package com.hxh19950701.teachingevaluateclient.network.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.CourseAndEvaluatedItem;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.bean.service.TeacherCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.network.NetClient;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;

import java.util.List;

public class EvaluateApi {

    private static String URL = null;

    private EvaluateApi() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void init(String URL) {
        EvaluateApi.URL = URL + "/evaluateManager.action";
    }

    public static HttpHandler<String> getAllTargets(ServiceCallback<List<EvaluateThirdTarget>> callBack) {
        String action = "getAllTargets";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<EvaluateThirdTarget>>>(){}.getType());
    }

    public static ResponseStream getAllTargetsSync() throws HttpException {
        String action = "getAllTargets";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequestSync(URL, requestParams);
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

    public static HttpHandler<String> getStudentAllEvaluatedItemsByCourse(int courseId, ServiceCallback<CourseAndEvaluatedItem> callBack) {
        String action = "getStudentAllEvaluatedItemsByCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<CourseAndEvaluatedItem>>(){}.getType());
    }

    public static HttpHandler<String> getTeacherAllEvaluatedItemsByCourse(int courseId, ServiceCallback<List<TeacherCourseEvaluate>> callBack) {
        String action = "getTeacherAllEvaluatedItemsByCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<TeacherCourseEvaluate>>>(){}.getType());
    }
}