package com.jenxsol.wakemesleepme.receivers;

import com.jenxsol.wakemesleepme.utils.AlarmSupport;
import com.jenxsol.wakemesleepme.utils.WiFiSupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (WiFiSupport.isWiFiConnected())
        {
            AlarmSupport.startBroadcastAliveService();
        }
    }

}
