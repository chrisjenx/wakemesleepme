package com.jenxsol.wakemesleepme.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.jenxsol.wakemesleepme.WMSMApplication;
import com.jenxsol.wakemesleepme.receivers.BroadcastAliveReceiver;
import com.jenxsol.wakemesleepme.services.BroadcastAliveIntentService;

public class AlarmSupport
{

    private static final long INTERVAL_15_SEC = 15 * 1000;
    private static final long INTERVAL_HALF_MIN = 30 * 1000;
    private static final long INTERVAL_ONE_MIN = 2 * INTERVAL_HALF_MIN;

    private final static WMSMApplication sApp = WMSMApplication.get();
    private final static AlarmManager mAlarm = (AlarmManager) sApp
            .getSystemService(Context.ALARM_SERVICE);

    private final static PendingIntent sBroadcastAlive;
    static
    {
        Intent i = new Intent(BroadcastAliveReceiver.ACTION);
        sBroadcastAlive = PendingIntent.getBroadcast(sApp, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static final void startBroadcastAliveService()
    {
        stopBroadcastAliveService();
        mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, INTERVAL_15_SEC, sBroadcastAlive);
    }

    public static final void stopBroadcastAliveService()
    {
        WakefulIntentService.sendWakefulWork(sApp, BroadcastAliveIntentService.getIntentStop(sApp));
        mAlarm.cancel(sBroadcastAlive);
    }

    private AlarmSupport()
    {
    }

}
