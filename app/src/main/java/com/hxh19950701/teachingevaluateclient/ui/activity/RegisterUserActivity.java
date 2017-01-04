package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.User;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.event.UserRegisterCompleteEvent;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.InputMethodUtils;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import org.greenrobot.eventbus.EventBus;

public class RegisterUserActivity extends BaseActivity {

    protected EditText etUsername;
    protected EditText etPassword;
    protected EditText etPasswordAgain;
    protected Button btnRegister;
    protected CoordinatorLayout clRegister;
    protected TextInputLayout tilUsername;
    protected TextInputLayout tilPassword;
    protected TextInputLayout tilPasswordAgain;

    @Override
    public void initView() {
        setContentView(R.layout.activity_register_user);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordAgain = (EditText) findViewById(R.id.etPasswordAgain);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        clRegister = (CoordinatorLayout) findViewById(R.id.clRegister);
        tilUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        tilPasswordAgain = (TextInputLayout) findViewById(R.id.tilPasswordAgain);
    }

    @Override
    public void initListener() {
        btnRegister.setOnClickListener(this);
        etUsername.addTextChangedListener(
                new TextWatcherImpl() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        etPassword.setText("");
                        String str = s.toString();
                        if (str.isEmpty()) {
                            tilUsername.setError("");
                            tilUsername.setErrorEnabled(false);
                            btnRegister.setEnabled(false);
                        } else if (str.length() < 6 || str.length() > 16) {
                            tilUsername.setError("用户名由6~16个数字和字母组成。");
                            etPassword.setEnabled(false);
                            etPasswordAgain.setEnabled(false);
                            btnRegister.setEnabled(false);
                        } else {
                            tilUsername.setError("");
                            tilUsername.setErrorEnabled(false);
                            etPassword.setEnabled(true);
                        }
                    }
                }
        );
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUsername();
                }
            }
        });
        etPassword.addTextChangedListener(
                new TextWatcherImpl() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        etPasswordAgain.setText("");
                        if (etPassword.getText().toString().isEmpty()) {
                            tilPassword.setError("");
                            tilPassword.setErrorEnabled(false);
                        } else if (s.length() < 6 || s.length() > 16) {
                            tilPassword.setError("密码由6~16个数字，字母和符号组成。");
                            etPasswordAgain.setEnabled(false);
                            btnRegister.setEnabled(false);
                        } else {
                            etPasswordAgain.setEnabled(true);
                            tilPassword.setError("");
                            tilPassword.setErrorEnabled(false);
                        }
                    }
                }

        );

        etPasswordAgain.addTextChangedListener(
                new TextWatcherImpl() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        String password = etPassword.getText().toString();
                        if (password.startsWith(s.toString())) {
                            tilPasswordAgain.setError("");
                            tilPasswordAgain.setErrorEnabled(false);
                            if (password.equals(s.toString())) {
                                if (etPasswordAgain.isEnabled() && TextUtils.isEmpty(tilUsername.getError())
                                        && TextUtils.isEmpty(tilPassword.getError())) {
                                    btnRegister.setEnabled(true);
                                }
                            } else {
                                btnRegister.setEnabled(false);
                            }
                        } else {
                            tilPasswordAgain.setError(getText(R.string.passwordInconsistent));
                            btnRegister.setEnabled(false);
                        }
                    }
                }
        );
    }

    @Override
    public void initData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etPassword.setEnabled(false);
        etPasswordAgain.setEnabled(false);
        btnRegister.setEnabled(false);
        InputMethodUtils.showSoftInput(etUsername);
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

    protected void checkUsername() {
        String username = etUsername.getText().toString();
        UserApi.hasExist(username, new SimpleServiceCallback<Boolean>(clRegister) {
            @Override
            public void onGetDataSuccess(Boolean isExist) {
                if (isExist) {
                    tilUsername.setError(getText(R.string.usernameInUse));
                    btnRegister.setEnabled(false);
                } else {
                    tilUsername.setError("");
                    tilUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void onGetDataFailure(int code, String msg) {
                tilUsername.setError("我们无法检测该用户名是否可用");
                btnRegister.setEnabled(false);
            }

            @Override
            public void onException(String s) {
                tilUsername.setError("我们无法检测该用户名是否可用");
                btnRegister.setEnabled(false);
            }
        });
    }

    protected void register() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this).title("正在注册").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).build();

        final String username = etUsername.getText().toString();
        final String password = MD5Utils.encipher(etPassword.getText().toString());

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
            public void onException(String s) {
                super.onException(s);
                SnackBarUtils.showLong(clRegister, getText(R.string.systemError));
            }

            @Override
            public void onGetDataSuccess(User user) {
                UserRegisterCompleteEvent event = new UserRegisterCompleteEvent(username, password);
                EventBus.getDefault().post(event);
                finish();
            }
        });
    }


    private void showErrorMsg(int errorCode) {
        switch (errorCode) {
            case Constant.ERROR_NO_SUCH_USERNAME:
            case Constant.ERROR_INVALID_USERNAME:
                SnackBarUtils.showLong(clRegister, R.string.nonExistUsername);
                break;
            case Constant.ERROR_INCORRECT_PASSWORD:
            case Constant.ERROR_INVALID_PASSWORD:
                SnackBarUtils.showLong(clRegister, R.string.errorPassword);
                break;
            default:
                SnackBarUtils.showLong(clRegister, R.string.systemError);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InputMethodUtils.hideSoftInputFromWindow(clRegister.getWindowToken());
    }
}