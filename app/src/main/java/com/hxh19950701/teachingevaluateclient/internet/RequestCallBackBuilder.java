package com.hxh19950701.teachingevaluateclient.internet;

import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.lang.reflect.Type;

public class RequestCallBackBuilder<Data> extends RequestCallBack<String> {

    public static final String TAG = RequestCallBackBuilder.class.getSimpleName();

    private ServiceCallback<Data> serviceCallback;
    private Type type;

    public RequestCallBackBuilder(Type type, ServiceCallback<Data> serviceCallback) {
        this.type = type;
        this.serviceCallback = serviceCallback;
    }

    @Override
    public void onStart() {
        if (serviceCallback != null) {
            serviceCallback.onStart();
        }
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        NetClient.saveCookie(responseInfo);
        if (serviceCallback != null) {
            DataFormatThread<Data> dataFormatThread = new DataFormatThread<Data>(responseInfo.result, type);
            dataFormatThread.setOnFormattingListener(new OnFormattingListenerBuilder(serviceCallback));
            dataFormatThread.start();
        }
    }

    @Override
    public void onFailure(HttpException e, String s) {
        Log.e(TAG, e.getMessage(), e);
        if (serviceCallback != null) {
            serviceCallback.onFailure(e, s);
            serviceCallback.onAfter();
        }
    }
}
