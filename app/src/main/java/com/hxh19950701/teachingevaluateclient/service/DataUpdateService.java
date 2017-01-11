package com.hxh19950701.teachingevaluateclient.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;
import com.hxh19950701.teachingevaluateclient.receiver.DataUpdateReceiver;

public class DataUpdateService extends Service {

    private BroadcastReceiver receiver = new DataUpdateReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EvaluateTargetManager.init(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}