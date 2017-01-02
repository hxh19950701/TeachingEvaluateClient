package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.application.MainApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.User;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.hxh19950701.teachingevaluateclient.utils.ViewUtils;
import com.lidroid.xutils.http.HttpHandler;

public class LoginActivity extends BaseActivity {

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
        setContentView(R.layout.activity_login);
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
        etUsername.addTextChangedListener(new TextWatcherImpl() {
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
    }

    @Override
    public void initData() {
        initLoginInfo();
        initAnim();
        initMsg();
    }


    private void initLoginInfo() {
        isMD5 = true;
        etUsername.setText(PrefUtils.getString(Constant.KEY_USERNAME, ""));
        etPassword.setText(PrefUtils.getString(Constant.KEY_PASSWORD, ""));
        cbRememberPassword.setChecked(PrefUtils.getBoolean(Constant.KEY_REMEMBER_PASSWORD, false));
        cbAutoLogin.setChecked(PrefUtils.getBoolean(Constant.KEY_AUTO_LOGIN, false));
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
                startActivityForResult(new Intent(this, RegisterUserActivity.class), 1);
                break;
            case R.id.btnLogin:
                startLogin();
                break;
        }
    }

    private void startLogin() {
        //判断用户名及密码是否已填写
        if (TextUtils.isEmpty(etUsername.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
            SnackBarUtils.showLong(clLogin, R.string.fillInUsernameAndPassword);
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
        final HttpHandler httpHandler = UserApi.login(username, password,
                new SimpleServiceCallback<User>(clLogin) {
                    @Override
                    public void onAfter() {
                        loginDialog.dismiss();
                    }

                    @Override
                    public void onGetDataSuccess(User user) {
                        saveDate();
                        enterApp(user.getIdentity());
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

    private void enterApp(int identity) {
        switch (identity) {
            case Constant.IDENTITY_STUDENT:
                startActivity(new Intent(this, StudentMainUiActivity.class));
                break;
            case Constant.IDENTITY_TEACHER:
                startActivity(new Intent(this, TeacherMainUiActivity.class));
                break;
            case Constant.IDENTITY_ADMINISTRATOR:
                startActivity(new Intent(this, AdministratorMainUiActivity.class));
                break;
            default:
                SnackBarUtils.showSystemError(clLogin);
                break;
        }
    }

    private void showErrorMsg(int errorCode) {
        switch (errorCode) {
            case Constant.ERROR_NO_SUCH_USERNAME:
            case Constant.ERROR_INVALID_USERNAME:
                SnackBarUtils.showLong(clLogin, R.string.nonExistUsername);
                break;
            case Constant.ERROR_INCORRECT_PASSWORD:
            case Constant.ERROR_INVALID_PASSWORD:
                SnackBarUtils.showLong(clLogin, R.string.errorPassword);
                break;
            default:
                SnackBarUtils.showSystemError(clLogin);
                break;
        }
    }

    private void saveDate() {
        final String username = etUsername.getText().toString();
        final String password = isMD5 ? etPassword.getText().toString() : MD5Utils.encipher(etPassword.getText().toString());
        if (cbRememberPassword.isChecked()) {
            PrefUtils.putString(Constant.KEY_USERNAME, username);
            PrefUtils.putString(Constant.KEY_PASSWORD, password);
            PrefUtils.putBoolean(Constant.KEY_REMEMBER_PASSWORD, true);
            PrefUtils.putBoolean(Constant.KEY_AUTO_LOGIN, cbAutoLogin.isChecked());
        } else {
            PrefUtils.putString(Constant.KEY_USERNAME, username);
            PrefUtils.putString(Constant.KEY_PASSWORD, "");
            PrefUtils.putBoolean(Constant.KEY_REMEMBER_PASSWORD, false);
            PrefUtils.putBoolean(Constant.KEY_AUTO_LOGIN, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        final MainApplication application = (MainApplication) getApplication();
        new MaterialDialog.Builder(this)
                .title(R.string.setServerAddress)
                .content(R.string.setServerAddressHint)
                .inputType(InputType.TYPE_TEXT_VARIATION_URI).cancelable(true)
                .neutralText(R.string.reset).negativeText(R.string.cancel).positiveText(R.string.modify)
                .alwaysCallInputCallback()
                .input(null, PrefUtils.getString("serverURL", "http://"), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        boolean enable = input.toString().startsWith("http://") && input.length() > 10;
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(enable);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = dialog.getInputEditText().getText().toString();
                        PrefUtils.putString("serverURL", url);
                        application.initServerURL();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PrefUtils.remove("serverURL");
                        application.initServerURL();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            etUsername.setText(data.getStringExtra(Constant.KEY_USERNAME));
            etPassword.setText(data.getStringExtra(Constant.KEY_PASSWORD));
            cbRememberPassword.setChecked(true);
            isMD5 = false;
            SnackBarUtils.showLongPost(clLogin, "注册成功，点击登录按钮立刻登录。");
        }
    }

}