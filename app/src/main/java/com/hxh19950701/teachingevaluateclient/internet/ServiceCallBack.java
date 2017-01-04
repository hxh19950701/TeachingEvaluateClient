package com.hxh19950701.teachingevaluateclient.internet;

import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.lidroid.xutils.exception.HttpException;

public abstract class ServiceCallback<Data> {

    public void onStart() {

    }

    public abstract void onSuccess(ResponseData<Data> bean);

    public void onFailure(HttpException e, String s) {
    }

    public void onException(String s) {
    }

    public void onAfter() {

    }
}