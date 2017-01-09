package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;

public class HelpActivity extends BaseActivity {

    protected WebView wvHelp;
    protected CoordinatorLayout clHelp;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_help);
        wvHelp = (WebView) findViewById(R.id.wvHelp);
        clHelp = (CoordinatorLayout) findViewById(R.id.clHelp);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wvHelp.loadUrl("http://3g.qq.com");
        wvHelp.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvHelp.canGoBack()) {
            wvHelp.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
