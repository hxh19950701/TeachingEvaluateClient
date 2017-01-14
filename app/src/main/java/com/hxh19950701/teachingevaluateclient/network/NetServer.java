package com.hxh19950701.teachingevaluateclient.network;

import android.app.Activity;
import android.content.Intent;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.activity.LoginActivity;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;

/**
 * Created by hxh19950701 on 2016/6/1.
 */
public class NetServer {

    private NetServer(){

    }


    public static void requireLoginAgain(Activity activity, String msg) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("msg", msg);
        PrefUtils.putBoolean("AutoLogin", false);
        if (activity != null) {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            activity.finish();
        }
    }

}