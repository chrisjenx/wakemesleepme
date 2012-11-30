package com.jenxsol.wakemesleepme.consts;

public class BuildConfig
{
    public static final BUILD sBuild = BUILD.DEV;

    // QLog
    public static final boolean B_LOGGING_ENABLED;
    // Bug tracking key
    public static final String B_BUGSENSE_KEY;

    static
    {
        switch (sBuild)
        {
            case PROD:
            case TEST:
            case DEV:
            default:
                B_LOGGING_ENABLED = true;
                B_BUGSENSE_KEY = "1859e647";
                break;
        }
    }

    private BuildConfig()
    {
    }

    public static enum BUILD
    {
        DEV, TEST, PROD;
    }
}
