package com.hxh19950701.teachingevaluateclient.utils;

import com.hxh19950701.teachingevaluateclient.common.Constant;

public class UserUtils {

    private UserUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static String getIdentityString(int identity) {
        switch (identity) {
            case Constant.IDENTITY_STUDENT:
                return "学生";
            case Constant.IDENTITY_TEACHER:
                return "教师";
            case Constant.IDENTITY_ADMINISTRATOR:
                return "管理员";
            default:
                return "未知";
        }
    }

    public static String getEnableString(boolean enable) {
        return enable ? "已启用" : "未启用";
    }

    public static String getSexString(int sex){
        return sex == Constant.SEX_MALE ? "男" : "女";
    }
}
