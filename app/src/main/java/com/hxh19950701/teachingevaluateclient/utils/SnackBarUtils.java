package com.hxh19950701.teachingevaluateclient.utils;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;

/**
 * Created by hxh19950701 on 2016/5/31.
 */
public class SnackBarUtils {
    private SnackBarUtils() {
        throw new UnsupportedOperationException();
    }

    public static void showLong(View container, CharSequence msg) {
        final Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showLong(View container, @StringRes int msg) {
        final Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showLongPost(View container, CharSequence msg) {
        showLongPost(container, msg, 500);
    }

    public static void showLongPost(View container, @StringRes int msg) {
        showLongPost(container, msg, 500);
    }

    public static void showLongPost(final View container, final CharSequence msg, final long sleepTime) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(sleepTime);
                showLong(container, msg);
            }
        }.start();
    }

    public static void showLongPost(final View container, @StringRes final int msg, final long sleepTime) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(sleepTime);
                showLong(container, msg);
            }
        }.start();
    }

    public static void showConnectFailure(View container, int code) {
        SnackBarUtils.showLong(container, container.getContext().getString(R.string.connectServerFail, code));
    }

    public static void showSystemError(View container) {
        SnackBarUtils.showLong(container, container.getContext().getString(R.string.systemError));
    }
}