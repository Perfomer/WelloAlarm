package com.volkovmedia.perfo.welloalarm.logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.volkovmedia.perfo.welloalarm.database.PreferencesManager;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.logic.services.AlarmBroadcastReceiver;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import javax.inject.Inject;

import static com.volkovmedia.perfo.welloalarm.general.Constants.INT_NO_VALUE;
import static com.volkovmedia.perfo.welloalarm.general.Constants.MILLISECONDS_IN_SECOND;
import static com.volkovmedia.perfo.welloalarm.general.Constants.SECONDS_IN_MINUTE;
import static com.volkovmedia.perfo.welloalarm.general.GeneralMethods.hasEnabledAlarms;
import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.getClosestTimeDifference;
import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class WelloAlarmManager {

    private AlarmManager mAlarmManager;

    @Inject
    Context mContext;

    @Inject
    PreferencesManager mPreferencesManager;

    public WelloAlarmManager() {
        WelloApplication.getComponent().inject(this);
        this.mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    private void setCurrentAlarm(Alarm alarm) {
        setCurrentAlarm(alarm, getClosestTimeDifference(alarm));
    }

    private void setCurrentAlarm(Alarm alarm, long timeDifference) {
        int alarmId = alarm.getId();

        mPreferencesManager.setCurrentAlarmIdentifier(alarmId);

        long time = System.currentTimeMillis() + timeDifference;

        PendingIntent pendingIntent = createPendingIntent();
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

        Toast.makeText(
                mContext,
                "Будильник " + (alarm.getName().isEmpty() ? "" : (alarm.getName()) + " ") + "зазвонит через " + (timeDifference / 1000 / 60) + " минут.",
                Toast.LENGTH_LONG
        ).show();
    }

    public boolean scheduleNearestAlarm(UniqueList<Alarm> alarms) {
        if (!hasEnabledAlarms(alarms)) {
            cancelCurrentAlarm();
            return false;
        }

        setCurrentAlarm(findNearestAlarm(alarms));
        return true;
    }

    public boolean scheduleNextAlarm(UniqueList<Alarm> alarms) {
        if (!hasEnabledAlarms(alarms)) {
            cancelCurrentAlarm();
            return false;
        }

        Alarm currentAlarm = alarms.getByKey(mPreferencesManager.getCurrentAlarmIdentifier());
        Alarm nextAlarm = getNextAlarm(alarms, currentAlarm);
        setCurrentAlarm(nextAlarm);

        return true;
    }

//    public void setSnoozedAlarm(Alarm alarm) {
//        long timeDifference = 5 * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;
//        mPreferencesManager.setSnoozedAlarm(alarm.getId(),System.currentTimeMillis() + timeDifference);
//        setCurrentAlarm(alarm, timeDifference);
//    }

    private void cancelCurrentAlarm() {
        mPreferencesManager.removeCurrentAlarmIdentifier();
        mAlarmManager.cancel(createPendingIntent());

        Toast.makeText(
                mContext,
                "Будильников нет",
                Toast.LENGTH_SHORT).show();
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);
        return PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public static Alarm findNearestAlarm(UniqueList<Alarm> alarmsList) {
        alarmsList = getEnabledAlarms(alarmsList);

        switch (alarmsList.size()) {
            case 0:
                return null;
            case 1:
                return alarmsList.get(0);
            default:
                return findNearestAlarm(alarmsList, alarmsList.get(0));
        }
    }

    private static Alarm getNextAlarm(UniqueList<Alarm> alarmList, Alarm alarm) {
        if (alarm == null || !alarm.isEnabled()) return findNearestAlarm(alarmList);

        return findNearestAlarm(alarmList, alarm);
    }

    private static Alarm findNearestAlarm(UniqueList<Alarm> enabledAlarmsList, @NonNull Alarm alarm) {
        Alarm nearestAlarm = alarm;
        long nearestTimeDifference = getClosestTimeDifference(nearestAlarm);

        for (int i = 1; i < enabledAlarmsList.size(); i++) {
            Alarm currentAlarm = enabledAlarmsList.get(i);

            long currentTimeDifference = getClosestTimeDifference(currentAlarm);

            if (currentTimeDifference < nearestTimeDifference) {
                nearestAlarm = currentAlarm;
                nearestTimeDifference = currentTimeDifference;
            }
        }

        return nearestAlarm;
    }

    private static UniqueList<Alarm> getEnabledAlarms(UniqueList<Alarm> alarmList) {
        UniqueList<Alarm> alarms = new UniqueList<>();

        for (int i = 0; i < alarmList.size(); i++) {
            Alarm currentAlarm = alarmList.get(i);
            if (currentAlarm.isEnabled()) alarms.add(currentAlarm);
        }

        return alarms;
    }
}
