package com.volkovmedia.perfo.welloalarm.database.tasks;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTaskCallback;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

public class SaveAlarmTask extends DeleteAlarmTask {

    private final boolean mUpdate;

    public SaveAlarmTask(AlarmDatabaseHelper database, UniqueList<Alarm> dataSource, boolean update, DatabaseTaskCallback<Integer> callback) {
        super(database, dataSource, callback);
        this.mUpdate = update;
    }

    @Override
    protected Integer doInBackground(Alarm... params) {
        Alarm alarm = params[0];
        UniqueList<Alarm> alarms = getSource();

        int id = getDatabase().saveAlarm(alarm);

        if (mUpdate) {
            alarms.update(alarm);
        } else {
            alarm.setId(id);
            alarms.add(alarm);
        }

        return alarms.getPositionByKey(alarm.getId());
    }

}