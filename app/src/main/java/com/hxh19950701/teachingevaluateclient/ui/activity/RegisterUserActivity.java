package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.hxh19950701.teachingevaluateclient.Bean.HasExistBean;
import com.hxh19950701.teachingevaluateclient.Bean.RegisterUserBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestParams;
import com.hxh19950701.teachingevaluateclient.base.BaseTextWatcher;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

public class RegisterUserActivity extends BaseActivity {

    protected Toolbar toolbar;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                new BaseTextWatcher() {
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
                new BaseTextWatcher() {
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
                new BaseTextWatcher() {
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
    public void initDate() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("注册");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etPassword.setEnabled(false);
        etPasswordAgain.setEnabled(false);
        btnRegister.setEnabled(false);
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
        BaseRequestParams requestParams = new BaseRequestParams();
        requestParams.addQueryStringParameter("action", "hasExist");
        requestParams.addQueryStringParameter("username", etUsername.getText().toString());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getUserManagerURL(),
                requestParams, new BaseRequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        HasExistBean hasExistBean = gson.fromJson(responseInfo.result, HasExistBean.class);
                        if (hasExistBean.isExist()) {
                            tilUsername.setError(getText(R.string.usernameInUse));
                            btnRegister.setEnabled(false);
                        } else {
                            tilUsername.setError("");
                            tilUsername.setErrorEnabled(false);
                        }
                    }
                });
    }

    protected void register() {
        final MaterialDialog registerDialog = new MaterialDialog.Builder(this)
                .title("正在注册").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).show();
        BaseRequestParams requestParams = new BaseRequestParams();
        requestParams.addBodyParameter("action", "register");
        requestParams.addBodyParameter("username", etUsername.getText().toString());
        requestParams.addBodyParameter("password", MD5Utils.encipher(etPassword.getText().toString()));
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, TeachingEvaluateClientApplication.getUserManagerURL(),
                requestParams, new BaseRequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        RegisterUserBean registerUserBean = gson.fromJson(responseInfo.result, RegisterUserBean.class);
                        if (registerUserBean.isSuccess()) {
                            registerDialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("username", etUsername.getText().toString());
                            intent.putExtra("password", etPassword.getText().toString());
                            setResult(1, intent);
                            finish();
                        } else {
                            SnackBarUtils.showLong(clRegister, getText(R.string.systemError));
                            registerDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        super.onFailure(e, s);
                        SnackBarUtils.showLong(clRegister, String.format(getString(R.string.connectServerFail), e.getExceptionCode()));
                        registerDialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(clRegister.getWindowToken(), 0);
    }
}