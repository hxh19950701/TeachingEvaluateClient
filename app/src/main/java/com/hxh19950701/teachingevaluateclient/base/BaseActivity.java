package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    
    protected abstract void initView();
    protected abstract void initListener();
    protected abstract void initData();
    public abstract void onClick(View view);

    protected Toolbar toolbar;

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        CharSequence title = getTitle();
        if (toolbar != null) {
            if (TextUtils.isEmpty(title)) {
                toolbar.setTitle(title);
            }
            setSupportActionBar(toolbar);
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