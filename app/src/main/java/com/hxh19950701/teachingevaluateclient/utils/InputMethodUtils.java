package com.hxh19950701.teachingevaluateclient.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtils {
    private InputMethodUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static InputMethodManager inputMethodManager = null;

    public static void init(Context context) {
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static boolean isActive() {
        return inputMethodManager.isActive();
    }

    public static void showForced() {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(View view) {
        inputMethodManager.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
    }

    public static void hideSoftInputFromWindow(IBinder token) {
        inputMethodManager.hideSoftInputFromWindow(token, 0);
    }
}