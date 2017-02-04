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

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.TeacherCourseRecyclerViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseMainUiActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;

import java.util.List;

public class TeacherMainUiActivity extends BaseMainUiActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvCourse;
    private SwipeRefreshLayout srlCourseList;
    private FloatingActionButton fabNewCourse;
    private CoordinatorLayout clPersonCenter;
    private NavigationView nvDrawer;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_teacher_main_ui);
        rvCourse = (RecyclerView) findViewById(R.id.rvCourse);
        srlCourseList = (SwipeRefreshLayout) findViewById(R.id.srlCourseList);
        fabNewCourse = (FloatingActionButton) findViewById(R.id.fabNewCourse);
        clPersonCenter = (CoordinatorLayout) findViewById(R.id.clPersonCenter);
        nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
    }

    @Override
    protected void initListener() {
        nvDrawer.setNavigationItemSelectedListener(this);
        fabNewCourse.setOnClickListener(this);
        srlCourseList.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        rvCourse.setLayoutManager(new LinearLayoutManager(this));
        initCourse();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabNewCourse:
                startActivityForResult(new Intent(this, TeacherNewCourseActivity.class), 0);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {
        initCourse();
    }

    private void initCourse() {
        CourseApi.getTeacherCourseList(new SimpleServiceCallback<List<Course>>(clPersonCenter) {
            @Override
            public void onGetDataSuccessful(List<Course> courses) {
                rvCourse.setAdapter(new TeacherCourseRecyclerViewAdapter(courses));
            }
        });
    }
}
