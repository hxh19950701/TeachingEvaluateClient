package com.hxh19950701.teachingevaluateclient.network;

import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.network.api.DepartmentApi;
import com.hxh19950701.teachingevaluateclient.network.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.network.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.network.api.TeacherApi;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;

public class NetService {

    private NetService() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void init(String URL) {
        UserApi.init(URL);
        StudentApi.init(URL);
        TeacherApi.init(URL);
        DepartmentApi.init(URL);
        EvaluateApi.init(URL);
        CourseApi.init(URL);
    }

}