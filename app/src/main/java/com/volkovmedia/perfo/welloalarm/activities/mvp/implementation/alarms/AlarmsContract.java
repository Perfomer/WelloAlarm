package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms;

import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.MvpPresenter;
import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.MvpView;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

interface AlarmsContract {

    interface View extends MvpView {

        void showNoAlarmsLayout();
        void showAlarms(UniqueList<Alarm> alarms);

        void onAlarmAdded(Alarm alarm, int position);
        void onAlarmEdited(Alarm alarm, int position);
        void onAlarmDeleted(Alarm alarm, int position);

        boolean isNoAlarmsViewVisible();
    }

    interface Presenter extends MvpPresenter {
        void addAlarm(Alarm alarm);
        void editAlarm(Alarm alarm, boolean withCallback);
        void deleteAlarm(Alarm alarm);
    }
}
