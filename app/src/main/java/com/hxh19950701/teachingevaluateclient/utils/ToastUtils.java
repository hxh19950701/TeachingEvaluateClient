package com.hxh19950701.teachingevaluateclient.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtils {

    private ToastUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static Context context = null;

    public static void init(Context context) {
        ToastUtils.context = context;
    }

    public static void show(CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int resId) {
        show(context.getResources().getString(resId));
    }

    public static void show(CharSequence msg, int duration, int gravity, int xOffset, int yOffset) {
        Toast toast = new Toast(context);
        toast.setText(msg);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.show();
    }

    public static void show(@StringRes int resId, int duration, int gravity, int xOffset, int yOffset) {
        show(context.getResources().getString(resId), duration, gravity, xOffset, yOffset);
    }

    public static void showLong(CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(@StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

}