package com.volkovmedia.perfo.welloalarm.di.modules;

import android.content.Context;

import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.database.PreferencesManager;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    AlarmDatabaseHelper provideDatabase(Context context) {
        return new AlarmDatabaseHelper(context);
    }

    @Provides
    PreferencesManager providePreferences(Context context) {
        return new PreferencesManager(context);
    }

}