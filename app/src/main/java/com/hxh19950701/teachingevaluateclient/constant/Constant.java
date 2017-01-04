package com.hxh19950701.teachingevaluateclient.constant;

public class Constant {
    private Constant() {
        throw new UnsupportedOperationException();
    }

    public static final int ITEM_COUNT = 10;

    public static final int IDENTITY_STUDENT = 0;
    public static final int IDENTITY_TEACHER = 1;
    public static final int IDENTITY_ADMINISTRATOR = 2;

    public static final int TERM_FIRST = 1;
    public static final int TERM_SECOND = 2;

    public static final int SEX_MALE = 1;
    public static final int SEX_FEMALE = 0;

    public static final int ERROR_UNKNOWN = 0;
    public static final int ERROR_NO_SUCH_USERNAME = -101;
    public static final int ERROR_INVALID_USERNAME = -103;
    public static final int ERROR_INVALID_PASSWORD = -104;
    public static final int ERROR_INCORRECT_PASSWORD = -102;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_AUTO_LOGIN = "auto_login";
    public static final String KEY_REMEMBER_PASSWORD = "remember_password";
    public static final String KEY_COOKIE = "cookie";
    public static final String KEY_SET_COOKIE = "set-cookie";
    public static final String KEY_ACTION = "action";
    public static final String KEY_SERVER_DOMAIN = "server_domain";

    public static final String PREFIX_SERVER_DOMAIN = "http://";
    public static final String DEFAULT_SERVER_DOMAIN = "http://192.168.191.1:8080/TeachingEvaluateServer";
}