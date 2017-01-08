package com.hxh19950701.teachingevaluateclient.utils;

import android.content.Context;
import android.content.Intent;

public class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    /**
     * 跳转Activity
     *
     * @param context 当前上下文
     * @param cls     目标Activity的字节码
     */
    public static void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, -1);
    }

    /**
     * 跳转Activity
     *
     * @param context 当前上下文
     * @param cls     目标Activity的字节码
     * @param flags   需要附加的flag
     */
    public static void startActivity(Context context, Class<?> cls, int flags) {
        Intent intent = new Intent(context, cls);
        if (flags > -1) {
            intent.setFlags(flags);
        }
        context.startActivity(intent);
    }

}  