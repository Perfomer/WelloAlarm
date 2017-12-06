package com.volkovmedia.perfo.welloalarm.logic.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alert.AlertActivity;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.PreferencesManager;
import com.volkovmedia.perfo.welloalarm.database.tasks.LoadAlarmsTask;
import com.volkovmedia.perfo.welloalarm.database.tasks.SaveAlarmTask;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import static com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager.findNearestAlarm;
import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class BootAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        if (TextUtils.equals(intent.getAction(), )) { //DOPISHI CHEREZ DEBUG
            AlarmDatabaseHelper databaseHelper = new AlarmDatabaseHelper(context);
            LoadAlarmsTask task = new LoadAlarmsTask(databaseHelper, alarms -> {
                WelloAlarmManager alarmManager = new WelloAlarmManager();
                alarmManager.scheduleNearestAlarm(alarms);
            });

            task.execute();
//        }
    }

}
