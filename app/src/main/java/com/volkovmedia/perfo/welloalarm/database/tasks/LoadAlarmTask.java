package com.volkovmedia.perfo.welloalarm.database.tasks;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTask;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTaskCallback;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

public class LoadAlarmTask extends DatabaseTask<Alarm, Integer, Void, Alarm>{

    public LoadAlarmTask(AlarmDatabaseHelper database, UniqueList<Alarm> dataSource, DatabaseTaskCallback<Alarm> callback) {
        super(database, dataSource, callback);
    }

    @Override
    protected Alarm doInBackground(Integer... params) {
        return getDatabase().readAlarm(params[0]);
    }

}