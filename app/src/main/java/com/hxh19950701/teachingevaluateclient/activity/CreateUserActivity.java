package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.hxh19950701.teachingevaluateclient.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateUserActivity extends BaseActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.tilUsername)
    TextInputLayout tilUsername;
    @BindView(R.id.rbStudent)
    RadioButton rbStudent;
    @BindView(R.id.rbTeacher)
    RadioButton rbTeacher;
    @BindView(R.id.rbAdministrator)
    RadioButton rbAdministrator;
    @BindView(R.id.rgIdentity)
    RadioGroup rgIdentity;
    @BindView(R.id.clCreateUser)
    CoordinatorLayout clCreateUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_user;
    }

    @Override
    protected void initView() {
        displayHomeAsUp();
    }

    @OnClick(R.id.btnCreate)
    public void showCreateUserDialog() {
        if (etUsername.length() < 6) {
            SnackBarUtils.showLong(clCreateUser, "用户名最少为6位");
            return;
        }
        int identity = Integer.parseInt(findViewById(rgIdentity.getCheckedRadioButtonId()).getTag().toString());
        String content = new StringBuilder("您要创建的用户信息如下：")
                .append("\n用户名：").append(etUsername.getText())
                .append("\n身份：").append(UserUtils.getIdentityString(identity))
                .append(identity == Constant.IDENTITY_ADMINISTRATOR ? "\n管理员用户之间不能相互管理，请谨慎创建管理员身份的用户。" : "")
                .toString();
        new MaterialDialog.Builder(this).title("创建用户").content(content)
                .positiveText("创建").onPositive((dialog, which) -> create())
                .show();
    }

    private void create() {
        String username = etUsername.getText().toString();
        int identity = Integer.parseInt(findViewById(rgIdentity.getCheckedRadioButtonId()).getTag().toString());
        UserApi.register(username, MD5Utils.encipher(username), identity, "",new SimpleServiceCallback<User>(clCreateUser) {
            @Override
            public void onGetDataSuccessful(User user) {
                SnackBarUtils.showLong(clCreateUser, "新建用户成功");
            }
        });
    }
}