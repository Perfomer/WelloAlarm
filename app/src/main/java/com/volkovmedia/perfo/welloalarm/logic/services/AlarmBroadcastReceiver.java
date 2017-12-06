package com.volkovmedia.perfo.welloalarm.logic.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alert.AlertActivity;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.PreferencesManager;
import com.volkovmedia.perfo.welloalarm.database.tasks.LoadAlarmsTask;
import com.volkovmedia.perfo.welloalarm.database.tasks.SaveAlarmTask;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private boolean mNextAlarmWillBeScheduled;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmDatabaseHelper databaseHelper = new AlarmDatabaseHelper(context);
        LoadAlarmsTask task = new LoadAlarmsTask(databaseHelper, alarms -> {
            PreferencesManager preferencesManager = new PreferencesManager(context);
            WelloAlarmManager alarmManager = new WelloAlarmManager();

            Alarm currentAlarm = alarms.getByKey(preferencesManager.getCurrentAlarmIdentifier());

            if (currentAlarm != null) {
                boolean alarmDisposable = currentAlarm.isDisposable();
                currentAlarm.setEnabled(!alarmDisposable);

                if (alarmDisposable) {
                    mNextAlarmWillBeScheduled = true;
                    SaveAlarmTask saveAlarmTask = new SaveAlarmTask(databaseHelper, alarms, true, position -> {
                        startAlertActivity(context, currentAlarm);
                        alarmManager.scheduleNextAlarm(alarms);
                    });

                    saveAlarmTask.execute(currentAlarm);
                }

            }

             if (!mNextAlarmWillBeScheduled) {
                 alarmManager.scheduleNextAlarm(alarms);
             }
        });

        task.execute();
    }

    private static void startAlertActivity(Context context, Alarm alarm) {
        Intent alarmActivity = new Intent(context, AlertActivity.class);

        alarmActivity.putExtra(KEY_ALARM, alarm);
        alarmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(alarmActivity);
    }

}
