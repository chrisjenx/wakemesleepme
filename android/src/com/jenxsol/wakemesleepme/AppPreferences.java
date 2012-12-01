package com.jenxsol.wakemesleepme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences
{
    private static final String PREF_SERVER_ENABLED = "server_enabled";

    private static final String APP_SHARED_PREFS = "com.jenxsol.wakemesleepme.AppPrefs";
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;
    
    

    public AppPreferences(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        prefsEditor = appSharedPrefs.edit();
    }
    
    

    
    
}