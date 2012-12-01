/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Nov 28, 2012
 */
package com.jenxsol.wakemesleepme.server;

import java.net.DatagramPacket;

import com.jenxsol.wakemesleepme.WMSMApplication;
import com.jenxsol.wakemesleepme.consts.Iface;
import com.jenxsol.wakemesleepme.utils.QLog;

import android.content.Context;
import android.net.wifi.WifiManager;

import de.greenrobot.event.EventBus;

/**
 * @author chris.jenkins
 */
public class UdpServer implements Iface
{
    // Event bus
    private static EventBus sBus = EventBus.getDefault();

    private static UdpServer sSelf;

    public static final UdpServer get()
    {
        if (null == sSelf) sSelf = new UdpServer();
        return sSelf;
    }

    protected final WMSMApplication mApp = WMSMApplication.get();
    protected final WifiManager mWifiManager = (WifiManager) mApp
            .getSystemService(Context.WIFI_SERVICE);

    // Lock object
    private final Object mLock = new Object();
    private UdpServerThread mServerThread;

    private UdpServer()
    {
    }

    /**
     * Starts the server. Does nothing if the server is allready started
     */
    public void start()
    {
        synchronized (mLock)
        {
            if (isServerRunning()) return;
            if (mServerThread == null)
            {
                mServerThread = new UdpServerThread(null);
            }
            mServerThread.start();
        }
    }

    /**
     * Method to send data out
     * 
     * @param data
     */
    public void sendPacket(String data)
    {
        if (data == null) return;
        synchronized (mLock)
        {
            if (!isServerRunning()) return;
            if (null == mServerThread) return;
            mServerThread.addPacketToSend(data.getBytes(), data.length());
        }
    }

    /**
     * Send pre-formatted packet
     * 
     * @param packet
     */
    public void sendPacket(DatagramPacket packet)
    {
        if (packet == null) return;
        synchronized (mLock)
        {
            if (!isServerRunning()) return;
            if (null == mServerThread) return;
            mServerThread.addPacketToSend(packet);
        }
    }

    public void stop()
    {
        synchronized (mLock)
        {
            if (mServerThread != null && !mServerThread.isFinished())
            {
                mServerThread.stopServer();
                mServerThread = null;
                QLog.d("Stopping server");
            }
        }
    }

    /**
     * Is the server running
     * 
     * @return
     */
    private final boolean isServerRunning()
    {
        if (null == mServerThread) return false;
        if (mServerThread.isStarted() && mServerThread.isRunning()) return true;
        return false;
    }

    /**
     * Sticky event for if the server is running
     * 
     * @author chris
     * 
     */
    public static final class EventServerStarted
    {
        public EventServerStarted()
        {
        }
    }

    /**
     * Event for if the server was stopped, the sticky event is removed on the
     * server dieing
     * 
     * @author chris
     * 
     */
    public static final class EventServerStopped
    {
        public EventServerStopped()
        {
        }
    }
}
