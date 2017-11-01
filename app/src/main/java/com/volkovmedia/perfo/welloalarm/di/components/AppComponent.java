package com.volkovmedia.perfo.welloalarm.di.components;

import android.content.Context;

import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms.AlarmsActivity;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.di.modules.AppModule;
import com.volkovmedia.perfo.welloalarm.di.modules.StorageModule;

import dagger.Component;

@Component(modules = {StorageModule.class})
public interface AppComponent {

    void injectsAlarmsActivity(AlarmsActivity activity);
    //AlarmDatabaseHelper getDatabase(Context context);


}
