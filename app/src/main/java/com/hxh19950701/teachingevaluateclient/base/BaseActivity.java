package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected abstract int getLayoutId();

    protected void initView() {

    }

    protected void initListener() {

    }

    protected void initData() {

    }

    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            setContentView(layoutId);
            ButterKnife.bind(this);
        }
        initToolbar();
        initView();
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
        if (!EventManager.isRegistered(this)) {
            EventManager.register(this);
        }
    }

    protected void stopReceiveEvent(){
        if (EventManager.isRegistered(this)) {
            EventManager.unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopReceiveEvent();
    }
}