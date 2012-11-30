package com.jenxsol.wakemesleepme.receivers;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.jenxsol.wakemesleepme.services.BroadcastAliveIntentService;
import com.jenxsol.wakemesleepme.utils.QLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastAliveReceiver extends BroadcastReceiver
{

    public static final String ACTION = "com.jenxsol.wakemesleepme.receivers.BroadcastAliveReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        QLog.d("Start  BroadcastAliveIntentService");
        WakefulIntentService.sendWakefulWork(context, BroadcastAliveIntentService.class);
    }

}
