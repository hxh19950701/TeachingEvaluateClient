package com.hxh19950701.teachingevaluateclient.base;

import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.apache.http.Header;

/**
 * Created by hxh19950701 on 2016/6/1.
 */
public class BaseRequestCallBack<T> extends RequestCallBack<T> {

    @Override
    public void onSuccess(ResponseInfo<T> responseInfo) {
        System.out.println(responseInfo.result);
        Header[] headers = responseInfo.getHeaders("set-cookie");
        if (headers != null && headers.length > 0) {
            String str = headers[0].getValue();
            String cookie = str.substring(0, str.lastIndexOf(' ') - 1);
            if (!PrefUtils.getString("cookie", " ").equals(cookie)) {
                PrefUtils.putString("cookie", cookie);
            }
        }
    }

    @Override
    public void onFailure(HttpException e, String s) {
        e.printStackTrace();
        System.out.println(s);
    }
}
