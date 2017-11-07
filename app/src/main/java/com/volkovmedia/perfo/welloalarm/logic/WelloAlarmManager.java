package com.volkovmedia.perfo.welloalarm.logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.volkovmedia.perfo.welloalarm.database.PreferencesManager;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.logic.services.AlarmBroadcastReceiver;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import javax.inject.Inject;

import static com.volkovmedia.perfo.welloalarm.general.GeneralMethods.hasEnabledAlarms;
import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.getClosestTimeDifference;
import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class WelloAlarmManager {
    //TODO Think about removing current alarm id from SharedPreferences
    private AlarmManager mAlarmManager;

    @Inject
    Context mContext;

    @Inject
    PreferencesManager mPreferencesManager;

    private PendingIntent mPendingIntent;

    public WelloAlarmManager() {
        WelloApplication.getComponent().inject(this);
        this.mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    private void setCurrentAlarm(Alarm alarm) {
        int alarmId = alarm.getId();

        mPreferencesManager.setCurrentAlarmIdentifier(alarmId);

        long timeDifference = getClosestTimeDifference(alarm);
        long time = System.currentTimeMillis() + timeDifference;
//        long time = System.currentTimeMillis() + 100; //TODO remove debug line

        mPendingIntent = createPendingIntent(alarm, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, mPendingIntent);

        Toast.makeText(
                mContext,
                "Будильник " + (alarm.getName().isEmpty() ? "" : (alarm.getName()) + " ") + "зазвонит через " + (timeDifference / 1000 / 60) + " минут.",
                Toast.LENGTH_LONG
        ).show();

    }

    public boolean scheduleNearestAlarm(UniqueList<Alarm> alarms) {
        if (!hasEnabledAlarms(alarms)) return false;

        int currentAlarmId = mPreferencesManager.getCurrentAlarmIdentifier();

        //cancelAlarm(alarms.getByKey(currentAlarmId));
        setCurrentAlarm(findNearestAlarm(alarms));

        return true;
    }

    private void cancelAlarm(Alarm alarm) {
        if (alarm != null)
            mAlarmManager.cancel(createPendingIntent(alarm, PendingIntent.FLAG_CANCEL_CURRENT));
    }

    private PendingIntent createPendingIntent(@NonNull Alarm alarm, int flag) {
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);

        intent.putExtra(KEY_ALARM, alarm);
        intent.putExtra("NAME", 10101);

        //TODO Play with Pending Intent flags.
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
