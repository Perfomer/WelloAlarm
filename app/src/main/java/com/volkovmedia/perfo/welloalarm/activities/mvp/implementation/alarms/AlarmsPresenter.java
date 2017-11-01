package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms;

import android.content.Context;

import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.MvpPresenter;
import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.MvpView;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

class AlarmsPresenter implements MvpPresenter {

    private AlarmsViewContract mView;
    private AlarmsModel mModel;

    private Context mContext;

    private WelloAlarmManager mAlarmManager;

    AlarmsPresenter(AlarmsModel model) {
        this.mModel = model;
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (AlarmsViewContract) view;
        this.mContext = (Context) view;

        this.mAlarmManager = new WelloAlarmManager(mContext);
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void viewIsReady() {
        mModel.loadAlarms(alarms -> {
            mModel.setAlarms(alarms);
            updateAlarmsLayout(alarms);

            mAlarmManager.scheduleNearestAlarm(alarms);
        });
    }

    private void updateAlarmsLayout(UniqueList<Alarm> alarms) {
        boolean noAlarmsViewVisible = mView.isNoAlarmsViewVisible();

        if (alarms.size() == 0 && !noAlarmsViewVisible)
            mView.showNoAlarmsLayout();
        else if (alarms.size() > 0 && noAlarmsViewVisible)
            mView.showAlarms(alarms);
    }

    void editAlarm(Alarm alarm, boolean withCallback) {
        mModel.saveAlarm(alarm, true, position -> {
                    if (withCallback) mView.onAlarmEdited(alarm, position);
                    mAlarmManager.scheduleNearestAlarm(mModel.getAlarms());
                });
    }

    void addAlarm(Alarm alarm) {
        mModel.saveAlarm(alarm, false, (position) -> {
            mView.onAlarmAdded(alarm, position);
            updateAlarmsLayout(mModel.getAlarms());
        });
    }

    void deleteAlarm(Alarm alarm) {
        mModel.deleteAlarm(alarm, (position) -> {
            mView.onAlarmDeleted(alarm, position);
            updateAlarmsLayout(mModel.getAlarms());
        });
    }
}