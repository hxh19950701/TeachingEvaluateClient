package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.User;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.TextInputLayoutUtils;

public class ModifyPasswordActivity extends BaseActivity {

    private CoordinatorLayout clModifyPassword;
    private TextInputLayout tilCurrentPassword;
    private TextInputLayout tilNewPassword;
    private TextInputLayout tilNewPasswordRetype;
    private Button btnModify;

    private final TextWatcher PASSWORD_WATCHER = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            if (tilNewPassword.getEditText().getText().toString().isEmpty()) {
                tilNewPasswordRetype.getEditText().setEnabled(false);
                TextInputLayoutUtils.setErrorEnabled(tilNewPassword, false);
            } else if (s.length() < 6) {
                tilNewPasswordRetype.getEditText().setEnabled(false);
                tilNewPassword.setError("密码由6~16个数字，字母和符号组成。");
            } else {
                tilNewPasswordRetype.getEditText().setEnabled(true);
                TextInputLayoutUtils.setErrorEnabled(tilNewPassword, false);
            }
            tilNewPasswordRetype.getEditText().setText("");
            refreshOperationEnable();
        }
    };

    private final TextWatcher PASSWORD_RETYPE_WATCHER = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            String password = tilNewPassword.getEditText().getText().toString();
            if (password.startsWith(s.toString())) {
                TextInputLayoutUtils.setErrorEnabled(tilNewPasswordRetype, false);
            } else {
                tilNewPasswordRetype.setError(getText(R.string.passwordInconsistent));
            }
            refreshOperationEnable();
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_modify_password);
        clModifyPassword = (CoordinatorLayout) findViewById(R.id.clModifyPassword);
        tilCurrentPassword = (TextInputLayout) findViewById(R.id.tilCurrentPassword);
        tilNewPassword = (TextInputLayout) findViewById(R.id.tilNewPassword);
        tilNewPasswordRetype = (TextInputLayout) findViewById(R.id.tilNewPasswordRetype);
        btnModify = (Button) findViewById(R.id.btnModify);
    }

    @Override
    protected void initListener() {
        tilNewPassword.getEditText().addTextChangedListener(PASSWORD_WATCHER);
        tilNewPasswordRetype.getEditText().addTextChangedListener(PASSWORD_RETYPE_WATCHER);
        btnModify.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        btnModify.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnModify:
                modifyPassword();
                break;
        }
    }

    private void modifyPassword() {
        MaterialDialog dialog = new MaterialDialog.Builder(this).title("正在修改密码").content("请稍后...")
                .cancelable(false).progressIndeterminateStyle(false).progress(true, 0).build();
        String currentPassword = MD5Utils.encipher(tilCurrentPassword.getEditText().getText().toString());
        String newPassword = MD5Utils.encipher(tilNewPassword.getEditText().getText().toString());
        UserApi.modifyPassword(currentPassword, newPassword, new SimpleServiceCallback<User>(clModifyPassword, dialog) {
            @Override
            public void onGetDataSuccessful(User user) {
                ActivityUtils.exitApp(ModifyPasswordActivity.this, "修改密码成功，你需要重新登录");
            }
        });
    }

    private void refreshOperationEnable() {
        String newPassword = tilNewPassword.getEditText().getText().toString();
        String newPasswordRetype = tilNewPasswordRetype.getEditText().getText().toString();
        btnModify.setEnabled(TextInputLayoutUtils.isInputComplete(tilCurrentPassword)
                && TextInputLayoutUtils.isInputComplete(tilNewPassword)
                && TextInputLayoutUtils.isInputComplete(tilNewPasswordRetype)
                && newPassword.equals(newPasswordRetype));
    }
}
