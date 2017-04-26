package com.hxh19950701.teachingevaluateclient.utils;

public class FilterUserUtils {

    private FilterUserUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static boolean isQualifiedUsername(String username, String key) {
        return key.isEmpty() || username.contains(key);
    }

    public static boolean isQualifiedIdentity(int identity, int condition) {
        return condition == -1 || identity == condition;
    }

    public static boolean isQualifiedEnable(boolean enable, int condition) {
        return condition == -1 || (enable ? 1 : 0) == condition;
    }
}