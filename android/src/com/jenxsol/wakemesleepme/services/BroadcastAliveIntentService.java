package com.jenxsol.wakemesleepme.services;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.jenxsol.wakemesleepme.consts.Packets;
import com.jenxsol.wakemesleepme.server.UdpServer;
import com.jenxsol.wakemesleepme.utils.QLog;

import android.content.Context;
import android.content.Intent;

public class BroadcastAliveIntentService extends WakefulIntentService
{
    private static final String EXTRA_STOP = "extra_stop";

    private static final UdpServer server = UdpServer.get();

    /**
     * Get the intent to stop the server. shouldnt be nessary to call this but
     * handy if you the user wants to kill it for some reason
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
        server.start();
        server.sendPacket(Packets.sAliveString);
    }

}