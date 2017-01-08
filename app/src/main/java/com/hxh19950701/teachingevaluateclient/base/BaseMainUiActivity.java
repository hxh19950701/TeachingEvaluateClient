package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.hxh19950701.teachingevaluateclient.R;

public abstract class BaseMainUiActivity extends BaseActivity {

    protected DrawerLayout dlPersonCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawerToggle();
        dlPersonCenter = (DrawerLayout) findViewById(R.id.dlPersonCenter);
    }

    private void initDrawerToggle() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (dlPersonCenter != null) {
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, dlPersonCenter, toolbar, R.string.app_name, R.string.app_name);
            drawerToggle.syncState();
            dlPersonCenter.addDrawerListener(drawerToggle);
        }
    }
}
