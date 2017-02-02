package com.hxh19950701.teachingevaluateclient.network;

import com.google.gson.JsonParseException;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.utils.GsonUtils;

import java.lang.reflect.Type;

public class DataFormatThread<Data> extends Thread {

    public static interface OnFormattingListener<Data> {
        void onStart(String jsonString, Type type);
        void onException(String jsonString, Type type, JsonParseException e);
        void onComplete(ResponseData<Data> data);
    }

    private static final String TAG = DataFormatThread.class.getSimpleName();

    private String jsonString;
    private Type type;
    private OnFormattingListener onFormattingListener;

    public DataFormatThread(String jsonString, Type type) {
        this.jsonString = jsonString;
        this.type = type;
    }

    public OnFormattingListener getOnFormattingListener() {
        return onFormattingListener;
    }

    public void setOnFormattingListener(OnFormattingListener onFormattingListener) {
        this.onFormattingListener = onFormattingListener;
    }

    @Override
    public void run() {
        onStart();
        try {
            ResponseData<Data> data = GsonUtils.fromJson(jsonString, type);
            onComplete(data);
        } catch (JsonParseException e) {
            onException(e);
        }
    }

    private void onStart() {
        if (onFormattingListener != null) {
            onFormattingListener.onStart(jsonString, type);
        }
    }

    private void onComplete(final ResponseData<Data> data) {
        if (onFormattingListener != null) {
            onFormattingListener.onComplete(data);
        }
    }

    private void onException(JsonParseException e) {
        if (onFormattingListener != null) {
            onFormattingListener.onException(jsonString, type, e);
        }
    }
}