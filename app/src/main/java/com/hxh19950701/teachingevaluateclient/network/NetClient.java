package com.hxh19950701.teachingevaluateclient.network;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.Header;

@SuppressWarnings({"unused", "WeakerAccess"})
public class NetClient {

    private NetClient() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static final String TAG = NetClient.class.getSimpleName();
    private static final HttpUtils HTTP_UTILS = new HttpUtils();

    public static <Data> HttpHandler<String> sendRequest(HttpRequest.HttpMethod httpMethod,
                                                         String url,
                                                         RequestParams requestParams,
                                                         ServiceCallback<Data> serviceCallback,
                                                         TypeToken<ResponseData<Data>> typeToken) {
        RequestCallBack<String> callBack = new RequestCallBackBuilder<>(typeToken.getType(), serviceCallback);
        return HTTP_UTILS.send(httpMethod, url, requestParams, callBack);
    }

    public static ResponseStream sendRequestSync(HttpRequest.HttpMethod httpMethod, String url, RequestParams requestParams) throws HttpException {
        return HTTP_UTILS.sendSync(httpMethod, url, requestParams);
    }

    public static <Data> HttpHandler<String> sendGetRequest(String url,
                                                            RequestParams requestParams,
                                                            final ServiceCallback<Data> serviceCallback,
                                                            TypeToken<ResponseData<Data>> typeToken) {
        return sendRequest(HttpRequest.HttpMethod.GET, url, requestParams, serviceCallback, typeToken);
    }

    public static <Data> HttpHandler<String> sendPostRequest(String url,
                                                             RequestParams requestParams,
                                                             ServiceCallback<Data> serviceCallback,
                                                             TypeToken<ResponseData<Data>> typeToken) {
        return sendRequest(HttpRequest.HttpMethod.POST, url, requestParams, serviceCallback, typeToken);
    }

    public static ResponseStream sendGetRequestSync(String url, RequestParams requestParams) throws HttpException {
        return sendRequestSync(HttpRequest.HttpMethod.GET, url, requestParams);
    }

    public static ResponseStream sendPostRequestSync(String url, RequestParams requestParams) throws HttpException {
        return sendRequestSync(HttpRequest.HttpMethod.POST, url, requestParams);
    }

    @SuppressLint("Assert")
    public static RequestParams buildRequestParams(String action, String... args) {
        assert (args.length % 2 == 0);
        RequestParams requestParams = new RequestParams();
        String cookie = PrefUtils.getString(Constant.KEY_COOKIE, "");
        requestParams.addHeader(Constant.KEY_COOKIE, cookie);
        requestParams.addBodyParameter(Constant.KEY_ACTION, action);
        Log.d(TAG, "action = [" + action + "].");
        for (int i = 0; i < args.length; i += 2) {
            Log.d(TAG, args[i] + " = [" + args[i + 1] + "].");
            requestParams.addBodyParameter(args[i], args[i + 1]);
        }
        return requestParams;
    }

    public static void saveCookie(ResponseInfo<String> responseInfo) {
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
