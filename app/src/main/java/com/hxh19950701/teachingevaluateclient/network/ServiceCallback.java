package com.hxh19950701.teachingevaluateclient.network;

import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.lidroid.xutils.exception.HttpException;

public abstract class ServiceCallback<Data> {

    public void onStart() {

    }

    public abstract void onSuccess(ResponseData<Data> data);

    public void onFailure(HttpException e, String s) {
    }

    public void onJsonSyntaxException(String s) {
    }

    public void onAfter() {

    }
}