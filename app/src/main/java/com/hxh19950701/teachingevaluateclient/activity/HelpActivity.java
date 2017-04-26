package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;

public class HelpActivity extends BaseActivity {

    private CoordinatorLayout clHelp;
    private WebView wvHelp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initView() {
        wvHelp = (WebView) findViewById(R.id.wvHelp);
        clHelp = (CoordinatorLayout) findViewById(R.id.clHelp);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        wvHelp.getSettings().setJavaScriptEnabled(true);
        wvHelp.setWebViewClient(new WebViewClient());
        wvHelp.loadUrl("http://3g.qq.com");
    }

    @Override
    public void onClick(View view) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvHelp.canGoBack()) {
            wvHelp.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
