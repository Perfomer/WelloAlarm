package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms;

import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.IMvpModel;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks.DeleteAlarmTask;
import com.volkovmedia.perfo.welloalarm.database.tasks.LoadAlarmsTask;
import com.volkovmedia.perfo.welloalarm.database.tasks.SaveAlarmTask;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTask;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTaskCallback;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import javax.inject.Inject;

public class AlarmsModel implements IMvpModel {

    @Inject
    AlarmDatabaseHelper mDatabase;

    private UniqueList<Alarm> mAlarms;

    private DatabaseTask mActiveReadingTask;

    public AlarmsModel() {
        WelloApplication.getComponent().inject(this);
    }

    void loadAlarms(boolean preventLoadFromDatabase, DatabaseTaskCallback<UniqueList<Alarm>> callback) {
        if (mAlarms != null && !preventLoadFromDatabase) {
            callback.onTaskDone(mAlarms);
        } else {
            LoadAlarmsTask task = new LoadAlarmsTask(mDatabase, result -> {
                mActiveReadingTask = null;
                mAlarms = result;
                callback.onTaskDone(result);
            });

            this.mActiveReadingTask = task;
            task.execute();
        }
    }

    void saveAlarm(Alarm alarm, boolean update, DatabaseTaskCallback<Integer> callback) {
        if (mAlarms != null) mAlarms.update(alarm);
        SaveAlarmTask task = new SaveAlarmTask(mDatabase, mAlarms, update, callback);
        task.execute(alarm);
    }

    void deleteAlarm(Alarm alarm, DatabaseTaskCallback<Integer> callback) {
        DeleteAlarmTask task = new DeleteAlarmTask(mDatabase, mAlarms, callback);
        task.execute(alarm);
    }

    @Override
    public void destroy() {
        if (mActiveReadingTask != null) {
            mActiveReadingTask.cancel(true);
        }
    }
}