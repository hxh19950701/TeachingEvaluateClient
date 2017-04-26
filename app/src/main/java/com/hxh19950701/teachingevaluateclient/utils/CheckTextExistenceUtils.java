package com.hxh19950701.teachingevaluateclient.utils;

import android.support.design.widget.TextInputLayout;

import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;

import java.util.HashMap;
import java.util.Map;

public class CheckTextExistenceUtils {

    public static CheckTextExistenceUtils newInstance(TextInputLayout layout,
                                                      CharSequence checkingText,
                                                      CharSequence existingText,
                                                      CharSequence unknownText,
                                                      CheckListener listener) {
        return new CheckTextExistenceUtils(layout, checkingText, existingText, unknownText, listener);
    }

    private final TextInputLayout layout;
    private final CharSequence checkingText;
    private final CharSequence existingText;
    private final CharSequence unknownText;
    private final CheckListener listener;
    private final CheckServiceCallback callback;

    private HttpHandler<String> httpHandler = null;
    private final Map<CharSequence, Boolean> cache = new HashMap<>(20);

    public CheckTextExistenceUtils(TextInputLayout layout,
                                   CharSequence checkingText,
                                   CharSequence existingText,
                                   CharSequence unknownText,
                                   CheckListener listener) {
        this.layout = layout;
        this.checkingText = checkingText;
        this.existingText = existingText;
        this.unknownText = unknownText;
        this.listener = listener;
        this.callback = new CheckServiceCallback();
    }

    public void abortCurrentCheck() {
        if (httpHandler != null && !httpHandler.isCancelled()) {
            httpHandler.cancel();
        }
    }

    public void checkExistence() {
        CharSequence text = layout.getEditText().getText();
        Boolean existence = cache.get(text);
        if (existence != null) {
            setupExistence(existence.booleanValue());
        } else {
            callback.text = text;
            listener.onCheckFromServer(callback);
        }
    }

    private void setupExistence(boolean existence) {
        if (existence) {
            layout.setError(existingText);
        } else {
            TextInputLayoutUtils.setErrorEnabled(layout, false);
        }
        listener.onComplete();
    }

    public interface CheckListener {
        void onCheckFromServer(CheckServiceCallback callback);

        void onComplete();
    }

    public class CheckServiceCallback extends ServiceCallback<Boolean> {

        private CharSequence text;

        public CharSequence getText() {
            return text;
        }

        @Override
        public void onStart() {
            super.onStart();
            layout.setError(checkingText);
        }

        @Override
        public void onAfter() {
            listener.onComplete();
        }

        @Override
        public void onSuccess(ResponseData<Boolean> data) {
            Boolean existence = data.getData();
            cache.put(text, existence);
            setupExistence(existence != null && existence);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            super.onFailure(e, s);
            layout.setError("我们无法检测该用户名是否可用。");
        }

        @Override
        public void onJsonSyntaxException(String s) {
            super.onJsonSyntaxException(s);
            layout.setError("我们无法检测该用户名是否可用。");
        }
    }
}
