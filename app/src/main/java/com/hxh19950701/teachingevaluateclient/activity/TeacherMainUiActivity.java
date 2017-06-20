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

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.TeacherCourseRecyclerViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Code;
import com.hxh19950701.teachingevaluateclient.bean.response.Course;
import com.hxh19950701.teachingevaluateclient.bean.response.Teacher;
import com.hxh19950701.teachingevaluateclient.event.CreateCourseCompleteEvent;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.network.api.TeacherApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeacherMainUiActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rvCourse)
    /*package*/ RecyclerView rvCourse;
    @BindView(R.id.srlCourseList)
    /*package*/ SwipeRefreshLayout srlCourseList;
    @BindView(R.id.clPersonCenter)
    /*package*/ CoordinatorLayout clPersonCenter;
    @BindView(R.id.nvDrawer)
    /*package*/ NavigationView nvDrawer;
    @BindView(R.id.dlPersonCenter)
    /*package*/ DrawerLayout dlPersonCenter;

    private TextView tvName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_teacher_main_ui;
    }

    @Override
    protected void initView() {
        View headerView = nvDrawer.getHeaderView(0);
        tvName = ButterKnife.findById(headerView, R.id.tv1);
        ButterKnife.findById(headerView, R.id.tv2).setVisibility(View.GONE);
        rvCourse.setLayoutManager(new LinearLayoutManager(this));
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
        nvDrawer.getMenu().getItem(2).setVisible(true);
    }

    @Override
    protected void initListener() {
        nvDrawer.setNavigationItemSelectedListener(this);
        srlCourseList.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        initTeacherInfo();
        initCourse();
        startReceiveEvent();
    }

    private void initTeacherInfo() {
        TeacherApi.currentTeacher(new SimpleServiceCallback<Teacher>(clPersonCenter) {
            @Override
            public void onGetDataSuccessful(Teacher teacher) {
                if (teacher == null) {
                    IntentUtils.startActivity(TeacherMainUiActivity.this, TeacherInfoCompleteActivity.class,
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    tvName.setText(teacher.getName());
                }
            }
        });
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
            case R.id.action_batch_student:
                createCode();
                break;
            case R.id.action_logout:
                ActivityUtils.exitApp(this, "您已注销成功");
                break;
        }
        return true;
    }

    private void createCode() {
        MaterialDialog dialog = new MaterialDialog.Builder(this).content("正在创建...")
                .progress(true, 0).cancelable(false).build();
        TeacherApi.createCode(new SimpleServiceCallback<Code>(clPersonCenter, dialog) {
            @Override
            public void onGetDataSuccessful(Code code) {
                new MaterialDialog.Builder(TeacherMainUiActivity.this).title("成功生成注册码")
                        .content("注册码为：" + code.getCode() + "\n10分钟内有效。").positiveText("关闭").show();
            }
        });
    }

    @OnClick(R.id.fabCreateCourse)
    public void createCourse() {
        IntentUtils.startActivity(this, CreateCourseActivity.class);
    }

    @Override
    public void onRefresh() {
        initCourse();
    }

    private void initCourse() {
        CourseApi.getTeacherCourseList(new SimpleServiceCallback<List<Course>>(clPersonCenter, srlCourseList) {
            @Override
            public void onGetDataSuccessful(List<Course> courses) {
                rvCourse.setAdapter(new TeacherCourseRecyclerViewAdapter(courses));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateCourseComplete(CreateCourseCompleteEvent event) {
        initCourse();
    }
}
