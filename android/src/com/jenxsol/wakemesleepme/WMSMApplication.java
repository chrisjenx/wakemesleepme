/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Nov 28, 2012
 */
package com.jenxsol.wakemesleepme;

import android.app.Application;


/**
 * @author chris.jenkins 
 */
public class WMSMApplication extends Application
{

    private static WMSMApplication sSelf;

    public static final WMSMApplication get()
    {
        return sSelf;
    }

    public WMSMApplication()
    {
        sSelf = this;
    }


}
