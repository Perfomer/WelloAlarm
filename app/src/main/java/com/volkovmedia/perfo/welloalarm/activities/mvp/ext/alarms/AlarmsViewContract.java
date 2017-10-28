package com.volkovmedia.perfo.welloalarm.activities.mvp.ext.alarms;

import com.volkovmedia.perfo.welloalarm.activities.mvp.impl.MvpView;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

interface AlarmsViewContract extends MvpView {

    void showNoAlarmsLayout();
    void showAlarms(UniqueList<Alarm> alarms);

    void onAlarmAdded(Alarm alarm, int position);
    void onAlarmEdited(Alarm alarm, int position);
    void onAlarmDeleted(Alarm alarm, int position);
}
