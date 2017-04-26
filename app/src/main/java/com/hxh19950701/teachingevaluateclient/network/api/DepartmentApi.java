package com.hxh19950701.teachingevaluateclient.network.api;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.Clazz;
import com.hxh19950701.teachingevaluateclient.bean.response.Department;
import com.hxh19950701.teachingevaluateclient.bean.response.Subject;
import com.hxh19950701.teachingevaluateclient.network.NetClient;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;

import java.util.List;

public class DepartmentApi {

    private static String URL = null;

    private DepartmentApi() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void init(String URL) {
        DepartmentApi.URL = URL + "/departmentManager.action";
    }

    public static HttpHandler<String> getDepartmentList(ServiceCallback<List<Department>> callBack) {
        String action = "getDepartmentList";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<Department>>>(){});
    }

    public static HttpHandler<String> getSubjectList(ServiceCallback<List<Subject>> callBack) {
        String action = "getSubjectList";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<Subject>>>(){});
    }

    public static HttpHandler<String> getSubjectListByDepartment(int id, ServiceCallback<List<Subject>> callBack) {
        String action = "getSubjectListByDepartment";
        RequestParams requestParams = NetClient.buildRequestParams(action, "id", id + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<Subject>>>(){});
    }

    public static HttpHandler<String> getClazzList(ServiceCallback<List<Clazz>> callBack) {
        String action = "getClazzList";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<Clazz>>>(){});
    }

    public static HttpHandler<String> getClazzListBySubject(int id, ServiceCallback<List<Clazz>> callBack) {
        String action = "getClazzListBySubject";
        RequestParams requestParams = NetClient.buildRequestParams(action, "id", id + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<List<Clazz>>>(){});
    }

    public static ResponseStream getClazzListSync() throws HttpException {
        String action = "getClazzList";
        RequestParams requestParams = NetClient.buildRequestParams(action);
        return NetClient.sendPostRequestSync(URL, requestParams);
    }

    public static HttpHandler<String> createDepartment(String name, ServiceCallback<Department> callBack) {
        String action = "createDepartment";
        RequestParams requestParams = NetClient.buildRequestParams(action, "name", name);
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Department>>(){});
    }

    public static HttpHandler<String> createSubject(int departmentId, String name, ServiceCallback<Subject> callBack) {
        String action = "createSubject";
        RequestParams requestParams = NetClient.buildRequestParams(action, "name", name, "id", departmentId + "");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Subject>>(){});
    }

    public static HttpHandler<String> createClazz(int subjectId, int year, String name, ServiceCallback<Clazz> callBack) {
        String action = "createClazz";
        RequestParams requestParams = NetClient.buildRequestParams(action, "name", name, "year", year +"", "id", subjectId+"");
        return NetClient.sendPostRequest(URL, requestParams, callBack, new TypeToken<ResponseData<Clazz>>(){});
    }
}