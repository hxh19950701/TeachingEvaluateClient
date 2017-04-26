package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.event.ServerUrlChangedEvent;
import com.hxh19950701.teachingevaluateclient.event.UserLoginSuccessfullyEvent;
import com.hxh19950701.teachingevaluateclient.event.UserRegisterCompleteEvent;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.DisplayUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.http.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class LoginActivity extends BaseActivity {

    public static Intent newIntent(Context context, String msg) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Constant.KEY_MASSAGE, msg);
        return intent;
    }

    @BindView(R.id.tilUsername)
    /*package*/ TextInputLayout tilUsername;
    @BindView(R.id.tilPassword)
    /*package*/ TextInputLayout tilPassword;
    @BindView(R.id.clLogin)
    /*package*/ CoordinatorLayout clLogin;
    @BindView(R.id.cbAutoLogin)
    /*package*/ CheckBox cbAutoLogin;
    @BindView(R.id.cbRememberPassword)
    /*package*/ CheckBox cbRememberPassword;

    private boolean isMD5;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        initLoginInfo();
        initAnim();
        initMsg();
        startReceiveEvent();
    }

    private void initLoginInfo() {
        tilUsername.getEditText().setText(PrefUtils.getString(Constant.KEY_USERNAME, ""));
        tilPassword.getEditText().setText(PrefUtils.getString(Constant.KEY_PASSWORD, ""));
        cbRememberPassword.setChecked(PrefUtils.getBoolean(Constant.KEY_REMEMBER_PASSWORD, false));
        cbAutoLogin.setChecked(PrefUtils.getBoolean(Constant.KEY_AUTO_LOGIN, false));
        isMD5 = true;
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
        LinearLayout llLogin = (LinearLayout) findViewById(R.id.llLogin);
        llLogin.startAnimation(animationSet);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserRegisterComplete(UserRegisterCompleteEvent event) {
        tilUsername.getEditText().setText(event.getUsername());
        tilPassword.getEditText().setText(event.getPassword());
        cbRememberPassword.setChecked(true);
        isMD5 = true;
    }

    @OnCheckedChanged(R.id.cbAutoLogin)
    public void onAutoLoginSettingChanged() {
        cbRememberPassword.setChecked(true);
        cbRememberPassword.setEnabled(!cbAutoLogin.isChecked());
    }

    @OnEditorAction(R.id.etPassword)
    public boolean onPasswordTextViewAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            startLogin();
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.etUsername)
    public void clearInputtedPassword() {
        tilPassword.getEditText().setText("");
        isMD5 = false;
    }

    @OnTextChanged(value = R.id.etPassword)
    public void onUserInputPassword() {
        if (isMD5 && tilPassword.getEditText().hasFocus()) {
            isMD5 = false;
            tilPassword.getEditText().setText("");
        }
    }

    @OnClick(R.id.fabRegisterStudent)
    public void registerStudent() {
        IntentUtils.startActivity(this, RegisterUserActivity.class);
    }

    @OnClick(R.id.btnLogin)
    public void startLogin() {
        if (tilUsername.getEditText().length() == 0 || tilPassword.getEditText().length() == 0) {
            SnackBarUtils.showLong(clLogin, R.string.fillInUsernameAndPassword);
            return;
        }
        String username = tilUsername.getEditText().getText().toString();
        String password = isMD5 ? tilPassword.getEditText().getText().toString()
                : MD5Utils.encipher(tilPassword.getEditText().getText().toString());
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.loggingIn).content(R.string.wait)
                .progress(true, 0).progressIndeterminateStyle(true)
                .cancelable(true).build();
        HttpHandler httpHandler = UserApi.login(username, password, new SimpleServiceCallback<User>(clLogin, dialog) {
            @Override
            public void onGetDataSuccessful(User user) {
                onUserLoginSuccessfully(user);
            }

            @Override
            public void onGetDataFailed(int code, String msg) {
                super.onGetDataFailed(code, msg);
                showErrorMsg(code);
            }
        });
        dialog.setOnCancelListener(dialogInterface -> {
            SnackBarUtils.showLong(clLogin, "登录过程被取消。");
            httpHandler.cancel();
        });
    }

    private void onUserLoginSuccessfully(User user) {
        String username = tilUsername.getEditText().getText().toString();
        String password = isMD5 ? tilPassword.getEditText().getText().toString()
                : MD5Utils.encipher(tilPassword.getEditText().getText().toString());
        EventManager.postEvent(new UserLoginSuccessfullyEvent(
                username, password, cbRememberPassword.isChecked(), cbAutoLogin.isChecked()));
        enterApp(user.getIdentity());
    }

    private void enterApp(int identity) {
        try {
            ActivityUtils.enterApp(this, identity);
        } catch (IllegalArgumentException e) {
            SnackBarUtils.showSystemError(clLogin);
        }
    }

    private void showErrorMsg(int errorCode) {
        switch (errorCode) {
            case Constant.CODE_NO_SUCH_USERNAME:
            case Constant.CODE_INVALID_USERNAME:
                SnackBarUtils.showLong(clLogin, R.string.nonExistUsername);
                break;
            case Constant.CODE_INCORRECT_PASSWORD:
            case Constant.CODE_INVALID_PASSWORD:
                tilPassword.getEditText().setText("");
                SnackBarUtils.showLong(clLogin, R.string.errorPassword);
                break;
            case Constant.CODE_DISABLED_USER:
                SnackBarUtils.showLong(clLogin, "该用户尚未启用，请联系管理员。");
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
                .input(null, PrefUtils.getString(Constant.KEY_SERVER_DOMAIN, Constant.PREFIX_SERVER_DOMAIN),
                        (dialog, input) -> {
                            boolean enable = input.toString().startsWith(Constant.PREFIX_SERVER_DOMAIN) && input.length() > 10;
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(enable);
                        })
                .onPositive((dialog, which) -> {
                    String url = dialog.getInputEditText().getText().toString();
                    PrefUtils.putString(Constant.KEY_SERVER_DOMAIN, url);
                    EventBus.getDefault().post(new ServerUrlChangedEvent(url));
                })
                .onNeutral((dialog, which) -> {
                    PrefUtils.remove(Constant.KEY_SERVER_DOMAIN);
                    EventBus.getDefault().post(new ServerUrlChangedEvent(""));
                })
                .show();
    }

}