package com.hxh19950701.teachingevaluateclient.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtils {

    private DisplayUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static final DisplayMetrics DM = new DisplayMetrics();

    public static void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(DM);
    }

    public static int dpToPx(float dipValue) {
        return (int) (dipValue * DM.density + 0.5);
    }

    public static float pxToDp(float pxValue) {
        return pxValue / DM.density;
    }

    public static int getScreenHeight() {
        return DM.heightPixels;
    }

    public static int getScreenWidth() {
        return DM.widthPixels;
    }
}