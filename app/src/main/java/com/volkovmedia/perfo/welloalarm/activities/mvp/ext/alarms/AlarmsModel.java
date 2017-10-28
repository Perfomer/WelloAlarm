package com.volkovmedia.perfo.welloalarm.activities.mvp.ext.alarms;

import android.os.AsyncTask;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

class AlarmsModel {

    private AlarmDatabaseHelper mDatabase;

    AlarmsModel(AlarmDatabaseHelper database) {
        this.mDatabase = database;
    }

    public void loadAlarms(LoadAlarmsCallback callback) {
        LoadAlarmsTask task = new LoadAlarmsTask(mDatabase, callback);
        task.execute();
    }

    public void saveAlarm(Alarm alarm, SaveAlarmCallback callback) {
        SaveAlarmTask task = new SaveAlarmTask(mDatabase, callback);
        task.execute(alarm);
    }

    public void deleteAlarm(Alarm alarm, DeleteAlarmCallback callback) {
        DeleteAlarmTask task = new DeleteAlarmTask(mDatabase, callback);
        task.execute(alarm);
    }

    public interface LoadAlarmsCallback {
        void onLoad(UniqueList<Alarm> alarms);
    }

    public interface SaveAlarmCallback {
        void onSave();
    }

    public interface DeleteAlarmCallback {
        void onDelete();
    }

    private static class LoadAlarmsTask extends AsyncTask<Void, Void, UniqueList<Alarm>> {

        private final LoadAlarmsCallback callback;
        private final AlarmDatabaseHelper database;

        LoadAlarmsTask(AlarmDatabaseHelper database, LoadAlarmsCallback callback) {
            this.callback = callback;
            this.database = database;
        }

        @Override
        protected UniqueList<Alarm> doInBackground(Void... params) {
            return database.readAlarms();
        }

        @Override
        protected void onPostExecute(UniqueList<Alarm> alarm) {
            if (callback != null) {
                callback.onLoad(alarm);
            }
        }
    }

    private static class SaveAlarmTask extends AsyncTask<Alarm, Void, Void> {

        private final SaveAlarmCallback callback;
        private final AlarmDatabaseHelper database;

        private SaveAlarmTask(AlarmDatabaseHelper database, SaveAlarmCallback callback) {
            this.callback = callback;
            this.database = database;
        }

        @Override
        protected Void doInBackground(Alarm... params) {
            database.saveAlarm(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (callback != null) {
                callback.onSave();
            }
        }
    }

    private static class DeleteAlarmTask extends AsyncTask<Alarm, Void, Void> {

        private final DeleteAlarmCallback callback;
        private final AlarmDatabaseHelper database;

        private DeleteAlarmTask(AlarmDatabaseHelper database, DeleteAlarmCallback callback) {
            this.callback = callback;
            this.database = database;
        }

        @Override
        protected Void doInBackground(Alarm... params) {
            database.removeAlarm(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (callback != null) {
                callback.onDelete();
            }
        }
    }

}
