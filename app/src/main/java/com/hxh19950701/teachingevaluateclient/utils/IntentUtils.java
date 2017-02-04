package com.hxh19950701.teachingevaluateclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void startActivity(Context context, Class<? extends Activity> cls) {
        startActivity(context, cls, -1);
    }

    public static void startActivity(Context context, Class<? extends Activity> cls, int flags) {
        Intent intent = new Intent(context, cls);
        if (flags > -1) {
            intent.setFlags(flags);
        }
        context.startActivity(intent);
    }

    public static void startService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startService(intent);
    }

    public static void stopService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.stopService(intent);
    }
}