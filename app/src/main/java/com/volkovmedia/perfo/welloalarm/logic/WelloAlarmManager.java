package com.volkovmedia.perfo.welloalarm.logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.volkovmedia.perfo.welloalarm.database.PreferencesManager;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.logic.services.AlarmBroadcastReceiver;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import static com.volkovmedia.perfo.welloalarm.general.GeneralMethods.hasEnabledAlarms;
import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.getClosestTimeDifference;
import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class WelloAlarmManager {

    private AlarmManager mAlarmManager;
    private Context mContext;

    private PreferencesManager mPreferencesManager;

    public WelloAlarmManager(Context context) {
        this.mContext = context;
        this.mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.mPreferencesManager = new PreferencesManager(context);
    }

    private void setCurrentAlarm(Alarm alarm) {
        int alarmId = alarm.getId();

        mPreferencesManager.setCurrentAlarmIdentifier(alarmId);

        long timeDifference = getClosestTimeDifference(alarm);
        long time = System.currentTimeMillis() + timeDifference;

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, createPendingIntent(alarm));

        Toast.makeText(
                mContext,
                "Будильник " + (alarm.getName().isEmpty() ? "" : (alarm.getName()) + " ") + "зазвонит через " + (timeDifference / 1000 / 60) + " минут.",
                Toast.LENGTH_LONG
        ).show();

    }

    public void scheduleNearestAlarm(UniqueList<Alarm> alarms) {
        int currentAlarmId = mPreferencesManager.getCurrentAlarmIdentifier();
        cancelAlarm(alarms.getByKey(currentAlarmId));

        if (hasEnabledAlarms(alarms)) {
            setCurrentAlarm(findNearestAlarm(alarms));
        }
    }

    private void cancelAlarm(Alarm alarm) {
        mAlarmManager.cancel(createPendingIntent(alarm));
    }

    private PendingIntent createPendingIntent(@NonNull Alarm alarm) {
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);

        intent.putExtra(KEY_ALARM, alarm);
        intent.putExtra("NAME", 1010120012);

        return PendingIntent.getBroadcast(mContext, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Alarm findNearestAlarm(UniqueList<Alarm> alarmList) {
        Alarm nearestAlarm = null;
        int position = 0;

        for (; position < alarmList.size(); position++) {
            Alarm currentAlarm = alarmList.get(position);
            if (currentAlarm.isEnabled()) {
                nearestAlarm = currentAlarm;
                break;
            }
        }

        long nearestTimeDifference = getClosestTimeDifference(nearestAlarm);

        for (position++; position < alarmList.size(); position++) {
            Alarm currentAlarm = alarmList.get(position);
            if (!currentAlarm.isEnabled()) continue;

            long currentTimeDifference = getClosestTimeDifference(currentAlarm);

            if (nearestTimeDifference > currentTimeDifference) {
                nearestAlarm = currentAlarm;
                nearestTimeDifference = currentTimeDifference;
            }
        }

        return nearestAlarm;
    }
}
