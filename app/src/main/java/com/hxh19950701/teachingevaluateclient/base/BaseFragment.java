package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxh19950701.teachingevaluateclient.manager.EventManager;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected abstract int getLayoutId();

    protected void initView() {

    }

    protected void initListener() {

    }

    protected void initData() {

    }

    public void onClick(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            View view = inflater.inflate(layoutId, container, false);
            ButterKnife.bind(this, view);
            initView();
            return view;
        }
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();
    }

    protected View findViewById(@IdRes int resId) {
        return getView() == null ? null : getView().findViewById(resId);
    }

    protected void startReceiveEvent() {
        if (!EventManager.isRegistered(this)) {
            EventManager.register(this);
        }
    }

    protected void stopReceiveEvent() {
        if (EventManager.isRegistered(this)) {
            EventManager.unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopReceiveEvent();
    }
}