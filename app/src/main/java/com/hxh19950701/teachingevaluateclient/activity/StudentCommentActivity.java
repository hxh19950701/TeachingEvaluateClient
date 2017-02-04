package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.network.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

public class StudentCommentActivity extends BaseActivity {

    private CoordinatorLayout clComment;
    private TextInputLayout tilComment;
    private Button btnCommit;

    private int courseId = -1;

    public static Intent newIntent(Context context, int courseId) {
        if (courseId < 0) {
            throw new IllegalArgumentException("Course id can not be negative : " + courseId);
        }
        Intent intent = new Intent(context, StudentCommentActivity.class);
        intent.putExtra(Constant.KEY_COURSE_ID, courseId);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_student_comment);
        tilComment = (TextInputLayout) findViewById(R.id.tilComment);
        btnCommit = (Button) findViewById(R.id.btnCommit);
        clComment = (CoordinatorLayout) findViewById(R.id.clComment);
    }

    @Override
    protected void initListener() {
        btnCommit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        courseId = getIntent().getIntExtra(Constant.KEY_COURSE_ID, -1);
        if (courseId > 0) {
            initCourseInfo();
        } else {
            SnackBarUtils.showLongPost(clComment, "初始化失败，非法的的启动参数。");
        }
    }

    private void initCourseInfo() {
        CourseApi.getCourse(courseId, new SimpleServiceCallback<Course>(clComment) {
            @Override
            public void onGetDataSuccessful(Course course) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCommit:
                commitComment();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this).content("确定不提交建议吗？")
                .positiveText("确定").negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

    private void commitComment() {
        MaterialDialog dialog = new MaterialDialog.Builder(this).title("正在注册").content("请稍后...")
                .cancelable(false).progressIndeterminateStyle(false).progress(true, 0).build();
        String comment = tilComment.getEditText().getText().toString();
        EvaluateApi.commentCourse(courseId, comment, new SimpleServiceCallback<StudentCourseInfo>(clComment, dialog) {
            @Override
            public void onGetDataSuccessful(StudentCourseInfo infoList) {
                finish();
            }
        });
    }
}
