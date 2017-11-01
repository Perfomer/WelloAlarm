package com.volkovmedia.perfo.welloalarm.database.tasks;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTask;
import com.volkovmedia.perfo.welloalarm.database.tasks._abstract.DatabaseTaskCallback;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

public class DeleteAlarmTask extends DatabaseTask<Alarm, Alarm, Void, Integer>{

    public DeleteAlarmTask(AlarmDatabaseHelper database, UniqueList<Alarm> dataSource, DatabaseTaskCallback<Integer> callback) {
        super(database, dataSource, callback);
    }

    @Override
    protected Integer doInBackground(Alarm... params) {
        Alarm alarm = params[0];
        UniqueList<Alarm> alarms = getSource();
        int position = alarms.getPositionByKey(alarm.getId());

        getDatabase().removeAlarm(alarm);
        alarms.remove(alarm);

        return position;
    }

}