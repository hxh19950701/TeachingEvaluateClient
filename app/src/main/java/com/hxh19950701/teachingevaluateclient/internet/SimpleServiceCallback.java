package com.hxh19950701.teachingevaluateclient.internet;

import android.view.View;

import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.exception.HttpException;

import java.net.HttpURLConnection;

public abstract class SimpleServiceCallback<Data> extends ServiceCallback<Data> {

    private View container;

    public SimpleServiceCallback(View container) {
        this.container = container;
    }

    @Override
    public void onSuccess(ResponseData<Data> data) {
        if (data.getCode() == HttpURLConnection.HTTP_OK) {
            onGetDataSuccess(data.getData());
        } else {
            onGetDataFailure(data.getCode(), data.getMsg());
        }
    }

    public abstract void onGetDataSuccess(Data data);

    public void onGetDataFailure(int code, String msg) {
        SnackBarUtils.showLong(container, msg);
    }

    @Override
    public void onFailure(HttpException e, String s) {
        SnackBarUtils.showConnectFailure(container, e.getExceptionCode());
    }

    @Override
    public void onException(String s) {
        SnackBarUtils.showSystemError(container);
    }
}
