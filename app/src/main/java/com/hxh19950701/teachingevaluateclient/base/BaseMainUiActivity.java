package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.hxh19950701.teachingevaluateclient.R;

public abstract class BaseMainUiActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout dlPersonCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dlPersonCenter = (DrawerLayout) findViewById(R.id.dlPersonCenter);
        initDrawerToggle();
    }

    private void initDrawerToggle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (dlPersonCenter != null) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, dlPersonCenter, toolbar, R.string.app_name, R.string.app_name);
                drawerToggle.syncState();
                dlPersonCenter.addDrawerListener(drawerToggle);
            }
        }
    }
}
