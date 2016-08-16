package com.hxh19950701.teachingevaluateclient.utils;

import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by hxh19950701 on 2016/5/31.
 */
public class SnackBarUtils {
    private SnackBarUtils() {

    }

    public static void showLong(View container, CharSequence msg) {
        final Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showLongPost(final View container, final CharSequence msg) {
        showLongPost(container, msg, 500);
    }

    public static void showLongPost(final View container, final CharSequence msg, final long sleepTime) {
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(sleepTime);
                final Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }.start();
    }
}