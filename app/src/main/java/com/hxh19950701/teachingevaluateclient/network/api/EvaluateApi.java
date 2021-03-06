package com.hxh19950701.teachingevaluateclient.network.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.CourseAndEvaluatedItem;
import com.hxh19950701.teachingevaluateclient.bean.response.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.bean.response.MaxAndMin;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.bean.response.TeacherCourseEvaluate;
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
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<EvaluateThirdTarget>>>(){});
    }

    public static ResponseStream getAllTargetsSync() throws HttpException {
        String action = "getAllTargets";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequestSync(URL, requestParams);
    }

    public static HttpHandler<String> commitEvaluate(int courseId, ServiceCallback<StudentCourseInfo> callBack) {
        String action = "commitEvaluate";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<StudentCourseInfo>>(){});
    }

    public static HttpHandler<String> updateItemScore(int courseId, int itemId, float score, ServiceCallback<StudentCourseEvaluate> callBack) {
        String action = "updateItemScore";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "", "itemId", itemId + "", "score", score + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<StudentCourseEvaluate>>(){});
    }

    public static HttpHandler<String> getStudentAllEvaluatedItemsByCourse(int courseId, ServiceCallback<CourseAndEvaluatedItem> callBack) {
        String action = "getStudentAllEvaluatedItemsByCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<CourseAndEvaluatedItem>>(){});
    }

    public static HttpHandler<String> getTeacherAllEvaluatedItemsByCourse(int courseId, ServiceCallback<List<TeacherCourseEvaluate>> callBack) {
        String action = "getTeacherAllEvaluatedItemsByCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<TeacherCourseEvaluate>>>(){});
    }

    public static HttpHandler<String> commentCourse(int courseId, String comment, ServiceCallback<StudentCourseInfo> callBack) {
        String action = "commentCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "", "comment", comment);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<StudentCourseInfo>>(){});
    }

    public static HttpHandler<String> getCompletedEvaluate(int courseId, ServiceCallback<List<StudentCourseInfo>> callBack) {
        String action = "getCompletedEvaluation";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<StudentCourseInfo>>>(){});
    }

    public static HttpHandler<String> getMaxAndMin(int courseId, ServiceCallback<MaxAndMin> callBack) {
        String action = "getMaxAndMin";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<MaxAndMin>>(){});
    }

    public static HttpHandler<String> reply(int id, String reply, ServiceCallback<StudentCourseInfo> callBack) {
        String action = "reply";
        RequestParams requestParams = NetClient.buildRequestParams(action, "id", id + "", "comment", reply);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<StudentCourseInfo>>(){});
    }
}