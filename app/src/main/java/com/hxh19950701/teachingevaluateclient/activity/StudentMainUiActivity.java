package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.StudentCourseRecyclerViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Student;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentMainUiActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.clPersonCenter)
    /*package*/ CoordinatorLayout clPersonCenter;
    @BindView(R.id.rvCourse)
    /*package*/ RecyclerView rvCourse;
    @BindView(R.id.nvDrawer)
    /*package*/ NavigationView nvDrawer;
    @BindView(R.id.srlCourseList)
    /*package*/ SwipeRefreshLayout srlCourseList;
    @BindView(R.id.dlPersonCenter)
    /*package*/ DrawerLayout dlPersonCenter;

    private TextView tvName;
    private TextView tvDepartment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_student_main_ui;
    }

    @Override
    public void initView() {
        View headerView = nvDrawer.getHeaderView(0);
        tvName = ButterKnife.findById(headerView, R.id.tv1);
        tvDepartment = ButterKnife.findById(headerView, R.id.tv2);

        rvCourse.setLayoutManager(new LinearLayoutManager(this));
        srlCourseList.setColorSchemeResources(R.color.colorAccent);

        initDrawerToggle();
    }

    private void initDrawerToggle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (dlPersonCenter != null) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                        this, dlPersonCenter, toolbar, R.string.app_name, R.string.app_name);
                drawerToggle.syncState();
                dlPersonCenter.addDrawerListener(drawerToggle);
            }
        }
    }

    @Override
    public void initListener() {
        nvDrawer.setNavigationItemSelectedListener(this);
        srlCourseList.setOnRefreshListener(this);
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
                    IntentUtils.startActivity(StudentMainUiActivity.this, StudentInfoCompleteActivity.class,
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    tvName.setText(student.getName());
                    tvDepartment.setText(student.getClazz().getSubject().getDepartment().getName());
                }
            }
        });
    }

    @OnClick(R.id.fabAddCourse)
    public void addCourse() {
        IntentUtils.startActivity(this, StudentAddCourseActivity.class);
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