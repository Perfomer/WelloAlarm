package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks.DeleteAlarmTask;
import com.volkovmedia.perfo.welloalarm.database.tasks.LoadAlarmsTask;
import com.volkovmedia.perfo.welloalarm.database.tasks.SaveAlarmTask;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTaskCallback;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import javax.inject.Inject;

public class AlarmsModel {

    @Inject
    AlarmDatabaseHelper mDatabase;

    private UniqueList<Alarm> mAlarms;

    public AlarmsModel() {
        WelloApplication.getComponent().inject(this);
        //this.mAlarms = new UniqueList<>();
    }

    void loadAlarms(DatabaseTaskCallback<UniqueList<Alarm>> callback) {
        if (mAlarms != null) {
            callback.onTaskDone(mAlarms);
        } else {
            LoadAlarmsTask task = new LoadAlarmsTask(mDatabase, callback);
            task.execute();
        }
    }

    void saveAlarm(Alarm alarm, boolean update, DatabaseTaskCallback<Integer> callback) {
        SaveAlarmTask task = new SaveAlarmTask(mDatabase, mAlarms, callback, update);
        task.execute(alarm);
    }

    void deleteAlarm(Alarm alarm, DatabaseTaskCallback<Integer> callback) {
        DeleteAlarmTask task = new DeleteAlarmTask(mDatabase, mAlarms, callback);
        task.execute(alarm);
    }

    public void setAlarms(UniqueList<Alarm> alarms) {
        this.mAlarms = alarms;
    }

}