package com.hxh19950701.teachingevaluateclient.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("unused")
public class ToastUtils {

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Duration {}

    private ToastUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static Toast toast = null;
    private static DefaultSetting defaultSetting = null;

    @SuppressLint("ShowToast")
    public static void init(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        defaultSetting = new DefaultSetting(
                toast.getDuration(), toast.getGravity(), toast.getXOffset(), toast.getYOffset());
    }

    public static void show(CharSequence msg) {
        show(msg, defaultSetting.duration, defaultSetting.gravity, defaultSetting.xOffset, defaultSetting.yOffset);
    }

    public static void showLong(CharSequence msg) {
        show(msg, Toast.LENGTH_LONG, defaultSetting.gravity, defaultSetting.xOffset, defaultSetting.yOffset);
    }

    public static void show(CharSequence msg, @Duration int duration, int gravity, int xOffset, int yOffset) {
        if (toast == null) {
            throw new IllegalStateException("You must initialize this util before you use it.");
        }
        toast.setText(msg);
        toast.setDuration(duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }

    public static void show(@StringRes int resId) {
        show(resId, defaultSetting.duration, defaultSetting.gravity, defaultSetting.xOffset, defaultSetting.yOffset);
    }

    public static void showLong(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG, defaultSetting.gravity, defaultSetting.xOffset, defaultSetting.yOffset);
    }

    public static void show(@StringRes int resId, @Duration int duration, int gravity, int xOffset, int yOffset) {
        if (toast == null) {
            throw new IllegalStateException("You must initialize this util before you use it.");
        }
        toast.setText(resId);
        toast.setDuration(duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }

    private static class DefaultSetting {
        private int duration;
        private int gravity;
        private int xOffset;
        private int yOffset;

        /*package*/ DefaultSetting(int duration, int gravity, int xOffset, int yOffset) {
            this.duration = duration;
            this.gravity = gravity;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
    }
}