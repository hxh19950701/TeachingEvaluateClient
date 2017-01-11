package com.hxh19950701.teachingevaluateclient.utils;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

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

    public static boolean isInputComplete(TextInputLayout textInputLayout) {
        boolean isInputted = !TextUtils.isEmpty(textInputLayout.getEditText().getText());
        boolean isError = !TextUtils.isEmpty(textInputLayout.getError());
        boolean isEnable = textInputLayout.getEditText().isEnabled();
        return isInputted && !isError && isEnable;
    }
}