package com.jenxsol.wakemesleepme.receivers;

import com.jenxsol.wakemesleepme.utils.AlarmSupport;
import com.jenxsol.wakemesleepme.utils.QLog;
import com.jenxsol.wakemesleepme.utils.WiFiSupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        QLog.i("ConnectionChange: " + action);
        if (ConnectivityManager.CONNECTIVITY_ACTION.equalsIgnoreCase(action))
        {
            if (ConnectivityManager.TYPE_WIFI == intent.getIntExtra(
                    ConnectivityManager.EXTRA_NETWORK_TYPE, -1))
            {
                boolean isValidConnection = WiFiSupport.isWiFiConnected();
                QLog.d("Connection Changed");
                QLog.d("Connection Connected: " + isValidConnection);
                startBroadcastAlive(isValidConnection);
            }
        }
    }

    private void startBroadcastAlive(boolean start)
    {
        if (start)
            AlarmSupport.startBroadcastAliveService();
        else
            AlarmSupport.stopBroadcastAliveService();

    }

}
