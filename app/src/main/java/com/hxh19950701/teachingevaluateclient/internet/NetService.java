package com.hxh19950701.teachingevaluateclient.internet;

import com.hxh19950701.teachingevaluateclient.internet.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.internet.api.DepartmentApi;
import com.hxh19950701.teachingevaluateclient.internet.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.internet.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.internet.api.TeacherApi;
import com.hxh19950701.teachingevaluateclient.internet.api.UserApi;

public class NetService {

    private NetService() {
        throw new UnsupportedOperationException();
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