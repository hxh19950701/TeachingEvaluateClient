package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.TextInputLayoutUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ModifyPasswordActivity extends BaseActivity {

    @BindView(R.id.clModifyPassword)
    /*package*/ CoordinatorLayout clModifyPassword;
    @BindView(R.id.tilCurrentPassword)
    /*package*/ TextInputLayout tilCurrentPassword;
    @BindView(R.id.tilNewPassword)
    /*package*/ TextInputLayout tilNewPassword;
    @BindView(R.id.tilNewPasswordRetype)
    /*package*/ TextInputLayout tilNewPasswordRetype;
    @BindView(R.id.btnModify)
    /*package*/ Button btnModify;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        refreshOperationEnable();
    }

    @OnTextChanged(value = R.id.tvNewPassword)
    public void checkNewPassword(){
        String newPassword = tilNewPassword.getEditText().getText().toString();
        if (newPassword.isEmpty()) {
            tilNewPasswordRetype.getEditText().setEnabled(false);
            TextInputLayoutUtils.setErrorEnabled(tilNewPassword, false);
        } else if (newPassword.length() < 6) {
            tilNewPasswordRetype.getEditText().setEnabled(false);
            tilNewPassword.setError("密码由6~16个数字，字母和符号组成。");
        } else {
            tilNewPasswordRetype.getEditText().setEnabled(true);
            TextInputLayoutUtils.setErrorEnabled(tilNewPassword, false);
        }
        tilNewPasswordRetype.getEditText().setText("");
        refreshOperationEnable();
    }

    @OnTextChanged(value = R.id.tvNewPasswordRetype)
    public void checkNewPasswordRetype(){
        String newPassword = tilNewPassword.getEditText().getText().toString();
        String newPasswordRetype = tilNewPasswordRetype.getEditText().getText().toString();
        if (newPassword.startsWith(newPasswordRetype)) {
            TextInputLayoutUtils.setErrorEnabled(tilNewPasswordRetype, false);
        } else {
            tilNewPasswordRetype.setError(getText(R.string.passwordInconsistent));
        }
        refreshOperationEnable();
    }

    @OnClick(R.id.btnModify)
    public void modifyPassword() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在修改密码").content("请稍后...")
                .cancelable(false).progress(true, 0).build();
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
