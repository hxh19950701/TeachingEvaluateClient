package com.hxh19950701.teachingevaluateclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hxh19950701.teachingevaluateclient.service.EvaluateTargetUpdateService;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;

public class EvaluateItemUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentUtils.startService(context, EvaluateTargetUpdateService.class);
    }

}