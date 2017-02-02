package com.hxh19950701.teachingevaluateclient.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

public class TeacherNewCourseActivity extends BaseActivity {

    protected TextInputLayout tilCourseName;
    protected TextInputLayout tilCoursePassword;
    protected TextInputLayout tilCoursePasswordRetype;
    protected TextInputLayout tilYear;
    protected TextInputLayout tilPersonCount;
    protected RadioButton rbTerm1;
    protected RadioButton rbTerm2;
    protected RadioGroup rgTerm;
    protected Button btnNewCourse;
    protected CoordinatorLayout clNewCourse;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_teacher_new_course);
        tilCourseName = (TextInputLayout) findViewById(R.id.tilCourseName);
        tilCoursePassword = (TextInputLayout) findViewById(R.id.tilCoursePassword);
        tilCoursePasswordRetype = (TextInputLayout) findViewById(R.id.tilCoursePasswordRetype);
        tilPersonCount = (TextInputLayout) findViewById(R.id.tilPersonCount);
        tilYear = (TextInputLayout) findViewById(R.id.tilYear);
        rbTerm1 = (RadioButton) findViewById(R.id.rbTerm1);
        rbTerm2 = (RadioButton) findViewById(R.id.rbTerm2);
        rgTerm = (RadioGroup) findViewById(R.id.rgTerm);
        btnNewCourse = (Button) findViewById(R.id.btnNewCourse);
        clNewCourse = (CoordinatorLayout) findViewById(R.id.clNewCourse);
    }

    @Override
    protected void initListener() {
        btnNewCourse.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCourse:
                showNewCourseDialog();
                break;
        }
    }

    private void showNewCourseDialog() {
        String courseName = tilCourseName.getEditText().getText().toString();
        String coursePassword = tilCoursePassword.getEditText().getText().toString();
        String coursePasswordRetype = tilCoursePasswordRetype.getEditText().getText().toString();
        int year = Integer.parseInt(tilYear.getEditText().getText().toString());
        int term = rbTerm1.isChecked() ? Constant.TERM_FIRST : Constant.TERM_SECOND;
        int personCount = Integer.parseInt(tilPersonCount.getEditText().getText().toString());

        if (coursePassword.equals(coursePasswordRetype)) {
            new MaterialDialog.Builder(this).title("要创建这个课程吗")
                    .content(R.string.courseInfo, courseName, year, term == Constant.TERM_FIRST ? "上学期" : "下学期", personCount)
                    .positiveText("创建")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            newCourse();
                        }
                    }).show();
        } else {
            SnackBarUtils.showLong(clNewCourse, "两次密码输入不一致");
        }
    }

    private void newCourse() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在创建").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).build();

        String courseName = tilCourseName.getEditText().getText().toString();
        String coursePassword = MD5Utils.encipher(tilCoursePassword.getEditText().getText().toString());
        int year = Integer.parseInt(tilYear.getEditText().getText().toString());
        int term = rbTerm1.isChecked() ? Constant.TERM_FIRST : Constant.TERM_SECOND;
        int personCount = Integer.parseInt(tilPersonCount.getEditText().getText().toString());

        CourseApi.newCourse(courseName, coursePassword, year, term, personCount, new SimpleServiceCallback<Course>(clNewCourse) {

            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onAfter() {
                dialog.dismiss();
            }

            @Override
            public void onGetDataSuccess(Course course) {
                showNewCourseSuccessDialog(course);
            }
        });
    }

    private void showNewCourseSuccessDialog(Course course) {
        new MaterialDialog.Builder(this).title("新建课程成功")
                .content(R.string.newCourseSuccessHint, course.getId(), course.getName(), course.getYear(),
                        course.getTerm() == Constant.TERM_FIRST ? "上学期" : "下学期", course.getTotalPersonCount())
                .positiveText(R.string.complete).cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    private void refreshOperationButtonEnable() {
        String courseName = tilCourseName.getEditText().getText().toString();
        if (TextUtils.isEmpty(courseName)) {
            tilCoursePassword.getEditText().setEnabled(false);
            tilCoursePasswordRetype.getEditText().setEnabled(false);
            rgTerm.setEnabled(false);
        }
    }
}
