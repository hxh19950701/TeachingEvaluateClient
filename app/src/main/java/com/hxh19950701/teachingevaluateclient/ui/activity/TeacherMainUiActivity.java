package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.TeacherCourseListViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseMainUiActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.CourseApi;

import java.util.List;

public class TeacherMainUiActivity extends BaseMainUiActivity {

    ListView lvCourse;
    SwipeRefreshLayout srlCourseList;
    FloatingActionButton fabNewCourse;
    CoordinatorLayout clPersonCenter;
    NavigationView nvDrawer;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_teacher_main_ui);
        lvCourse = (ListView) findViewById(R.id.lvCourse);
        srlCourseList = (SwipeRefreshLayout) findViewById(R.id.srlCourseList);
        fabNewCourse = (FloatingActionButton) findViewById(R.id.fabNewCourse);
        clPersonCenter = (CoordinatorLayout) findViewById(R.id.clPersonCenter);
        nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
    }

    @Override
    protected void initListener() {
        fabNewCourse.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        CourseApi.getTeacherCourseList(new SimpleServiceCallback<List<Course>>(clPersonCenter) {
            @Override
            public void onGetDataSuccess(List<Course> courses) {
                lvCourse.setAdapter(new TeacherCourseListViewAdapter(courses));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabNewCourse:
                startActivityForResult(new Intent(this, TeacherNewCourseActivity.class), 0);
                break;
        }
    }

}
