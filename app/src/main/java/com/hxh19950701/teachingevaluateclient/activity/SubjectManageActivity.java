package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.SubjectAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Subject;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.DepartmentApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.List;

import butterknife.BindView;

public class SubjectManageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static Intent newIntent(Context context, int departmentId) {
        if (departmentId <= 0) {
            throw new IllegalArgumentException("department id can not be negative!");
        }
        return new Intent(context, SubjectManageActivity.class)
                .putExtra(Constant.KEY_DEPARTMENT, departmentId);
    }

    @BindView(R.id.rvSubject)
    /*package*/ RecyclerView rvSubject;
    @BindView(R.id.clSubjectManage)
    /*package*/ CoordinatorLayout clSubjectManage;
    @BindView(R.id.srlSubjectManage)
    SwipeRefreshLayout srlSubjectManage;

    private int departmentId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subject_manage;
    }


    @Override
    protected void initView() {
        srlSubjectManage.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void initListener() {
        srlSubjectManage.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        initSubject();
    }

    private void initSubject() {
        departmentId = getIntent().getIntExtra(Constant.KEY_DEPARTMENT, -1);
        if (departmentId > 0) {
            DepartmentApi.getSubjectListByDepartment(departmentId, new SimpleServiceCallback<List<Subject>>(clSubjectManage, srlSubjectManage) {
                @Override
                public void onGetDataSuccessful(List<Subject> subjects) {
                    rvSubject.setLayoutManager(new LinearLayoutManager(SubjectManageActivity.this));
                    rvSubject.setAdapter(new SubjectAdapter(subjects));
                }
            });
        } else {
            SnackBarUtils.showLongPost(clSubjectManage, "初始化失败，非法的的启动参数。");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create) {
            showCreateSubjectDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCreateSubjectDialog() {
        new MaterialDialog.Builder(this).title("输入专业名称")
                .inputType(InputType.TYPE_CLASS_TEXT).input("", "", false, null)
                .positiveText("创建").onPositive((dialog, which) -> create(dialog.getInputEditText().getText().toString())).show();
    }

    private void create(String subjectName) {
        MaterialDialog dialog = new MaterialDialog.Builder(this).content("正在创建...")
                .progress(true, 0).cancelable(false).build();
        DepartmentApi.createSubject(departmentId, subjectName, new SimpleServiceCallback<Subject>(clSubjectManage, dialog) {
            @Override
            public void onGetDataSuccessful(Subject subject) {
                SnackBarUtils.showLong(clSubjectManage, "创建成功");
                DepartmentInfoManager.init(getApplicationContext());
            }
        });
    }

    @Override
    public void onRefresh() {
        initSubject();
    }
}
