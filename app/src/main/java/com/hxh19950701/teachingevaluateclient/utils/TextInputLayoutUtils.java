package com.hxh19950701.teachingevaluateclient.utils;

import android.support.design.widget.TextInputLayout;

public class TextInputLayoutUtils {

    private TextInputLayoutUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static void setErrorEnabled(TextInputLayout textInputLayout, boolean enable) {
        if (!enable) {
            textInputLayout.setError(null);
        }
        textInputLayout.setErrorEnabled(enable);
    }
}