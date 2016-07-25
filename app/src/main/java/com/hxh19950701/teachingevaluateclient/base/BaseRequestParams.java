package com.hxh19950701.teachingevaluateclient.base;

import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.lidroid.xutils.http.RequestParams;

/**
 * Created by hxh19950701 on 2016/7/2.
 */
public class BaseRequestParams extends RequestParams {
    public BaseRequestParams(){
        super();
        addHeader("cookie", PrefUtils.getString("cookie", ""));
    }
}
