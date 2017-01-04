package com.hxh19950701.teachingevaluateclient.internet;

import android.util.Log;

import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.HttpUtils;
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
    private static final HttpUtils HTTP_UTILS = new HttpUtils();

    public static <Data> HttpHandler<String> sendRequest(
            HttpRequest.HttpMethod httpMethod, String url, final RequestParams requestParams,
            final ServiceCallback<Data> serviceCallback, final Type type) {
        RequestCallBack<String> callBack = new RequestCallBackBuilder<Data>(type, serviceCallback);
        return HTTP_UTILS.send(httpMethod, url, requestParams, callBack);
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
            String newCookie = str.substring(0, str.lastIndexOf(' ') - 1);
            String currentCookie = PrefUtils.getString(Constant.KEY_COOKIE, "");
            if (!newCookie.equals(currentCookie)) {
                PrefUtils.putString(Constant.KEY_COOKIE, newCookie);
            }
        }
    }
}
