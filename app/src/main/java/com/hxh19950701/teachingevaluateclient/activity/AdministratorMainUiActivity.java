package com.hxh19950701.teachingevaluateclient.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseMainUiActivity;

public class AdministratorMainUiActivity extends BaseMainUiActivity {

    private CoordinatorLayout clPersonCenter;
    private NavigationView nvDrawer;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_administrator_main_ui);
        clPersonCenter = (CoordinatorLayout) findViewById(R.id.clPersonCenter);
        nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
        dlPersonCenter = (DrawerLayout) findViewById(R.id.dlPersonCenter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
