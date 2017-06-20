package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Course;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.event.StudentCommentCourseCompleteEvent;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.network.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.hxh19950701.teachingevaluateclient.utils.TextInputLayoutUtils;
import com.hxh19950701.teachingevaluateclient.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class StudentCommentActivity extends BaseActivity {

    @BindView(R.id.clComment)
    /*package*/ CoordinatorLayout clComment;
    @BindView(R.id.tilComment)
    /*package*/ TextInputLayout tilComment;
    @BindView(R.id.btnCommit)
    /*package*/ Button btnCommit;

    private Course course = null;

    public static Intent newIntent(Context context, int courseId) {
        if (courseId < 0) {
            throw new IllegalArgumentException("Course id can not be negative : " + courseId);
        }
        return new Intent(context, StudentCommentActivity.class)
                .putExtra(Constant.KEY_COURSE_ID, courseId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_student_comment;
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        initCourseInfo();
        btnCommit.setEnabled(false);
    }

    private void initCourseInfo() {
        int courseId = getIntent().getIntExtra(Constant.KEY_COURSE_ID, -1);
        if (courseId > 0) {
            CourseApi.getCourse(courseId, new SimpleServiceCallback<Course>(clComment) {
                @Override
                public void onGetDataSuccessful(Course course) {
                    StudentCommentActivity.this.course = course;
                }
            });
        } else {
            SnackBarUtils.showLongPost(clComment, "初始化失败，非法的的启动参数。");
        }
    }

    @OnTextChanged(R.id.etComment)
    public void checkComment(){
        if (tilComment.getEditText().length() < 5) {
            tilComment.setError("评论字数至少5个字，最长120字");
        } else {
            TextInputLayoutUtils.setErrorEnabled(tilComment, false);
            btnCommit.setEnabled(course != null);
        }
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this).content("确定不提交评论吗？")
                .positiveText("确定").onPositive((dialog, which) -> finish())
                .negativeText(R.string.cancel)
                .show();
    }

    @OnClick(R.id.btnCommit)
    public void commitComment() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在注册").content("请稍后...")
                .cancelable(false).progress(true, 0).build();
        String comment = tilComment.getEditText().getText().toString();
        EvaluateApi.commentCourse(course.getId(), comment, new SimpleServiceCallback<StudentCourseInfo>(clComment, dialog) {
            @Override
            public void onGetDataSuccessful(StudentCourseInfo infoList) {
                ToastUtils.show("评论提交成功");
                EventManager.postEvent(new StudentCommentCourseCompleteEvent(course, comment));
                finish();
            }
        });
    }

}