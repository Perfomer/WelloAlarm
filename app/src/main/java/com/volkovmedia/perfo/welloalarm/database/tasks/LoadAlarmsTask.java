package com.volkovmedia.perfo.welloalarm.database.tasks;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTask;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTaskCallback;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

public class LoadAlarmsTask extends DatabaseTask<Alarm, Void, Void, UniqueList<Alarm>>{

    public LoadAlarmsTask(AlarmDatabaseHelper database, DatabaseTaskCallback<UniqueList<Alarm>> callback) {
        super(database, null, callback);
    }

    @Override
    protected UniqueList<Alarm> doInBackground(Void... voids) {
        return getDatabase().readAlarms();
    }

}