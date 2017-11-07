package com.volkovmedia.perfo.welloalarm.di.components;

import android.content.Context;

import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms.AlarmsActivity;
import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms.AlarmsModel;
import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms.AlarmsPresenter;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.PreferencesManager;
import com.volkovmedia.perfo.welloalarm.di.modules.AppModule;
import com.volkovmedia.perfo.welloalarm.di.modules.MvpModule;
import com.volkovmedia.perfo.welloalarm.di.modules.StorageModule;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;

import dagger.Component;

@Component(modules = {AppModule.class, StorageModule.class, MvpModule.class})
public interface AppComponent {

    void inject(AlarmsActivity object);
    void inject(AlarmsPresenter object);
    void inject(AlarmsModel object);

    void inject(WelloAlarmManager object);

    Context getContext();
    WelloAlarmManager getAlarmManager();

    AlarmDatabaseHelper getDatabase();
    PreferencesManager getPreferences();

    AlarmsPresenter getAlarmsPresenter();
    AlarmsModel getAlarmsModel();

}
