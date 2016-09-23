package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.hxh19950701.teachingevaluateclient.Bean.LoginBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseTextWatcher;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.internet.NetServer;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.hxh19950701.teachingevaluateclient.utils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;

public class LoginActivity extends BaseActivity {

    protected Toolbar toolbar;
    protected FloatingActionButton fabRegisterStudent;
    protected EditText etUsername;
    protected EditText etPassword;
    protected CoordinatorLayout clLogin;
    protected Button btnLogin;
    protected CheckBox cbAutoLogin;
    protected CheckBox cbRememberPassword;
    protected LinearLayout llLogin;

    protected boolean isMD5;

    @Override
    public void initView() {
        if (PrefUtils.getBoolean("AutoLogin", false)) {
            NetServer.login(PrefUtils.getString("username", ""), PrefUtils.getString("password", ""), null);
            startActivity(new Intent(getApplication(), StudentMainUiActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabRegisterStudent = (FloatingActionButton) findViewById(R.id.fabRegisterStudent);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        clLogin = (CoordinatorLayout) findViewById(R.id.clLogin);
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        cbAutoLogin = (CheckBox) findViewById(R.id.cbAutoLogin);
        cbRememberPassword = (CheckBox) findViewById(R.id.cbRememberPassword);
    }

    @Override
    public void initListener() {
        fabRegisterStudent.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        etUsername.addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                etPassword.setText("");
            }
        });
        cbAutoLogin.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cbRememberPassword.setChecked(true);
                            cbRememberPassword.setEnabled(false);
                        } else {
                            cbRememberPassword.setEnabled(true);
                        }
                    }
                }
        );
        etPassword.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus && isMD5) {
                            isMD5 = false;
                            etPassword.setText("");
                        }
                    }
                });
        etPassword.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            startLogin();
                            return true;
                        }
                        return false;
                    }
                }
        );
        cbRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    PrefUtils.putString("password", "");
                }
            }
        });
    }

    @Override
    public void initDate() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.login);
        initLoginInfo();
        initAnim();
        initMsg();
    }

    private void initLoginInfo() {
        isMD5 = true;
        etUsername.setText(PrefUtils.getString("username", ""));
        etPassword.setText(PrefUtils.getString("password", ""));
        cbRememberPassword.setChecked(PrefUtils.getBoolean("RememberPassword", false));
        cbAutoLogin.setChecked(PrefUtils.getBoolean("AutoLogin", false));
    }

    private void initMsg() {
        Intent intent = getIntent();
        if (intent != null) {
            String msg = intent.getStringExtra("msg");
            if (!TextUtils.isEmpty(msg)) {
                SnackBarUtils.showLongPost(clLogin, msg);
            }
        }
    }

    public void initAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, ViewUtils.getScreenHeight(), 0);
        translateAnimation.setDuration(1000);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(2000);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        llLogin.startAnimation(animationSet);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabRegisterStudent:
                startActivityForResult(new Intent(getApplication(), RegisterUserActivity.class), 1);
                break;
            case R.id.btnLogin:
                startLogin();
                break;
        }
    }

    private void startLogin() {
        //判断用户名及密码是否已填写
        if (TextUtils.isEmpty(etUsername.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
            SnackBarUtils.showLong(clLogin, getText(R.string.fillInUsernameAndPassword));
            return;
        }
        //获取用户名及密码MD5
        final String username = etUsername.getText().toString();
        final String password = isMD5 ? etPassword.getText().toString() : MD5Utils.encipher(etPassword.getText().toString());
        //显示登录对话框
        final MaterialDialog loginDialog = new MaterialDialog.Builder(this)
                .title(R.string.loggingIn).content(R.string.wait)
                .progress(true, 0).progressIndeterminateStyle(true)
                .cancelable(true).show();
        //开始登录
        final HttpHandler httpHandler = NetServer.login(username, password, new BaseRequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                super.onSuccess(responseInfo);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(responseInfo.result, LoginBean.class);
                if (loginBean.isSuccess()) {
                    saveDate(username, password);
                    switch (loginBean.getIdentity()) {
                        case Constant.IDENTITY_STUDENT:
                            startActivity(new Intent(getApplication(), StudentMainUiActivity.class));
                            break;
                        case Constant.IDENTITY_TEACHER:
                            break;
                        case Constant.IDENTITY_ADMINISTRATOR:
                            break;
                        default:
                            break;
                    }
                    finish();
                } else {
                    switch (loginBean.getErrorCode()) {
                        case Constant.ERROR_NO_SUCH_USERNAME:
                            SnackBarUtils.showLong(clLogin, getText(R.string.nonExistUsername));
                            break;
                        case Constant.ERROR_INCORRECT_PASSWORD:
                            SnackBarUtils.showLong(clLogin, getText(R.string.errorPassword));
                            break;
                        default:
                            SnackBarUtils.showLong(clLogin, getText(R.string.systemError));
                            break;
                    }
                }
                loginDialog.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                super.onFailure(e, s);
                SnackBarUtils.showLong(clLogin, String.format(getString(R.string.connectServerFail), e.getExceptionCode()));
                loginDialog.dismiss();
            }
        });
        //添加取消登录监听器
        loginDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SnackBarUtils.showLong(clLogin, "登录过程被取消。");
                httpHandler.cancel();
            }
        });
    }

    private void saveDate(String username, String password) {
        if (cbRememberPassword.isChecked()) {
            PrefUtils.putBoolean("RememberPassword", true);
            PrefUtils.putBoolean("AutoLogin", cbAutoLogin.isChecked());
            PrefUtils.putString("username", username);
            PrefUtils.putString("password", password);
        } else {
            PrefUtils.putBoolean("RememberPassword", false);
            PrefUtils.putBoolean("AutoLogin", false);
            PrefUtils.putString("username", username);
            PrefUtils.putString("password", "");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                showSetServerIpDialog();
                return true;
            case R.id.action_help:
                startActivity(new Intent(this, HelpActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSetServerIpDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.setServerAddress)
                .content(R.string.setServerAddressHint)
                .inputType(InputType.TYPE_TEXT_VARIATION_URI)
                .cancelable(true)
                .neutralText(R.string.reset)
                .negativeText(R.string.cancel)
                .positiveText(R.string.modify)
                .alwaysCallInputCallback()
                .input("", PrefUtils.getString("serverURL", "http://"), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if ((TextUtils.isEmpty(input.toString()) || input.toString().startsWith("http://")) && input.length() > 10) {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        } else {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = dialog.getInputEditText().getText().toString();
                        if (TextUtils.isEmpty(url)) {
                            PrefUtils.remove("serverURL");
                        } else {
                            PrefUtils.putString("serverURL", dialog.getInputEditText().getText().toString());
                        }
                        TeachingEvaluateClientApplication.initServerURL();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PrefUtils.remove("serverURL");
                        TeachingEvaluateClientApplication.initServerURL();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            etUsername.setText(data.getStringExtra("username"));
            etPassword.setText(data.getStringExtra("password"));
            cbRememberPassword.setChecked(true);
            isMD5 = false;
            SnackBarUtils.showLongPost(clLogin, "注册成功，点击登录按钮立刻登录。");
        }
    }

}