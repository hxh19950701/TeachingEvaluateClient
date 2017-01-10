package com.hxh19950701.teachingevaluateclient.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    public abstract void initListener();
    public abstract void initData();
    public abstract void onClick(View v);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();
    }

    protected void startReceiveEvent() {
        EventBus defaultEventBus = EventBus.getDefault();
        if (!defaultEventBus.isRegistered(this)) {
            defaultEventBus.register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus defaultEventBus = EventBus.getDefault();
        if (defaultEventBus.isRegistered(this)) {
            defaultEventBus.unregister(this);
        }
    }
}