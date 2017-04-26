package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.DepartmentAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Department;
import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.DepartmentApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.List;

import butterknife.BindView;

public class DepartmentManageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.clDepartmentManage)
    CoordinatorLayout clDepartmentManage;
    @BindView(R.id.rvDepartment)
    RecyclerView rvDepartment;
    @BindView(R.id.srlDepartmentManage)
    SwipeRefreshLayout srlDepartmentManage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_department_manage;
    }

    @Override
    protected void initView() {
        srlDepartmentManage.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void initListener() {
        srlDepartmentManage.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        initDepartment();

    }

    private void initDepartment() {
        DepartmentApi.getDepartmentList(new SimpleServiceCallback<List<Department>>(clDepartmentManage, srlDepartmentManage) {
            @Override
            public void onGetDataSuccessful(List<Department> departments) {
                rvDepartment.setLayoutManager(new LinearLayoutManager(DepartmentManageActivity.this));
                rvDepartment.setAdapter(new DepartmentAdapter(departments));
            }
        });
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
            showCreateDepartmentDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCreateDepartmentDialog() {
        new MaterialDialog.Builder(this)
                .title("输入系部名称").inputType(InputType.TYPE_CLASS_TEXT).input("", "", false, (dialog, input) -> {
        })
                .positiveText("创建").onPositive((dialog, which) -> create(dialog.getInputEditText().getText().toString()))
                .show();
    }

    private void create(String departmentName) {
        MaterialDialog dialog = new MaterialDialog.Builder(this).content("正在创建...")
                .progress(true, 0).cancelable(false).build();
        DepartmentApi.createDepartment(departmentName, new SimpleServiceCallback<Department>(clDepartmentManage, dialog) {
            @Override
            public void onGetDataSuccessful(Department department) {
                SnackBarUtils.showLong(clDepartmentManage, "创建成功");
                initDepartment();
                DepartmentInfoManager.init(getApplicationContext());
            }
        });
    }

    @Override
    public void onRefresh() {
        initDepartment();
    }
}