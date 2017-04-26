package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.event.UserRegisterCompleteEvent;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.CheckTextExistenceUtils;
import com.hxh19950701.teachingevaluateclient.utils.InputMethodUtils;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.TextInputLayoutUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RegisterUserActivity extends BaseActivity {

    @BindView(R.id.clRegister)
    /*package*/ CoordinatorLayout clRegister;
    @BindView(R.id.tilUsername)
    /*package*/ TextInputLayout tilUsername;
    @BindView(R.id.tilPassword)
    /*package*/ TextInputLayout tilPassword;
    @BindView(R.id.tilPasswordRetype)
    /*package*/ TextInputLayout tilPasswordRetype;
    @BindView(R.id.btnRegister)
    /*package*/ Button btnRegister;

    private CheckTextExistenceUtils existenceUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_user;
    }

    @Override
    protected void initListener() {
        existenceUtils = new CheckTextExistenceUtils(
                tilUsername,
                "正在检测该用户名是否可用，请稍后……",
                getText(R.string.usernameInUse),
                "我们无法检测该用户名是否可用。",
                new CheckTextExistenceUtils.CheckListener() {
                    @Override
                    public void onCheckFromServer(CheckTextExistenceUtils.CheckServiceCallback callback) {
                        UserApi.hasExist(callback.getText().toString(), callback);
                    }

                    @Override
                    public void onComplete() {
                        refreshOperationEnable();
                    }
                });
    }

    @Override
    public void initData() {
        displayHomeAsUp();

        tilPasswordRetype.getEditText().setEnabled(false);
        InputMethodUtils.showForced();
    }

    @OnTextChanged(value = R.id.etUsername)
    public void checkUsername() {
        existenceUtils.abortCurrentCheck();
        String username = tilUsername.getEditText().getText().toString();
        if (username.isEmpty()) {
            TextInputLayoutUtils.setErrorEnabled(tilUsername, false);
            refreshOperationEnable();
        } else if (username.length() < 6) {
            tilUsername.setError("用户名由6~16个数字和字母组成。");
            refreshOperationEnable();
        } else {
            existenceUtils.checkExistence();
        }
    }

    @OnTextChanged(value = R.id.etPassword)
    public void checkPassword() {
        String password = tilPassword.getEditText().getText().toString();
        if (password.isEmpty()) {
            tilPasswordRetype.getEditText().setEnabled(false);
            TextInputLayoutUtils.setErrorEnabled(tilPassword, false);
        } else if (password.length() < 6) {
            tilPasswordRetype.getEditText().setEnabled(false);
            tilPassword.setError("密码由6~16个数字，字母和符号组成。");
        } else {
            tilPasswordRetype.getEditText().setEnabled(true);
            TextInputLayoutUtils.setErrorEnabled(tilPassword, false);
        }
        tilPasswordRetype.getEditText().setText("");
        refreshOperationEnable();
    }

    @OnTextChanged(value = R.id.etPasswordRetype)
    public void checkPasswordRetype() {
        String password = tilPassword.getEditText().getText().toString();
        String passwordRetype = tilPasswordRetype.getEditText().getText().toString();
        if (password.startsWith(passwordRetype)) {
            TextInputLayoutUtils.setErrorEnabled(tilPasswordRetype, false);
        } else {
            tilPasswordRetype.setError(getText(R.string.passwordInconsistent));
        }
        refreshOperationEnable();
    }

    @OnClick(R.id.btnRegister)
    public void register() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在注册").content("请稍后...")
                .cancelable(false).progress(true, 0).build();
        String username = tilUsername.getEditText().getText().toString();
        String password = MD5Utils.encipher(tilPassword.getEditText().getText().toString());
        UserApi.register(username, password, Constant.IDENTITY_STUDENT, new SimpleServiceCallback<User>(clRegister, dialog) {
            @Override
            public void onGetDataSuccessful(User user) {
                EventManager.postEvent(new UserRegisterCompleteEvent(username, password));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InputMethodUtils.hideSoftInputFromWindow(clRegister.getWindowToken());
    }

    private void refreshOperationEnable() {
        String password = tilPassword.getEditText().getText().toString();
        String passwordRetype = tilPasswordRetype.getEditText().getText().toString();
        btnRegister.setEnabled(TextInputLayoutUtils.isInputComplete(tilUsername)
                && TextInputLayoutUtils.isInputComplete(tilPassword)
                && TextInputLayoutUtils.isInputComplete(tilPasswordRetype)
                && password.equals(passwordRetype));
    }
}