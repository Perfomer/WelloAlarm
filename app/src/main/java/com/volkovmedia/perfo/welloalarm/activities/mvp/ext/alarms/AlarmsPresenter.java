package com.volkovmedia.perfo.welloalarm.activities.mvp.ext.alarms;

import com.volkovmedia.perfo.welloalarm.activities.mvp.impl.MvpPresenter;
import com.volkovmedia.perfo.welloalarm.activities.mvp.impl.MvpView;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

class AlarmsPresenter implements MvpPresenter {

    private AlarmsViewContract mView;
    private AlarmsModel mModel;

    private UniqueList<Alarm> mAlarms;

    AlarmsPresenter(AlarmsModel model) {
        this.mModel = model;
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (AlarmsViewContract) view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void viewIsReady() {
        mModel.loadAlarms(alarms -> {
            this.mAlarms = alarms;

            if (alarms.size() == 0) mView.showNoAlarmsLayout();
            else mView.showAlarms(alarms);
        });
    }

    void editAlarm(Alarm alarm) {
        mModel.saveAlarm(alarm, () -> {
            mAlarms.update(alarm);
            mView.onAlarmEdited(alarm, mAlarms.getPositionByKey(alarm.getId()));
        });
    }

    void addAlarm(Alarm alarm) {
        mModel.saveAlarm(alarm, () -> {
            mAlarms.add(alarm);
            mView.onAlarmAdded(alarm, mAlarms.getPositionByKey(alarm.getId()));
        });
    }

    void deleteAlarm(Alarm alarm) {
        mModel.deleteAlarm(alarm, () -> {
            int position = mAlarms.getPositionByKey(alarm.getId());
            mAlarms.remove(alarm);
            mView.onAlarmDeleted(alarm, position);
        });
    }
}