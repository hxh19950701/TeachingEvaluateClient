package com.hxh19950701.teachingevaluateclient.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.event.StudentAddCourseSuccessfullyEvent;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;

public class StudentAddCourseActivity extends BaseActivity {

    private TextInputLayout tilClassId;
    private TextInputLayout tilClassPassword;
    private CoordinatorLayout clAddCourse;
    private Button btnAddCourse;

    private final TextWatcher textWatcher = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            refreshAddCourseButtonEnable();
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_student_add_course);
        tilClassId = (TextInputLayout) findViewById(R.id.tilClassId);
        tilClassPassword = (TextInputLayout) findViewById(R.id.tilCoursePasswordRetype);
        clAddCourse = (CoordinatorLayout) findViewById(R.id.clAddCourse);
        btnAddCourse = (Button) findViewById(R.id.btnNewCourse);
    }

    @Override
    protected void initListener() {
        tilClassId.getEditText().addTextChangedListener(textWatcher);
        tilClassPassword.getEditText().addTextChangedListener(textWatcher);
        btnAddCourse.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        refreshAddCourseButtonEnable();
    }

    private void refreshAddCourseButtonEnable() {
        btnAddCourse.setEnabled(tilClassId.getEditText().getText().length() != 0
                && tilClassPassword.getEditText().getText().length() != 0);
    }

    private void showAddCourseSuccessDialog(Course course) {
        StringBuilder msg = new StringBuilder("你已成功添加如下课程：\n")
                .append("课程名：").append(course.getName()).append("\n")
                .append("任课老师：").append(course.getTeacher().getName());
        new MaterialDialog.Builder(this).title("成功添加课程").content(msg)
                .cancelable(false).positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                }).show();
    }

    private void addCourse() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在添加").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).build();
        int courseId = Integer.valueOf(tilClassId.getEditText().getText().toString());
        String password = MD5Utils.encipher(tilClassPassword.getEditText().getText().toString());
        CourseApi.addCourse(courseId, password, new SimpleServiceCallback<StudentCourseInfo>(clAddCourse) {

            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onAfter() {
                dialog.dismiss();
            }

            @Override
            public void onGetDataSuccess(StudentCourseInfo info) {
                EventManager.postEvent(new StudentAddCourseSuccessfullyEvent(info));
                showAddCourseSuccessDialog(info.getCourse());
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCourse:
                addCourse();
                break;
        }
    }

}
