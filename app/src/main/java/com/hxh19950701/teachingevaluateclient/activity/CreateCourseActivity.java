package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Course;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.event.CreateCourseCompleteEvent;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class CreateCourseActivity extends BaseActivity {

    @BindView(R.id.clNewCourse)
    /*package*/ CoordinatorLayout clNewCourse;
    @BindView(R.id.tilCourseName)
    /*package*/ TextInputLayout tilCourseName;
    @BindView(R.id.tilCoursePassword)
    /*package*/ TextInputLayout tilCoursePassword;
    @BindView(R.id.tilCoursePasswordRetype)
    /*package*/ TextInputLayout tilCoursePasswordRetype;
    @BindView(R.id.tilYear)
    /*package*/ TextInputLayout tilYear;
    @BindView(R.id.tilPersonCount)
    /*package*/ TextInputLayout tilPersonCount;
    @BindView(R.id.rgTerm)
    /*package*/ RadioGroup rgTerm;
    @BindView(R.id.btnCreateCourse)
    /*package*/ Button btnCreateCourse;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_course;
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
    }

    @OnTextChanged(R.id.etCoursePassword)
    public void checkCoursePassword() {

    }

    @OnClick(R.id.btnCreateCourse)
    public void showCreateCourseDialog() {
        String courseName = tilCourseName.getEditText().getText().toString();
        String coursePassword = tilCoursePassword.getEditText().getText().toString();
        String coursePasswordRetype = tilCoursePasswordRetype.getEditText().getText().toString();
        int year = Integer.parseInt(tilYear.getEditText().getText().toString());
        String term = rgTerm.getCheckedRadioButtonId() == R.id.rbTerm1 ? "上学期" : "下学期";
        int personCount = Integer.parseInt(tilPersonCount.getEditText().getText().toString());
        if (coursePassword.equals(coursePasswordRetype)) {
            new MaterialDialog.Builder(this).title("要创建这个课程吗")
                    .content(R.string.courseInfo, courseName, year, term, personCount)
                    .positiveText("创建").onPositive((dialog, which) -> createCourse())
                    .show();
        } else {
            SnackBarUtils.showLong(clNewCourse, "两次密码输入不一致");
        }
    }

    private void createCourse() {
        MaterialDialog dialog = new MaterialDialog.Builder(this).content("正在创建，请稍后...")
                .cancelable(false).progress(true, 0).build();
        String courseName = tilCourseName.getEditText().getText().toString();
        String coursePassword = MD5Utils.encipher(tilCoursePassword.getEditText().getText().toString());
        int year = Integer.parseInt(tilYear.getEditText().getText().toString());
        int term = rgTerm.getCheckedRadioButtonId() == R.id.rbTerm1
                ? Constant.TERM_FIRST : Constant.TERM_SECOND;
        int personCount = Integer.parseInt(tilPersonCount.getEditText().getText().toString());
        CourseApi.newCourse(courseName, coursePassword, year, term, personCount,
                new SimpleServiceCallback<Course>(clNewCourse, dialog) {
                    @Override
                    public void onGetDataSuccessful(Course course) {
                        EventManager.postEvent(new CreateCourseCompleteEvent(course));
                        showCreateCourseSuccessfullyDialog(course);
                    }
                });
    }

    private void showCreateCourseSuccessfullyDialog(Course course) {
        new MaterialDialog.Builder(this).title("新建课程成功")
                .content(R.string.newCourseSuccessHint, course.getId(), course.getName(), course.getYear(),
                        course.getTerm() == Constant.TERM_FIRST ? "上学期" : "下学期", course.getTotalPersonCount())
                .positiveText(R.string.complete).onPositive((dialog, which) -> finish())
                .cancelable(false).show();
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
