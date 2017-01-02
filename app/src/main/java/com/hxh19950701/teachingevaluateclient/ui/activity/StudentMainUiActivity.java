package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.StudentCourseAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseMainUiActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Student;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.internet.NetServer;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.internet.api.StudentApi;

import java.util.List;

public class StudentMainUiActivity extends BaseMainUiActivity {

    protected FloatingActionButton fabAddCourse;
    protected ActionBarDrawerToggle drawerToggle;
    protected TextView tvName;
    protected TextView tvDepartment;
    protected TextView tvLogout;
    protected CoordinatorLayout clPersonCenter;
    protected ListView lvCourse;
    protected NavigationView nvDrawer;
    protected SwipeRefreshLayout srlCourseList;

    protected List<StudentCourseInfo> data;

    @Override
    public void initView() {
        setContentView(R.layout.activity_student_main_ui);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabAddCourse = (FloatingActionButton) findViewById(R.id.fabAddCourse);
        dlPersonCenter = (DrawerLayout) findViewById(R.id.dlPersonCenter);
        clPersonCenter = (CoordinatorLayout) findViewById(R.id.clPersonCenter);
        nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
        lvCourse = (ListView) findViewById(R.id.lvCourse);
        srlCourseList = (SwipeRefreshLayout) findViewById(R.id.srlCourseList);

        tvName = (TextView) nvDrawer.getHeaderView(0).findViewById(R.id.tvName);
        tvDepartment = (TextView) nvDrawer.getHeaderView(0).findViewById(R.id.tvDepartment);
    }

    @Override
    public void initListener() {
        fabAddCourse.setOnClickListener(this);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigationItemLogout:
                        NetServer.requireLoginAgain(StudentMainUiActivity.this, "注销成功。");
                        break;
                }
                dlPersonCenter.closeDrawers();
                return true;
            }
        });

        srlCourseList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initStudentCourse();
            }
        });

        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.get(position).getScore() >= 0) {

                } else {
                    Intent intent = new Intent(getApplication(), StudentEvaluateActivity.class);
                    intent.putExtra("course", data.get(position).getCourse().getId());
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void initData() {
        srlCourseList.setColorSchemeResources(R.color.colorAccent);
        initUserInfo();
        initStudentCourse();
    }

    private void initStudentCourse() {
        srlCourseList.setRefreshing(true);
        CourseApi.getStudentCourseList(new SimpleServiceCallback<List<StudentCourseInfo>>(clPersonCenter) {
            @Override
            public void onAfter() {
                srlCourseList.setRefreshing(false);
            }

            @Override
            public void onGetDataSuccess(List<StudentCourseInfo> infoList) {
                data = infoList;
                StudentCourseAdapter studentCourseAdapter = new StudentCourseAdapter(infoList);
                lvCourse.setAdapter(studentCourseAdapter);
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
        Intent intent = new Intent(this, RegisterStudentActivity.class);
        startActivity(intent);
        finish();
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
