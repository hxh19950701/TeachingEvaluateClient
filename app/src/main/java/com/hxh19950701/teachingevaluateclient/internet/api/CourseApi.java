package com.hxh19950701.teachingevaluateclient.internet.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.internet.NetClient;
import com.hxh19950701.teachingevaluateclient.internet.ServiceCallback;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

public class CourseApi {

    private static String URL = null;

    private CourseApi() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void init(String URL) {
        CourseApi.URL = URL + "/courseManager.action";
    }

    public static HttpHandler<String> getCourse(int courseId, ServiceCallback<Course> callBack) {
        String action = "getCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Course>>() {}.getType());
    }

    public static HttpHandler<String> getStudentCourseList(ServiceCallback<List<StudentCourseInfo>> callBack) {
        String action = "getStudentCourseList";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<StudentCourseInfo>>>() {}.getType());
    }

    public static HttpHandler<String> getStudentCourseList(int uid, ServiceCallback<List<StudentCourseInfo>> callBack) {
        String action = "getStudentCourseList";
        RequestParams requestParams = NetClient.buildRequestParams(action, "uid", uid + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Course>>() {}.getType());
    }

    public static HttpHandler<String> addCourse(int uid, int courseId, ServiceCallback<Course> callBack) {
        String action = "addCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "uid", uid + "", "courseId", courseId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Course>>() {}.getType());
    }

    public static HttpHandler<String> addCourse(int courseId, String password, ServiceCallback<Course> callBack) {
        String action = "addCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseId", courseId + "", "password", password);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Course>>() {}.getType());
    }

    public static HttpHandler<String> newCourse(String courseName, String password, int year, int term, int personCount, ServiceCallback<Course> callBack) {
        String action = "newCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "courseName", courseName, "password", password, "year", year + "", "term", term + "", "totalPersonCount", personCount + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Course>>() {}.getType());
    }

    public static HttpHandler<String> newCourse(int uid, String courseName, String password, int year, int term, ServiceCallback<Course> callBack) {
        String action = "newCourse";
        RequestParams requestParams = NetClient.buildRequestParams(action, "uid", uid + "", "courseName", courseName, "password", password, "year", year + "", "term", term + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Course>>() {}.getType());
    }

    public static HttpHandler<String> getTeacherCourseList(ServiceCallback<List<Course>> callBack) {
        String action = "getTeacherCourseList";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<Course>>>() {}.getType());
    }

    public static HttpHandler<String> getTeacherCourseList(int uid, ServiceCallback<List<Course>> callBack) {
        String action = "getTeacherCourseList";
        RequestParams requestParams = NetClient.buildRequestParams(action, "uid", uid + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<Course>>>() {}.getType());
    }
}
