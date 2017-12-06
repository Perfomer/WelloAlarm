package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms;

import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.base.BasePresenter;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import javax.inject.Inject;

public class AlarmsPresenter extends BasePresenter<AlarmsContract.View, AlarmsModel> {

    //TODO allow cancel editing

    @Inject
    WelloAlarmManager mAlarmManager;

    private boolean mAlarmsScheduled;

    public AlarmsPresenter() {
        super(WelloApplication.getComponent().getAlarmsModel());
        mAlarmManager = WelloApplication.getComponent().getAlarmManager();
    }

    @Override
    public void viewIsReady() {
        getModel().loadAlarms(false, alarms -> {
//            showAlarmsLayout(alarms);

            if (!mAlarmsScheduled) {
                mAlarmsScheduled = mAlarmManager.scheduleNearestAlarm(alarms);
            }
        });
    }

    private void showAlarms(UniqueList<Alarm> alarms) {
        switchAlarmsLayout(alarms, () -> {
            if (getView().isNoAlarmsViewVisible()) getView().showAlarms(alarms);
        });
    }

    private void showAlarmsLayout(UniqueList<Alarm> alarms) {
        switchAlarmsLayout(alarms, () -> getView().showAlarmsViews());
    }

    private void switchAlarmsLayout(UniqueList<Alarm> alarms, AlarmsLayoutUpdater action) {
        boolean noAlarmsViewVisible = getView().isNoAlarmsViewVisible();

        if (alarms.size() == 0) {
            if (!noAlarmsViewVisible) getView().showNoAlarmsLayout();
        } else action.onViewUpdate();
    }

    private void onAlarmsUpdated(UniqueList<Alarm> alarms) {
        mAlarmManager.scheduleNearestAlarm(alarms);
        showAlarms(alarms);
    }

    public void onInterfaceUpdateAsked() {
        getModel().loadAlarms(true,
                alarms -> switchAlarmsLayout(alarms,
                        () -> getView().showAlarms(alarms))
        );
    }

    public void editAlarm(Alarm alarm, boolean withCallback) {
        getModel().saveAlarm(alarm, true, position -> {
            if (withCallback) getView().onAlarmEdited(alarm, position);
            getModel().loadAlarms(false, mAlarmManager::scheduleNearestAlarm);
        });
    }

    public void addAlarm(Alarm alarm) {
        getModel().saveAlarm(alarm, false, (position) -> {
            getView().onAlarmAdded(alarm, position);
            getModel().loadAlarms(false, this::onAlarmsUpdated);
        });
    }

    public void deleteAlarm(Alarm alarm) {
        getModel().deleteAlarm(alarm, (position) -> {
            getView().onAlarmDeleted(alarm, position);
            getModel().loadAlarms(false, this::onAlarmsUpdated);
        });
    }

    private interface AlarmsLayoutUpdater {
        void onViewUpdate();
    }
}