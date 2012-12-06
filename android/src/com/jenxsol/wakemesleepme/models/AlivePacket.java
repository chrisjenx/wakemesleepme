/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Dec 2, 2012
 */
package com.jenxsol.wakemesleepme.models;


/**
 * @author chris.jenkins 
 */
public class AlivePacket
{

    private String mac;
    private String type;
    private String status;

    /**
     * @return the mac
     */
    public final String getMac()
    {
        return mac;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public final void setMac(String mac)
    {
        this.mac = mac;
    }

    /**
     * @return the type
     */
    public final String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public final void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return the status
     */
    public final String getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public final void setStatus(String status)
    {
        this.status = status;
    }

}
