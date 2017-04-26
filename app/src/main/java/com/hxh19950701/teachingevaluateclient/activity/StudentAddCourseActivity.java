package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Course;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.event.StudentAddCourseSuccessfullyEvent;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class StudentAddCourseActivity extends BaseActivity {

    @BindView(R.id.clAddCourse)
    /*package*/ CoordinatorLayout clAddCourse;
    @BindView(R.id.etCourseId)
    /*package*/ EditText etCourseId;
    @BindView(R.id.etCoursePassword)
    /*package*/ EditText etCoursePassword;
    @BindView(R.id.btnAddCourse)
    /*package*/ Button btnAddCourse;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_student_add_course;
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        refreshAddCourseButtonEnable();
    }

    @OnTextChanged({R.id.etCourseId, R.id.etCoursePassword})
    public void refreshAddCourseButtonEnable() {
        btnAddCourse.setEnabled(etCourseId.length() != 0 && etCoursePassword.length() != 0);
    }

    private void showAddCourseSuccessfullyDialog(Course course) {
        String msg = new StringBuilder().append("你已成功添加如下课程：")
                .append("\n课程名：").append(course.getName())
                .append("\n任课老师：").append(course.getTeacher().getName())
                .toString();
        new MaterialDialog.Builder(this).title("成功添加课程").content(msg)
                .cancelable(false).positiveText(R.string.ok)
                .onPositive((dialog, which) -> finish())
                .show();
    }

    @OnClick(R.id.btnAddCourse)
    public void addCourse() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在添加").content("请稍后...")
                .cancelable(false).progress(true, 0).build();
        int courseId = Integer.valueOf(etCourseId.getText().toString());
        String password = MD5Utils.encipher(etCoursePassword.getText().toString());
        CourseApi.addCourse(courseId, password, new SimpleServiceCallback<StudentCourseInfo>(clAddCourse, dialog) {
            @Override
            public void onGetDataSuccessful(StudentCourseInfo info) {
                EventManager.postEvent(new StudentAddCourseSuccessfullyEvent(info));
                showAddCourseSuccessfullyDialog(info.getCourse());
            }
        });
    }

}
