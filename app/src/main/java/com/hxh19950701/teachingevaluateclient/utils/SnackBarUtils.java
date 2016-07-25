package com.hxh19950701.teachingevaluateclient.utils;

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

}