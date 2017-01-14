package com.hxh19950701.teachingevaluateclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.User;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.event.ServerUrlChangedEvent;
import com.hxh19950701.teachingevaluateclient.event.UserLoginSuccessEvent;
import com.hxh19950701.teachingevaluateclient.event.UserRegisterCompleteEvent;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.DisplayUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.http.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {

    protected FloatingActionButton fabRegisterStudent;
    protected EditText etUsername;
    protected EditText etPassword;
    protected CoordinatorLayout clLogin;
    protected Button btnLogin;
    protected CheckBox cbAutoLogin;
    protected CheckBox cbRememberPassword;
    protected LinearLayout llLogin;

    protected boolean isMD5;

    private TextWatcher usernameWatcher = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            etPassword.setText("");
        }
    };

    private TextWatcher passwordWatcher = new TextWatcherImpl() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            super.beforeTextChanged(s, start, count, after);
            if (isMD5 && etPassword.hasFocus()) {
                isMD5 = false;
                etPassword.setText("");
            }
        }
    };

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
        cbAutoLogin.setOnCheckedChangeListener(this);
        etUsername.addTextChangedListener(usernameWatcher);
        etPassword.addTextChangedListener(passwordWatcher);
        etPassword.setOnEditorActionListener(this);
    }

    @Override
    public void initData() {
        initLoginInfo();
        initAnim();
        initMsg();
        startReceiveEvent();
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
        String msg = intent.getStringExtra(Constant.KEY_MASSAGE);
        if (!TextUtils.isEmpty(msg)) {
            SnackBarUtils.showLongPost(clLogin, msg);
        }
    }

    public void initAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, DisplayUtils.getScreenHeight(), 0);
        translateAnimation.setDuration(1000);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(2000);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        llLogin.startAnimation(animationSet);
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void onUserRegisterComplete(UserRegisterCompleteEvent event) {
        etUsername.setText(event.getUsername());
        etPassword.setText(event.getPassword());
        cbRememberPassword.setChecked(true);
        isMD5 = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabRegisterStudent:
                IntentUtils.startActivity(this, RegisterUserActivity.class);
                break;
            case R.id.btnLogin:
                startLogin();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cbAutoLogin:
                cbRememberPassword.setChecked(true);
                cbRememberPassword.setEnabled(!isChecked);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            startLogin();
            return true;
        }
        return false;
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
        final MaterialDialog dialog = new MaterialDialog.Builder(this).title(R.string.loggingIn).content(R.string.wait)
                .progress(true, 0).progressIndeterminateStyle(true).cancelable(true).build();
        //开始登录
        final HttpHandler httpHandler = UserApi.login(username, password, new SimpleServiceCallback<User>(clLogin) {

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
                EventBus.getDefault().post(new UserLoginSuccessEvent(username, password, cbRememberPassword.isChecked(), cbAutoLogin.isChecked()));
                enterApp(user.getIdentity());
            }

            @Override
            public void onGetDataFailure(int code, String msg) {
                showErrorMsg(code);
            }
        });
        //添加取消登录监听器
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
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
            case Constant.IDENTITY_TEACHER:
            case Constant.IDENTITY_ADMINISTRATOR:
                IntentUtils.startActivity(this, Constant.IDENTITY_ACTIVITY[identity], Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                etPassword.setText("");
                SnackBarUtils.showLong(clLogin, R.string.errorPassword);
                break;
            default:
                SnackBarUtils.showSystemError(clLogin);
                break;
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
                IntentUtils.startActivity(this, HelpActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSetServerIpDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.setServerAddress)
                .content(R.string.setServerAddressHint)
                .inputType(InputType.TYPE_TEXT_VARIATION_URI).cancelable(true)
                .neutralText(R.string.reset).negativeText(R.string.cancel).positiveText(R.string.modify)
                .alwaysCallInputCallback()
                .input(null, PrefUtils.getString(Constant.KEY_SERVER_DOMAIN, Constant.PREFIX_SERVER_DOMAIN), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        boolean enable = input.toString().startsWith(Constant.PREFIX_SERVER_DOMAIN) && input.length() > 10;
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(enable);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = dialog.getInputEditText().getText().toString();
                        PrefUtils.putString(Constant.KEY_SERVER_DOMAIN, url);
                        EventBus.getDefault().post(new ServerUrlChangedEvent(url));
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PrefUtils.remove(Constant.KEY_SERVER_DOMAIN);
                        EventBus.getDefault().post(new ServerUrlChangedEvent(""));
                    }
                })
                .show();
    }


}