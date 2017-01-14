package com.hxh19950701.teachingevaluateclient.network;

import android.os.Handler;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;

import java.lang.reflect.Type;

public class OnFormattingListenerBuilder<Data> implements DataFormatThread.OnFormattingListener<Data> {

    private static final String TAG = OnFormattingListenerBuilder.class.getSimpleName();

    private Handler handler = new Handler();
    private ServiceCallback<Data> serviceCallback;

    public OnFormattingListenerBuilder(ServiceCallback<Data> serviceCallback) {
        this.serviceCallback = serviceCallback;
    }

    @Override
    public void onStart(String jsonString, Type type) {
        Log.i(TAG, jsonString);
    }

    @Override
    public void onException(final String jsonString, Type type, JsonParseException e) {
        Log.e(TAG, e.getMessage(), e);
        handler.post(new Runnable() {
            @Override
            public void run() {
                serviceCallback.onException(jsonString);
                serviceCallback.onAfter();
            }
        });
    }

    @Override
    public void onComplete(final ResponseData<Data> data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                serviceCallback.onSuccess(data);
                serviceCallback.onAfter();
            }
        });

    }
}