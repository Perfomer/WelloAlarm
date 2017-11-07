package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms;

import android.content.Context;

import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.MvpPresenter;
import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.MvpView;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import javax.inject.Inject;

public class AlarmsPresenter implements AlarmsContract.Presenter {

    private AlarmsContract.View mView;

    @Inject
    AlarmsModel mModel;

    @Inject
    WelloAlarmManager mAlarmManager;

    private boolean mAlarmsScheduled;

    public AlarmsPresenter() {
        WelloApplication.getComponent().inject(this);
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (AlarmsContract.View) view;
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

            if (!mAlarmsScheduled) {
                mAlarmsScheduled = mAlarmManager.scheduleNearestAlarm(alarms);
            }
        });
    }

    private void updateAlarmsLayout(UniqueList<Alarm> alarms) {
        boolean noAlarmsViewVisible = mView.isNoAlarmsViewVisible();

        if (alarms.size() == 0) {
            if (!noAlarmsViewVisible)
                mView.showNoAlarmsLayout();
        }
        else if (noAlarmsViewVisible)
            mView.showAlarms(alarms);
    }

    @Override
    public void editAlarm(Alarm alarm, boolean withCallback) {
        mModel.saveAlarm(alarm, true, position -> {
            if (withCallback) mView.onAlarmEdited(alarm, position);
            mModel.loadAlarms(mAlarmManager::scheduleNearestAlarm);
        });
    }

    @Override
    public void addAlarm(Alarm alarm) {
        mModel.saveAlarm(alarm, false, (position) -> {
            mView.onAlarmAdded(alarm, position);
            mModel.loadAlarms(this::updateAlarmsLayout);
        });
    }

    @Override
    public void deleteAlarm(Alarm alarm) {
        mModel.deleteAlarm(alarm, (position) -> {
            mView.onAlarmDeleted(alarm, position);
            mModel.loadAlarms(this::updateAlarmsLayout);
        });
    }
}