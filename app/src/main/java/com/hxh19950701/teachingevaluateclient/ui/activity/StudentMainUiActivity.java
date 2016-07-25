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

import com.google.gson.Gson;
import com.hxh19950701.teachingevaluateclient.Bean.CourseListBean;
import com.hxh19950701.teachingevaluateclient.Bean.StudentInfoBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.StudentCourseAdapter;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestParams;
import com.hxh19950701.teachingevaluateclient.internet.NetServer;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

public class StudentMainUiActivity extends BaseActivity {
    protected Toolbar toolbar;
    protected FloatingActionButton fabAddCourse;
    protected ActionBarDrawerToggle drawerToggle;
    protected DrawerLayout dlPersonCenter;
    protected TextView tvName;
    protected TextView tvDepartment;
    protected TextView tvLogout;
    protected CoordinatorLayout clPersonCenter;
    protected ListView lvCourse;
    protected NavigationView nvDrawer;
    protected SwipeRefreshLayout srlCourseList;

    protected CourseListBean data;

    protected StudentCourseAdapter studentCourseAdapter;

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
                item.setChecked(true);
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
                Intent intent = new Intent(getApplication(), StudentEvaluateActivity.class);
                intent.putExtra("course", data.getCourseList().get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initDate() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("课堂评价系统-学生端");
        srlCourseList.setColorSchemeResources(R.color.colorAccent);
        initDrawerToggle();
        initUserInfo();
    }

    private void initStudentCourse() {
        srlCourseList.setRefreshing(true);
        BaseRequestParams requestParams = new BaseRequestParams();
        requestParams.addQueryStringParameter("action", "getCourseList");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getCourseManager(),
                requestParams, new BaseRequestCallBack<String>() {
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        data = gson.fromJson(responseInfo.result, CourseListBean.class);
                        if (data.isSuccess()) {
                            initCourseList();
                        } else {
                            NetServer.requireLoginAgain(StudentMainUiActivity.this, getString(R.string.identityExpired));
                        }
                        srlCourseList.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        super.onFailure(e, s);
                        srlCourseList.setRefreshing(false);
                        String.format(getString(R.string.connectServerFail), e.getExceptionCode());
                    }
                });
    }

    private void initCourseList() {
        studentCourseAdapter = new StudentCourseAdapter(data);
        lvCourse.setAdapter(studentCourseAdapter);
    }

    private void initUserInfo() {
        BaseRequestParams requestParams = new BaseRequestParams();
        requestParams.addQueryStringParameter("action", "currentUserInfo");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getStudentManagerURL(),
                requestParams, new BaseRequestCallBack<String>() {
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        StudentInfoBean studentInfoBean = gson.fromJson(responseInfo.result, StudentInfoBean.class);
                        if (studentInfoBean.isSuccess()) {
                            if (studentInfoBean.getStudent() == null) {
                                startActivity(new Intent(getApplication(), RegisterStudentActivity.class));
                                finish();
                            } else {
                                tvName.setText(studentInfoBean.getStudent().getName());
                                tvDepartment.setText(studentInfoBean.getStudent().getClazz().getSubject().getDepartment().getName());
                                initStudentCourse();
                            }
                        } else {
                            NetServer.requireLoginAgain(StudentMainUiActivity.this, getString(R.string.identityExpired));
                        }
                    }
                });
    }

    private void initDrawerToggle() {
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, dlPersonCenter, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        dlPersonCenter.addDrawerListener(drawerToggle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabAddCourse:

        }
    }

}
