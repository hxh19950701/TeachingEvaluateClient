package com.hxh19950701.teachingevaluateclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.activity.AdministratorMainUiActivity;
import com.hxh19950701.teachingevaluateclient.activity.LoginActivity;
import com.hxh19950701.teachingevaluateclient.activity.StudentMainUiActivity;
import com.hxh19950701.teachingevaluateclient.activity.TeacherMainUiActivity;
import com.hxh19950701.teachingevaluateclient.common.Constant;

public class ActivityUtils {

    private static final Class[] IDENTITY_ACTIVITY = new Class[Constant.IDENTITY_COUNT];

    static {
        IDENTITY_ACTIVITY[Constant.IDENTITY_STUDENT] = StudentMainUiActivity.class;
        IDENTITY_ACTIVITY[Constant.IDENTITY_TEACHER] = TeacherMainUiActivity.class;
        IDENTITY_ACTIVITY[Constant.IDENTITY_ADMINISTRATOR] = AdministratorMainUiActivity.class;
    }

    private ActivityUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void enterApp(Context context, int identity) {
        switch (identity) {
            case Constant.IDENTITY_STUDENT:
            case Constant.IDENTITY_TEACHER:
            case Constant.IDENTITY_ADMINISTRATOR:
                IntentUtils.startActivity(context, IDENTITY_ACTIVITY[identity], Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            default:
                throw new IllegalArgumentException("Unknown identity code : " + identity);
        }
    }

    public static void exitApp(Activity activity, String msg) {
        PrefUtils.putBoolean(Constant.KEY_AUTO_LOGIN, false);
        activity.startActivity(LoginActivity.newIntent(activity, msg).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
