package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.hxh19950701.teachingevaluateclient.Bean.Course;
import com.hxh19950701.teachingevaluateclient.Bean.CourseBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestParams;
import com.hxh19950701.teachingevaluateclient.base.BaseTextWatcher;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by hxh19950701 on 2016/7/2.
 */
public class StudentAddCourseActivity extends BaseActivity {

    protected Toolbar toolbar;
    protected TextInputLayout tilClassId;
    protected TextInputLayout tilClassPassword;
    protected CoordinatorLayout clAddCourse;
    protected Button btnAddCourse;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_student_add_course);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tilClassId = (TextInputLayout) findViewById(R.id.tilClassId);
        tilClassPassword = (TextInputLayout) findViewById(R.id.tilClassPassword);
        clAddCourse = (CoordinatorLayout) findViewById(R.id.clAddCourse);
        btnAddCourse = (Button) findViewById(R.id.btnAddCourse);
    }

    @Override
    protected void initListener() {
        TextWatcher textWatcher = new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshAddCourseButtonEnable();
            }
        };
        tilClassId.getEditText().addTextChangedListener(textWatcher);
        tilClassPassword.getEditText().addTextChangedListener(textWatcher);
        btnAddCourse.setOnClickListener(this);
    }

    @Override
    protected void initDate() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("添加课程");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        new MaterialDialog.Builder(this).title("成功添加课程").content(msg).cancelable(false)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setResult(1);
                        finish();
                    }
                }).show();
    }

    private void addCourse() {
        final MaterialDialog addCourseDialog = new MaterialDialog.Builder(this)
                .title("正在添加").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).show();
        final RequestParams requestParams = new BaseRequestParams();
        requestParams.addQueryStringParameter("action", "addCourse");
        requestParams.addQueryStringParameter("courseId", tilClassId.getEditText().getText().toString());
        requestParams.addQueryStringParameter("password", tilClassPassword.getEditText().getText().toString());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getCourseManager(),
                requestParams, new BaseRequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        CourseBean courseBean = gson.fromJson(responseInfo.result, CourseBean.class);
                        if (courseBean.isSuccess()) {
                            showAddCourseSuccessDialog(courseBean.getCourse());
                        } else {
                            SnackBarUtils.showLong(clAddCourse, "课程未找到，或已添加该课程");
                        }
                        addCourseDialog.dismiss();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        super.onFailure(e, s);
                        SnackBarUtils.showLong(clAddCourse, String.format(getString(R.string.connectServerFail), e.getExceptionCode()));
                        addCourseDialog.dismiss();
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddCourse:
                addCourse();
                break;
        }
    }

}
