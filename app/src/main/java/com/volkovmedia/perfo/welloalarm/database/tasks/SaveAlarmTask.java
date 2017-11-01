package com.volkovmedia.perfo.welloalarm.database.tasks;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTaskCallback;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

public class SaveAlarmTask extends DeleteAlarmTask {

    private final boolean mUpdate;

    public SaveAlarmTask(AlarmDatabaseHelper database, UniqueList<Alarm> dataSource, DatabaseTaskCallback<Integer> callback, boolean update) {
        super(database, dataSource, callback);
        this.mUpdate = update;
    }

    @Override
    protected Integer doInBackground(Alarm... params) {
        Alarm alarm = params[0];
        UniqueList<Alarm> alarms = getSource();

        int newId = getDatabase().saveAlarm(alarm);
        alarm.setId(newId);

        if (mUpdate) alarms.update(alarm);
        else alarms.add(alarm);

        return alarms.getPositionByKey(alarm.getId());
    }

}