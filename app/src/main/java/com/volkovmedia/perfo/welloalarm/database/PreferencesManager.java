package com.volkovmedia.perfo.welloalarm.database;

import android.content.Context;
import android.content.SharedPreferences;

import static com.volkovmedia.perfo.welloalarm.general.Constants.INT_NO_VALUE;

public class PreferencesManager {

    private final static String PREF_APP = "welloalarm", PREF_CURRENT_ALARM = "current_alarm";

    private SharedPreferences mPreferences;

    public PreferencesManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
    }

    public void setCurrentAlarmIdentifier(int alarmId) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PREF_CURRENT_ALARM, alarmId);
        editor.apply();
    }

    public int getCurrentAlarmIdentifier() {
        return mPreferences.getInt(PREF_CURRENT_ALARM, INT_NO_VALUE);
    }

}