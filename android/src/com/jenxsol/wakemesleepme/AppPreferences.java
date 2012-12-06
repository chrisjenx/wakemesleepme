package com.jenxsol.wakemesleepme;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import de.greenrobot.event.EventBus;

public class AppPreferences
{

    private static final String APP_SHARED_PREFS = "com.jenxsol.wakemesleepme.AppPrefs";

    private static final String PREF_SERVER_ENABLED = "server_enabled";
    private static final String PREF_WAKE_MAC_ADDRESSES = "server_enabled";

    private static final EventBus sBus = EventBus.getDefault();
    private static AppPreferences sSelf;


    public static final AppPreferences get()
    {
        if (sSelf == null)
            sSelf = new AppPreferences(WMSMApplication.get());
        return sSelf;
    }

    private SharedPreferences mPrefs;
    private Editor mEdit;

    public AppPreferences(Context context)
    {
        this.mPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        mEdit = mPrefs.edit();
    }

    public void addWakeAddress(String macAddress)
    {
        Set<String> addresses = getWakeAddresses();
        addresses.add(macAddress);
        putWakeAddresses(addresses);
    }

    public void putWakeAddresses(Set<String> macAddresses)
    {
        mEdit.putStringSet(PREF_WAKE_MAC_ADDRESSES, macAddresses);
        mEdit.apply();
        sBus.post(new EventPrefsUpdated());
    }

    /**
     * get which mac addresses to wake
     * 
     * @author chris.jenkins
     * @return
     */
    public Set<String> getWakeAddresses()
    {
        return mPrefs.getStringSet(PREF_WAKE_MAC_ADDRESSES, new HashSet<String>());
    }

    public static final class EventPrefsUpdated
    {

        /**
         * @author chris.jenkins
         */
        public EventPrefsUpdated()
        {
        }
    }
}