package com.volkovmedia.perfo.welloalarm.logic.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alert.AlertActivity;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks.LoadAlarmsTask;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import static com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager.findNearestAlarm;
import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    //TODO [0] Fix empty intent
    //TODO [1] Disable disposable alarms
    //TODO [2] Set next alarm

    @Override
    public void onReceive(Context context, Intent intent) {

        LoadAlarmsTask task = new LoadAlarmsTask(new AlarmDatabaseHelper(context), alarms -> {
            Alarm receivedAlarm = getAlarmFromIntent(intent),
                    nearestAlarm = findNearestAlarm(alarms);

            if (nearestAlarm != null) {
                if (receivedAlarm.getId() == nearestAlarm.getId())
                    startAlertActivity(context, nearestAlarm);
                else {
                    WelloAlarmManager manager = new WelloAlarmManager(context);
                    manager.scheduleNearestAlarm(alarms);
                }
            }
        });

        task.execute();
    }

    private static Alarm getAlarmFromIntent(Intent intent) {
        return intent.getParcelableExtra(KEY_ALARM);
    }

    private static void startAlertActivity(Context context, Alarm alarm) {
        Intent alarmActivity = new Intent(context, AlertActivity.class);
        alarmActivity.putExtra(KEY_ALARM, alarm);
        alarmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmActivity);
    }

}
