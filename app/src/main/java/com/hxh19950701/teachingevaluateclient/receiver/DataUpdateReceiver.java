package com.hxh19950701.teachingevaluateclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;
import com.hxh19950701.teachingevaluateclient.utils.ConnectivityUtils;

public class DataUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityUtils.isNetworkConnected()) {
            Context applicationContext = context.getApplicationContext();
            EvaluateTargetManager.init(applicationContext);
            DepartmentInfoManager.init(applicationContext);
        }
    }

}