package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Course;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.utils.CourseUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import butterknife.BindView;

public class CourseInfoActivity extends BaseActivity {

    public static Intent newIntent(Context context, int courseId) {
        if (courseId < 0) {
            throw new IllegalArgumentException("Course id can not be negative : " + courseId);
        }
        return new Intent(context, CourseInfoActivity.class)
                .putExtra(Constant.KEY_COURSE_ID, courseId);
    }

    public static Intent newIntent(Context context, @NonNull Course course) {
        return new Intent(context, CourseInfoActivity.class)
                .putExtra(Constant.KEY_COURSE, course);
    }

    @BindView(R.id.clCourseInfo)
    /*package*/ CoordinatorLayout clCourseInfo;
    @BindView(R.id.tvCourseId)
    /*package*/ TextView tvCourseId;
    @BindView(R.id.tvCourseName)
    /*package*/ TextView tvCourseName;
    @BindView(R.id.tvCourseTime)
    /*package*/ TextView tvCourseTime;
    @BindView(R.id.tvStudentCount)
    /*package*/ TextView tvStudentCount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_info;
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        initCourse();
    }

    private void initCourse() {
        Intent intent = getIntent();
        Course course = (Course) intent.getSerializableExtra(Constant.KEY_COURSE);
        if (course != null) {
            showCourseInfo(course);
        } else {
            int courseId = intent.getIntExtra(Constant.KEY_COURSE_ID, -1);
            if (courseId < 0) {
                SnackBarUtils.showLongPost(clCourseInfo, "初始化失败，非法的的启动参数。");
                return;
            }
            CourseApi.getCourse(courseId, new SimpleServiceCallback<Course>(clCourseInfo) {
                @Override
                public void onGetDataSuccessful(Course course) {
                    showCourseInfo(course);
                }
            });
        }
    }

    private void showCourseInfo(Course course) {
        tvCourseId.setText(String.valueOf(course.getId()));
        tvCourseName.setText(course.getName());
        tvCourseTime.setText(CourseUtils.formatCourseTime(course.getYear(), course.getTerm()));
        tvStudentCount.setText(String.valueOf(course.getTotalPersonCount()));
    }
}