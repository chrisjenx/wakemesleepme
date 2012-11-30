package com.jenxsol.wakemesleepme.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.jenxsol.wakemesleepme.consts.BuildConfig;

import android.util.Log;

public class QLog
{

    public static final int NONE = 0;
    public static final int ERRORS_ONLY = 1;
    public static final int ERRORS_WARNINGS = 2;
    public static final int ERRORS_WARNINGS_INFO = 3;
    public static final int ERRORS_WARNINGS_INFO_DEBUG = 4;
    public static final int ALL = 5;

    public static final int LOGGING_LEVEL = (BuildConfig.B_LOGGING_ENABLED) ? ALL : ERRORS_ONLY;

    /*
     * For filtering app specific output
     */
    private static final String TAG = "wmsm";
    /*
     * So any important logs can be outputted in non filtered output also
     */
    private static final String TAG_GENERAL_OUTPUT = "QLog";

    static
    {
        i("Log class reloaded");
    }

    /**
     * @param obj
     * @param cause
     *            The exception which caused this error, may not be null
     */
    public static void e(final Object obj, final Throwable cause)
    {
        if (LOGGING_LEVEL > NONE)
        {
            Log.e(TAG, getTrace() + String.valueOf(obj));
            Log.e(TAG, getThrowableTrace(cause));
            // Log.e(TAG_GENERAL_OUTPUT, getTrace() + String.valueOf(obj));
            // Log.e(TAG_GENERAL_OUTPUT, getThrowableTrace(cause));
        }
    }

    public static void e(final Object obj)
    {
        if (LOGGING_LEVEL > NONE)
        {
            Log.e(TAG, getTrace() + String.valueOf(obj));
            // Log.e(TAG_GENERAL_OUTPUT, getTrace() + String.valueOf(obj));
        }
    }

    public static void w(final Object obj, final Throwable cause)
    {
        if (LOGGING_LEVEL > ERRORS_ONLY)
        {
            Log.w(TAG, getTrace() + String.valueOf(obj));
            Log.w(TAG, getThrowableTrace(cause));
            // Log.w(TAG_GENERAL_OUTPUT, getTrace() + String.valueOf(obj));
            // Log.w(TAG_GENERAL_OUTPUT, getThrowableTrace(cause));
        }
    }

    public static void w(final Object obj)
    {
        if (LOGGING_LEVEL > ERRORS_ONLY)
        {
            Log.w(TAG, getTrace() + String.valueOf(obj));
            // Log.w(TAG_GENERAL_OUTPUT, getTrace() + String.valueOf(obj));
        }
    }

    public static void i(final Object obj)
    {
        if (LOGGING_LEVEL > ERRORS_WARNINGS)
        {
            Log.i(TAG, getTrace() + String.valueOf(obj));
        }
    }

    public static void d(final Object obj)
    {
        if (LOGGING_LEVEL > ERRORS_WARNINGS_INFO)
        {
            Log.d(TAG, getTrace() + String.valueOf(obj));
        }
    }

    public static void v(final Object obj)
    {
        if (LOGGING_LEVEL > ERRORS_WARNINGS_INFO_DEBUG)
        {
            Log.v(TAG, getTrace() + String.valueOf(obj));
        }
    }

    private static String getThrowableTrace(final Throwable thr)
    {
        StringWriter b = new StringWriter();
        thr.printStackTrace(new PrintWriter(b));
        return b.toString();
    }

    private static String getTrace()
    {
        int depth = 2;
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();
        String callerMethodName = elements[depth].getMethodName();
        String callerClassPath = elements[depth].getClassName();
        int lineNo = elements[depth].getLineNumber();
        int i = callerClassPath.lastIndexOf('.');
        String callerClassName = callerClassPath.substring(i + 1);
        String trace = callerClassName + ": " + callerMethodName + "() [" + lineNo + "] - ";
        return trace;
    }

    /**
     * Prints the stack trace to mubaloo log and standard log
     * 
     * @param e
     */
    public static void handleException(final Exception e)
    {
        QLog.e(e.toString());
        e.printStackTrace();
    }
}
