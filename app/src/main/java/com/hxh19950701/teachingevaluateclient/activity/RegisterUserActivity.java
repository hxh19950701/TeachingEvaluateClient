package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.User;
import com.hxh19950701.teachingevaluateclient.event.UserRegisterCompleteEvent;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.InputMethodUtils;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.TextInputLayoutUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends BaseActivity {

    private CoordinatorLayout clRegister;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextInputLayout tilPasswordRetype;
    private Button btnRegister;

    private HttpHandler<String> httpHandler = null;
    private Map<String, Boolean> existence = new HashMap<>(20);

    private final TextWatcher USERNAME_WATCHER = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            String username = s.toString();
            if (httpHandler != null) {
                httpHandler.cancel();
            }
            if (username.isEmpty()) {
                TextInputLayoutUtils.setErrorEnabled(tilUsername, false);
                refreshOperationEnable();
            } else if (username.length() < 6) {
                tilUsername.setError("用户名由6~16个数字和字母组成。");
                refreshOperationEnable();
            } else {
                Boolean exist = existence.get(username);
                if (exist == null) {
                    tilUsername.setError("正在检测该用户名是否可用，请稍后……");
                    checkUsernameExistence();
                } else {
                    setupUsernameExistence(exist);
                }
            }
        }
    };

    private final TextWatcher PASSWORD_WATCHER = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            if (tilPassword.getEditText().getText().toString().isEmpty()) {
                tilPasswordRetype.getEditText().setEnabled(false);
                TextInputLayoutUtils.setErrorEnabled(tilPassword, false);
            } else if (s.length() < 6) {
                tilPasswordRetype.getEditText().setEnabled(false);
                tilPassword.setError("密码由6~16个数字，字母和符号组成。");
            } else {
                tilPasswordRetype.getEditText().setEnabled(true);
                TextInputLayoutUtils.setErrorEnabled(tilPassword, false);
            }
            tilPasswordRetype.getEditText().setText("");
            refreshOperationEnable();
        }
    };

    private final TextWatcher PASSWORD_RETYPE_WATCHER = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            String password = tilPassword.getEditText().getText().toString();
            if (password.startsWith(s.toString())) {
                TextInputLayoutUtils.setErrorEnabled(tilPasswordRetype, false);
            } else {
                tilPasswordRetype.setError(getText(R.string.passwordInconsistent));
            }
            refreshOperationEnable();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_register_user);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        clRegister = (CoordinatorLayout) findViewById(R.id.clRegister);
        tilUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        tilPasswordRetype = (TextInputLayout) findViewById(R.id.tilPasswordRetype);
    }

    @Override
    public void initListener() {
        btnRegister.setOnClickListener(this);
        tilUsername.getEditText().addTextChangedListener(USERNAME_WATCHER);
        tilPassword.getEditText().addTextChangedListener(PASSWORD_WATCHER);
        tilPasswordRetype.getEditText().addTextChangedListener(PASSWORD_RETYPE_WATCHER);
    }

    @Override
    public void initData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tilPasswordRetype.getEditText().setEnabled(false);
        refreshOperationEnable();
        //弹出键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                register();
        }
    }

    private void setupUsernameExistence(boolean isExist) {
        if (isExist) {
            CharSequence errorText = getResources().getText(R.string.usernameInUse);
            tilUsername.setError(errorText);
        } else {
            TextInputLayoutUtils.setErrorEnabled(tilUsername, false);
        }
    }

    protected void checkUsernameExistence() {
        final String username = tilUsername.getEditText().getText().toString();
        httpHandler = UserApi.hasExist(username, new SimpleServiceCallback<Boolean>(clRegister) {

            @Override
            public void onAfter() {
                refreshOperationEnable();
            }

            @Override
            public void onGetDataSuccess(Boolean isExist) {
                existence.put(username, isExist);
                setupUsernameExistence(isExist);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                tilUsername.setError("我们无法检测该用户名是否可用。");
            }

            @Override
            public void onGetDataFailure(int code, String msg) {
                tilUsername.setError("我们无法检测该用户名是否可用。");
            }

            @Override
            public void onException(String s) {
                tilUsername.setError("我们无法检测该用户名是否可用。");
            }
        });
    }

    protected void register() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this).title("正在注册").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).build();
        final String username = tilUsername.getEditText().getText().toString();
        final String password = MD5Utils.encipher(tilPassword.getEditText().getText().toString());

        UserApi.registerStudent(username, password, new SimpleServiceCallback<User>(clRegister) {

            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onAfter() {
                dialog.dismiss();
            }

            @Override
            public void onGetDataSuccess(User user) {
                UserRegisterCompleteEvent event = new UserRegisterCompleteEvent(username, password);
                EventBus.getDefault().post(event);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InputMethodUtils.hideSoftInputFromWindow(clRegister.getWindowToken());
    }

    private static boolean isInputComplete(TextInputLayout textInputLayout) {
        boolean isInputted = !TextUtils.isEmpty(textInputLayout.getEditText().getText());
        boolean isError = !TextUtils.isEmpty(textInputLayout.getError());
        boolean isEnable = textInputLayout.getEditText().isEnabled();
        return isInputted && !isError && isEnable;
    }

    private void refreshOperationEnable() {
        final String password = tilUsername.getEditText().getText().toString();
        final String passwordRetype = tilPasswordRetype.getEditText().getText().toString();
        btnRegister.setEnabled(isInputComplete(tilUsername) && isInputComplete(tilPassword)
                && isInputComplete(tilPasswordRetype) && password.equals(passwordRetype));
    }
}