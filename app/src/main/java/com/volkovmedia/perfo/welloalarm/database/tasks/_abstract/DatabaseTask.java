package com.volkovmedia.perfo.welloalarm.database.tasks._abstract;

import android.os.AsyncTask;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.DataContainer;

public abstract class DatabaseTask<Source extends DataContainer, Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private final DatabaseTaskCallback<Result> mCallback;
    private final AlarmDatabaseHelper mDatabaseHelper;
    private final UniqueList<Source> mDataSource;

    public DatabaseTask(AlarmDatabaseHelper database, UniqueList<Source> dataSource, DatabaseTaskCallback<Result> callback) {
        this.mCallback = callback;
        this.mDatabaseHelper = database;
        this.mDataSource = dataSource;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (mCallback != null) {
            mCallback.onTaskDone(result);
        }
    }

    protected AlarmDatabaseHelper getDatabase() {
        return mDatabaseHelper;
    }

    protected UniqueList<Source> getSource() {
        return mDataSource;
    }

    protected DatabaseTaskCallback<Result> getCallback() {
        return mCallback;
    }
}