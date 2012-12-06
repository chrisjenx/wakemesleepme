/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Nov 28, 2012
 */
package com.jenxsol.wakemesleepme;

import android.app.Application;

import com.bugsense.trace.BugSenseHandler;
import com.jenxsol.wakemesleepme.consts.BuildConfig;
import com.jenxsol.wakemesleepme.utils.AlarmSupport;
import com.jenxsol.wakemesleepme.utils.WiFiSupport;

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

    @Override
    public void onCreate()
    {
        super.onCreate();
        BugSenseHandler.initAndStartSession(this, BuildConfig.B_BUGSENSE_KEY);

        // Check started
        if (WiFiSupport.isWiFiConnected())
        {
            AlarmSupport.startBroadcastAliveService();
        }

        // Add vals address
        AppPreferences.get().addWakeAddress("58:B0:35:F2:DE:46");
    }

}
