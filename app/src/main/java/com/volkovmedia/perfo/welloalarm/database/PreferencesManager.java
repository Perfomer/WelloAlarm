package com.volkovmedia.perfo.welloalarm.database;

import android.content.Context;
import android.content.SharedPreferences;

import static com.volkovmedia.perfo.welloalarm.general.Constants.INT_NO_VALUE;

public class PreferencesManager {

    private final static String PREF_APP = "welloalarm", PREF_CURRENT_ALARM = "current_alarm";
//            PREF_SNOOZED_ALARM = "snoozed_alarm", PREF_SNOOZED_ALARM_TIME = "snoozed_alarm_time";

    private SharedPreferences mPreferences;

    public PreferencesManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
    }

    public void setCurrentAlarmIdentifier(int alarmId) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PREF_CURRENT_ALARM, alarmId);
        editor.apply();
    }

//    public void setSnoozedAlarm(int alarmId, long time) {
//        SharedPreferences.Editor editor = mPreferences.edit();
//        editor.putInt(PREF_SNOOZED_ALARM, alarmId);
//        editor.putLong(PREF_SNOOZED_ALARM_TIME, time);
//        editor.apply();
//    }

    public int getCurrentAlarmIdentifier() {
        return mPreferences.getInt(PREF_CURRENT_ALARM, INT_NO_VALUE);
    }

    public void removeCurrentAlarmIdentifier() {
        mPreferences.edit()
                .remove(PREF_CURRENT_ALARM)
                .apply();
    }
//
//    public long getSnoozedAlarmTime() {
//        return mPreferences.getLong(PREF_SNOOZED_ALARM_TIME, INT_NO_VALUE);
//    }
//
//    public long getSnoozedAlarmIdentifier() {
//        return mPreferences.getInt(PREF_SNOOZED_ALARM, INT_NO_VALUE);
//    }



}