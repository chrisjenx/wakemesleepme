/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Dec 2, 2012
 */
package com.jenxsol.wakemesleepme.models;

import java.net.DatagramPacket;

/**
 * @author chris.jenkins
 */
public class WrappedPacket
{

    private DatagramPacket packet;
    private long time;
    private String ipAddress;
    private String macAddress;

    /**
     * @return the packet
     */
    public final DatagramPacket getPacket()
    {
        return packet;
    }

    /**
     * @param packet
     *            the packet to set
     */
    public final void setPacket(DatagramPacket packet)
    {
        this.packet = packet;
    }

    /**
     * @return the time
     */
    public final long getTime()
    {
        return time;
    }

    /**
     * @param time
     *            the time to set
     */
    public final void setTime(long time)
    {
        this.time = time;
    }

    /**
     * @return the ipAddress
     */
    public final String getIpAddress()
    {
        return ipAddress;
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     */
    public final void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the macAddress
     */
    public String getMacAddress()
    {
        return macAddress;
    }

    /**
     * @param macAddress
     *            the macAddress to set
     */
    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof WrappedPacket))
            return false;
        WrappedPacket other = (WrappedPacket) obj;
        if (ipAddress == null)
        {
            if (other.ipAddress != null)
                return false;
        }
        else if (!ipAddress.equals(other.ipAddress))
            return false;
        return true;
    }

}
