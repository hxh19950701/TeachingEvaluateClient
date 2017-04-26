package com.hxh19950701.teachingevaluateclient.activity;

import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;

import butterknife.OnClick;

public class AdministratorMainUiActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_administrator_main_ui;
    }

    @Override
    @OnClick({R.id.btnUserManage,
            R.id.btnDepartmentManage,
            R.id.btnCreateUser,
            R.id.btnChangePassword,
            R.id.btnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUserManage:
                IntentUtils.startActivity(this, UserManageActivity.class);
                break;
            case R.id.btnDepartmentManage:
                IntentUtils.startActivity(this, DepartmentManageActivity.class);
                break;
            case R.id.btnCreateUser:
                IntentUtils.startActivity(this, CreateUserActivity.class);
                break;
            case R.id.btnChangePassword:
                IntentUtils.startActivity(this, ModifyPasswordActivity.class);
                break;
            case R.id.btnLogout:
                ActivityUtils.exitApp(this, "您已注销成功");
                break;
        }
    }

}
