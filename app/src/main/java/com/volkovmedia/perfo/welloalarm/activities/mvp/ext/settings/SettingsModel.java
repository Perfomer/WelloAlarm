package com.volkovmedia.perfo.welloalarm.activities.mvp.ext.settings;

import android.os.AsyncTask;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

public class SettingsModel {

    private AlarmDatabaseHelper mDatabase;

    public SettingsModel(AlarmDatabaseHelper database) {
        this.mDatabase = database;
    }

    public void saveAlarm(Alarm alarm, SaveAlarmCallback callback) {
        SaveAlarmTask task = new SaveAlarmTask(mDatabase, callback);
        task.execute(alarm);
    }

    public interface SaveAlarmCallback {
        void onSave();
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

}