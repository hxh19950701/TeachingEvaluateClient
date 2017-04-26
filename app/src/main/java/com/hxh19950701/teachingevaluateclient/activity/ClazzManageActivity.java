package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.ClazzAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Clazz;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.dialog.CreateClazzDialog;
import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.DepartmentApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.List;

import butterknife.BindView;

public class ClazzManageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static Intent newIntent(Context context, int subjectId) {
        if (subjectId <= 0) {
            throw new IllegalArgumentException("subject id can not be negative!");
        }
        return new Intent(context, ClazzManageActivity.class)
                .putExtra(Constant.KEY_SUBJECT, subjectId);
    }

    @BindView(R.id.rvClazz)
    /*package*/ RecyclerView rvClazz;
    @BindView(R.id.clClazzManage)
    /*package*/ CoordinatorLayout clClazzManage;
    @BindView(R.id.srlClazzManage)
    /*package*/ SwipeRefreshLayout srlClazzManage;

    private int subjectId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clazz_manage;
    }

    @Override
    protected void initView() {
        srlClazzManage.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void initListener() {
        srlClazzManage.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        initClazz();
    }

    private void initClazz() {
        subjectId = getIntent().getIntExtra(Constant.KEY_SUBJECT, -1);
        if (subjectId > 0) {
            DepartmentApi.getClazzListBySubject(subjectId, new SimpleServiceCallback<List<Clazz>>(clClazzManage, srlClazzManage) {
                @Override
                public void onGetDataSuccessful(List<Clazz> clazzs) {
                    rvClazz.setLayoutManager(new LinearLayoutManager(ClazzManageActivity.this));
                    rvClazz.setAdapter(new ClazzAdapter(clazzs));
                }
            });

        } else {
            SnackBarUtils.showLongPost(clClazzManage, "初始化失败，非法的的启动参数。");
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
        new CreateClazzDialog(this, new CreateClazzDialog.OnPositiveListener() {
            @Override
            public void onPositive(String clazzName, int year) {
                create(clazzName, year);
            }
        }).show();
    }

    private void create(String clazzName, int year) {
        MaterialDialog dialog = new MaterialDialog.Builder(this).content("正在创建...")
                .progress(true, 0).cancelable(false).build();
        DepartmentApi.createClazz(subjectId, year, clazzName, new SimpleServiceCallback<Clazz>(clClazzManage, dialog) {
            @Override
            public void onGetDataSuccessful(Clazz clazz) {
                SnackBarUtils.showLong(clClazzManage, "创建成功");
                initClazz();
                DepartmentInfoManager.init(getApplicationContext());
            }
        });
    }

    @Override
    public void onRefresh() {
        initClazz();
    }
}