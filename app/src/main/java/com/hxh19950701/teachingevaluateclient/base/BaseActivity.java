package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    public abstract void onClick(View view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initToolbar();
        initListener();
        initData();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    protected void displayHomeAsUp() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void startReceiveEvent() {
        EventBus defaultEventBus = EventBus.getDefault();
        if (!defaultEventBus.isRegistered(this)) {
            defaultEventBus.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus defaultEventBus = EventBus.getDefault();
        if (defaultEventBus.isRegistered(this)) {
            defaultEventBus.unregister(this);
        }
    }
}