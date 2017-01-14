package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.hxh19950701.teachingevaluateclient.network.NetServer;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.network.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;

import java.util.List;

public class StudentMainUiActivity extends BaseMainUiActivity implements SwipeRefreshLayout.OnRefreshListener {

    protected FloatingActionButton fabAddCourse;
    protected ActionBarDrawerToggle drawerToggle;
    protected TextView tvName;
    protected TextView tvDepartment;
    protected TextView tvLogout;
    protected CoordinatorLayout clPersonCenter;
    protected RecyclerView rvCourse;
    protected NavigationView nvDrawer;
    protected SwipeRefreshLayout srlCourseList;

    @Override
    public void initView() {
        setContentView(R.layout.activity_student_main_ui);
        fabAddCourse = (FloatingActionButton) findViewById(R.id.fabAddCourse);
        dlPersonCenter = (DrawerLayout) findViewById(R.id.dlPersonCenter);
        clPersonCenter = (CoordinatorLayout) findViewById(R.id.clPersonCenter);
        nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
        rvCourse = (RecyclerView) findViewById(R.id.rvCourse);
        srlCourseList = (SwipeRefreshLayout) findViewById(R.id.srlCourseList);

        tvName = (TextView) nvDrawer.getHeaderView(0).findViewById(R.id.tvName);
        tvDepartment = (TextView) nvDrawer.getHeaderView(0).findViewById(R.id.tvDepartment);
    }

    @Override
    public void initListener() {
        fabAddCourse.setOnClickListener(this);
        nvDrawer.setNavigationItemSelectedListener(this);
        srlCourseList.setOnRefreshListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigationItemLogout:
                NetServer.requireLoginAgain(this, "注销成功。");
                break;
        }
        dlPersonCenter.closeDrawers();
        return true;
    }

    @Override
    public void initData() {
        rvCourse.setLayoutManager(new LinearLayoutManager(this));
        srlCourseList.setColorSchemeResources(R.color.colorAccent);
        initUserInfo();
        initStudentCourse();
    }

    @Override
    public void onRefresh() {
        initStudentCourse();
    }

    private void initStudentCourse() {
        CourseApi.getStudentCourseList(new SimpleServiceCallback<List<StudentCourseInfo>>(clPersonCenter) {

            @Override
            public void onStart() {
                srlCourseList.setRefreshing(true);
            }

            @Override
            public void onAfter() {
                srlCourseList.setRefreshing(false);
            }

            @Override
            public void onGetDataSuccess(List<StudentCourseInfo> infoList) {
                StudentCourseRecyclerViewAdapter studentCourseRecyclerViewAdapter = new StudentCourseRecyclerViewAdapter(infoList);
                rvCourse.setAdapter(studentCourseRecyclerViewAdapter);
            }
        });
    }

    private void initUserInfo() {
        StudentApi.currentStudent(new SimpleServiceCallback<Student>(clPersonCenter) {
            @Override
            public void onGetDataSuccess(Student student) {
                if (student == null) {
                    requireFillInfo();
                } else {
                    setInfo(student.getName(), student.getClazz().getSubject().getDepartment().getName());
                }
            }
        });
    }

    private void requireFillInfo() {
        IntentUtils.startActivity(this, RegisterStudentActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void setInfo(String name, String otherInfo) {
        tvName.setText(name);
        tvDepartment.setText(otherInfo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAddCourse:
                startActivityForResult(new Intent(this, StudentAddCourseActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == 1) {
            initStudentCourse();
        }
    }

}
