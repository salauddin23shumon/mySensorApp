package com.sss.myhwmonitorapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class SensorPreference {

    public static final String IS_RUNNING = "isServiceRunning";

    public static boolean isServiceRunning(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_RUNNING, false);
    }


    public static void setServiceRunning(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_RUNNING, requestingLocationUpdates)
                .apply();

    }
}
