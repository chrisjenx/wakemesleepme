/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Nov 28, 2012
 */
package com.jenxsol.wakemesleepme.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.jenxsol.wakemesleepme.WMSMApplication;
import com.jenxsol.wakemesleepme.consts.Iface;

/**
 * @author chris.jenkins
 */
public class UdpServer implements Iface
{

    private static UdpServer sSelf;

    public static final UdpServer get()
    {
        if (null == sSelf)
            sSelf = new UdpServer();
        return sSelf;
    }

    protected final WMSMApplication mApp = WMSMApplication.get();
    protected final WifiManager mWifiManager = (WifiManager) mApp
            .getSystemService(Context.WIFI_SERVICE);

    private DatagramSocket mSocket;

    private UdpServer()
    {
        start();
    }

    private void start()
    {
        if (!mWifiManager.isWifiEnabled())
        {
            return;
        }
        WifiInfo info = mWifiManager.getConnectionInfo();
        final int ipAddress = info.getIpAddress();
        final String ipAddressString = Formatter.formatIpAddress(ipAddress);
        try
        {
            mSocket = new DatagramSocket(UDP_PORT, InetAddress.getByName(ipAddressString));
            mSocket = new DatagramSocket(UDP_PORT);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    }
}
