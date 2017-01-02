package com.hxh19950701.teachingevaluateclient.internet;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.Header;

import java.lang.reflect.Type;

public class NetClient {

    private NetClient() {
        throw new UnsupportedOperationException();
    }

    private static final String TAG = NetClient.class.getSimpleName();

    public static <Data> HttpHandler<String> sendRequest(
            HttpRequest.HttpMethod httpMethod, String url, RequestParams requestParams,
            final ServiceCallback<Data> serviceCallback, final Type type) {
        HttpUtils httpUtils = new HttpUtils();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e(TAG, responseInfo.result);
                saveCookie(responseInfo);
                if (serviceCallback != null) {
                    serviceCallback.onAfter();
                    ResponseData<Data> data = null;
                    try {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                        data = gson.fromJson(responseInfo.result, type);
                    } catch (Exception e) {
                        e.printStackTrace();
                        serviceCallback.onException(responseInfo.result);
                    }
                    serviceCallback.onSuccess(data);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Log.e(TAG, s);
                if (serviceCallback != null) {
                    serviceCallback.onAfter();
                    serviceCallback.onFailure(e, s);
                }
            }
        };
        return httpUtils.send(httpMethod, url, requestParams, callBack);
    }

    public static <Data> HttpHandler<String> sendGetRequest(String url, RequestParams requestParams, final ServiceCallback<Data> serviceCallback, Type type) {
        return sendRequest(HttpRequest.HttpMethod.GET, url, requestParams, serviceCallback, type);
    }

    public static <Data> HttpHandler<String> sendPostRequest(String url, RequestParams requestParams, final ServiceCallback<Data> serviceCallback, Type type) {
        return sendRequest(HttpRequest.HttpMethod.POST, url, requestParams, serviceCallback, type);
    }

    public static RequestParams buildRequestParams(String action, String... args) {
        assert (args.length % 2 == 0);
        RequestParams requestParams = new RequestParams();
        String cookie = PrefUtils.getString(Constant.KEY_COOKIE, "");
        requestParams.addHeader(Constant.KEY_COOKIE, cookie);
        requestParams.addBodyParameter(Constant.KEY_ACTION, action);
        for (int i = 0; i < args.length; i += 2) {
            Log.d(TAG, args[i] + " = [" + args[i + 1] + "].");
            requestParams.addBodyParameter(args[i], args[i + 1]);
        }
        return requestParams;
    }

    public static final void saveCookie(ResponseInfo<String> responseInfo) {
        Header[] headers = responseInfo.getHeaders(Constant.KEY_SET_COOKIE);
        if (headers != null && headers.length > 0) {
            String str = headers[0].getValue();
            String cookie = str.substring(0, str.lastIndexOf(' ') - 1);
            if (!PrefUtils.getString(Constant.KEY_COOKIE, " ").equals(cookie)) {
                PrefUtils.putString(Constant.KEY_COOKIE, cookie);
            }
        }
    }
}
