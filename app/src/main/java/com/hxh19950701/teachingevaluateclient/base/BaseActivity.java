package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;

/**
 * Created by hxh19950701 on 2016/5/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initToolbar();
        initListener();
        initData();
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    public abstract void onClick(View view);

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

}