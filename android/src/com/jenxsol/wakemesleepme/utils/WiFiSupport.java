package com.jenxsol.wakemesleepme.utils;

import java.io.IOException;
import java.net.InetAddress;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.jenxsol.wakemesleepme.WMSMApplication;

public class WiFiSupport
{

    private static final WifiManager sWifi = (WifiManager) WMSMApplication.get().getSystemService(
            Context.WIFI_SERVICE);
    private static final ConnectivityManager sConn = (ConnectivityManager) WMSMApplication.get()
            .getSystemService(Context.CONNECTIVITY_SERVICE);

    /**
     * Can return null, which means it failed to get dhcp, meaning wifi could be off or not yet
     * connected
     * 
     * @return
     * @throws IOException
     */
    public static final InetAddress getBroadcastAddress() throws IOException
    {
        DhcpInfo dhcp = sWifi.getDhcpInfo();
        if (dhcp == null)
            return null;

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    public static final String getMacAddress()
    {
        WifiInfo info = sWifi.getConnectionInfo();
        if (info != null)
        {
            String mac = info.getMacAddress();
            return mac;
        }
        return null;

    }

    /**
     * Is wifi connected to a network
     * 
     * @return true if connected with an address
     */
    public static final boolean isWiFiConnected()
    {
        if (null != sConn)
        {
            NetworkInfo wifiInfo = sConn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo == null)
                return false;
            return wifiInfo.isConnected();
        }
        return false;
    }

    private WiFiSupport()
    {

    }
}
