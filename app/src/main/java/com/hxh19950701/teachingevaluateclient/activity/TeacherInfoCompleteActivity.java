package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Teacher;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.TeacherApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TeacherInfoCompleteActivity extends BaseActivity {

    @BindView(R.id.etTeacherId)
    EditText etTeacherId;
    @BindView(R.id.tilTeacherId)
    TextInputLayout tilTeacherId;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.rbMale)
    RadioButton rbMale;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.rgSex)
    RadioGroup rgSex;
    @BindView(R.id.clTeacherInfoComplete)
    CoordinatorLayout clTeacherInfoComplete;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_teacher_info_complete;
    }

    @Override
    protected void initView() {
        displayHomeAsUp();
    }

    @OnClick(R.id.btnSubmit)
    public void showSubmitDialog() {
        if (etTeacherId.length() == 0) {
            SnackBarUtils.showLong(clTeacherInfoComplete, "请填写你的教师工号");
            return;
        }
        if (etName.length() == 0) {
            SnackBarUtils.showLong(clTeacherInfoComplete, "请填写你的姓名");
            return;
        }
        String content = new StringBuilder("请确认你的信息是否正确，提交后将不能修改。")
                .append("\n教师工号：").append(etTeacherId.getText())
                .append("\n教师姓名：").append(etName.getText())
                .append("\n性别：").append(rgSex.getCheckedRadioButtonId() == R.id.rbMale ? "男" : "女")
                .toString();
        new MaterialDialog.Builder(this).title("提交信息").content(content)
                .positiveText("提交").onPositive((dialog, which) -> submit())
                .show();
    }

    private void submit(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在保存信息").content("请稍后...")
                .cancelable(false).progress(true, 0).build();
        String teacherId = etTeacherId.getText().toString();
        String name = etName.getText().toString();
        int sex = rgSex.getCheckedRadioButtonId() == R.id.rbMale ? Constant.SEX_MALE : Constant.SEX_FEMALE;
        TeacherApi.register(teacherId, name, sex, new SimpleServiceCallback<Teacher>(clTeacherInfoComplete, dialog) {
            @Override
            public void onGetDataSuccessful(Teacher teacher) {
                IntentUtils.startActivity(TeacherInfoCompleteActivity.this, TeacherMainUiActivity.class,
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this).content("你的信息尚未保存，现在退出吗？")
                .positiveText("退出").negativeText("取消")
                .onPositive((dialog, which) -> ActivityUtils.exitApp(TeacherInfoCompleteActivity.this, "在你完善信息之前，你将无法使用本系统。"))
                .show();
    }
}
