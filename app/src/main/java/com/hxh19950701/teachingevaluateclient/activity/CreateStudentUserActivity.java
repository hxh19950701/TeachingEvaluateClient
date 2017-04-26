package com.hxh19950701.teachingevaluateclient.activity;

import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.dialog.CreateStudentDialog;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateStudentUserActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rbSingleCreate)
    RadioButton rbSingleCreate;
    @BindView(R.id.rbMultiCreate)
    RadioButton rbMultiCreate;
    @BindView(R.id.rgCreateType)
    RadioGroup rgCreateType;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.tilNumber)
    TextInputLayout tilNumber;
    @BindView(R.id.tilUsername)
    TextInputLayout tilUsername;
    @BindView(R.id.clCreateStudentUser)
    CoordinatorLayout clCreateStudentUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_student_user;
    }

    @Override
    protected void initListener() {
        rgCreateType.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        tilNumber.setVisibility(checkedId == R.id.rbMultiCreate ? View.VISIBLE : View.GONE);
        tilUsername.setHint(checkedId == R.id.rbMultiCreate ? "用户名前缀" : "用户名");
    }

    private boolean isSingleCreateMode() {
        return rgCreateType.getCheckedRadioButtonId() == R.id.rbSingleCreate;
    }

    private boolean isLegalUsername() {
        if (isSingleCreateMode()) {
            return etUsername.length() >= 6 && etUsername.length() <= 16;
        } else {
            return etUsername.length() + etNumber.length() >= 6 && etUsername.length() <= 16;
        }
    }

    @OnClick(R.id.btnCreateType)
    public void showCreateDialog() {
        if (isLegalUsername()) {
            if (!isSingleCreateMode() && (etNumber.getText().toString().equals("")
                    || etNumber.getText().toString().equals("0")
                    || etNumber.getText().toString().equals("1")
                    || etNumber.getText().toString().equals("01"))) {
                SnackBarUtils.showLong(clCreateStudentUser, "数量不能为空,0和1");
            } else {
                int number = isSingleCreateMode() ? 1 : Integer.parseInt(etNumber.getText().toString());
                new MaterialDialog.Builder(this).content("将创建" + number + "个学生用户。")
                        .positiveText("创建").onPositive((dialog, which) -> create())
                        .negativeText("取消").show();
            }
        } else {
            SnackBarUtils.showLong(clCreateStudentUser, "用户名不合法");
        }
    }

    private void create() {
        List<String> list;
        if (isSingleCreateMode()) {
            list = new ArrayList<>(1);
            list.add(etUsername.getText().toString());

        } else {
            int number = Integer.parseInt(etNumber.getText().toString());
            list = new ArrayList<>(number);
            for (int i = 1; i <= number; ++i) {
                String postfix = number >= 10 && i < 10 ? "0" + i : "" + i;
                list.add(etUsername.getText().toString() + postfix);
            }
        }
        new CreateStudentDialog(this, list).show();
    }

}
