package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.StudentCourseRecyclerViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseMainUiActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Student;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.event.StudentAddCourseSuccessfullyEvent;
import com.hxh19950701.teachingevaluateclient.event.StudentCommentCourseCompleteEvent;
import com.hxh19950701.teachingevaluateclient.event.StudentEvaluateCourseCompleteEvent;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.network.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class StudentMainUiActivity extends BaseMainUiActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FloatingActionButton fabAddCourse;
    private TextView tvName;
    private TextView tvDepartment;
    private CoordinatorLayout clPersonCenter;
    private RecyclerView rvCourse;
    private NavigationView nvDrawer;
    private SwipeRefreshLayout srlCourseList;

    @Override
    public void initView() {
        setContentView(R.layout.activity_student_main_ui);
        fabAddCourse = (FloatingActionButton) findViewById(R.id.fabAddCourse);
        clPersonCenter = (CoordinatorLayout) findViewById(R.id.clPersonCenter);
        nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
        rvCourse = (RecyclerView) findViewById(R.id.rvCourse);
        rvCourse.setLayoutManager(new LinearLayoutManager(this));
        srlCourseList = (SwipeRefreshLayout) findViewById(R.id.srlCourseList);
        srlCourseList.setColorSchemeResources(R.color.colorAccent);

        View headerView = nvDrawer.getHeaderView(0);
        tvName = (TextView) headerView.findViewById(R.id.tvName);
        tvDepartment = (TextView) headerView.findViewById(R.id.tvDepartment);
    }

    @Override
    public void initListener() {
        fabAddCourse.setOnClickListener(this);
        nvDrawer.setNavigationItemSelectedListener(this);
        srlCourseList.setOnRefreshListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        dlPersonCenter.closeDrawers();
        switch (item.getItemId()) {
            case R.id.action_edit_info:
                IntentUtils.startActivity(this, StudentEditInfoActivity.class);
                break;
            case R.id.action_modify_password:
                IntentUtils.startActivity(this, ModifyPasswordActivity.class);
                break;
            case R.id.action_logout:
                ActivityUtils.exitApp(this, "您已注销成功");
                break;
        }
        return true;
    }

    @Override
    public void initData() {
        initUserInfo();
        initStudentCourse();
        startReceiveEvent();
    }

    @Override
    public void onRefresh() {
        initStudentCourse();
    }

    private void initStudentCourse() {
        CourseApi.getStudentCourseList(new SimpleServiceCallback<List<StudentCourseInfo>>(clPersonCenter, srlCourseList) {
            @Override
            public void onGetDataSuccessful(List<StudentCourseInfo> infoList) {
                rvCourse.setAdapter(new StudentCourseRecyclerViewAdapter(infoList));
            }
        });
    }

    private void initUserInfo() {
        StudentApi.currentStudent(new SimpleServiceCallback<Student>(clPersonCenter) {
            @Override
            public void onGetDataSuccessful(Student student) {
                if (student == null) {
                    IntentUtils.startActivity(StudentMainUiActivity.this, RegisterStudentActivity.class,
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    tvName.setText(student.getName());
                    tvDepartment.setText(student.getClazz().getSubject().getDepartment().getName());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAddCourse:
                IntentUtils.startActivity(this, StudentAddCourseActivity.class);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStudentAddCourseSuccessfully(StudentAddCourseSuccessfullyEvent event) {
        initStudentCourse();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStudentCommentCourseComplete(StudentCommentCourseCompleteEvent event) {
        initStudentCourse();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStudentEvaluateCourseComplete(StudentEvaluateCourseCompleteEvent event) {
        initStudentCourse();
    }
}