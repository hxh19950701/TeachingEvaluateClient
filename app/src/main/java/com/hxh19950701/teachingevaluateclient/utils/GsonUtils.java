package com.hxh19950701.teachingevaluateclient.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtils {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private GsonUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return GSON.fromJson(json, typeOfT);
    }

}