package com.jenxsol.wakemesleepme.services;

import java.util.Set;

import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.jenxsol.wakemesleepme.AppPreferences;
import com.jenxsol.wakemesleepme.server.UdpServer;
import com.jenxsol.wakemesleepme.utils.PacketSupport;
import com.jenxsol.wakemesleepme.utils.QLog;

public class BroadcastAliveIntentService extends WakefulIntentService
{

    private static final String EXTRA_STOP = "extra_stop";

    private static final UdpServer server = UdpServer.get();
    private static final AppPreferences mPrefs = AppPreferences.get();

    /**
     * Get the intent to stop the server. shouldnt be nessary to call this but handy if you the user
     * wants to kill it for some reason
     * 
     * @param ctx
     * @return
     */
    public static final Intent getIntentStop(final Context ctx)
    {
        Intent i = new Intent(ctx, BroadcastAliveIntentService.class);
        i.putExtra(EXTRA_STOP, true);
        return i;
    }

    public BroadcastAliveIntentService()
    {
        this(BroadcastAliveIntentService.class.getSimpleName());
    }

    public BroadcastAliveIntentService(String name)
    {
        super(name);
    }

    @Override
    protected void doWakefulWork(Intent intent)
    {
        QLog.d("Doing BroadcastAlive on SeperateThread with WakeLock");
        if (intent.getBooleanExtra(EXTRA_STOP, false))
        {
            server.stop();
            return;
        }
        // server.start();

        // Loop through the saved addresses on the device
        Set<String> macAddresses = mPrefs.getWakeAddresses();
        for (String mac : macAddresses)
        {
            QLog.d("Wake Up: " + mac);
            server.sendPacket(PacketSupport.createWoLANPacket(mac));
        }

        server.sendPacket(PacketSupport.createAlivePacket());

        // server.sendPacket(PacketSupport.createWoLANPacket("C8:2A:14:2A:37:BA")); // Mine
        // server.sendPacket(PacketSupport.createWoLANPacket("30:85:A9:E1:D2:6D"));
        // server.sendPacket(PacketSupport.createWoLANPacket("58:B0:35:F2:DE:46")); // vals
    }

}