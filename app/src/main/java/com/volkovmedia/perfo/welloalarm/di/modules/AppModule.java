package com.volkovmedia.perfo.welloalarm.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(@NonNull Context context) {
        this.mContext = context;
    }

    @Provides
    public Context provideContext() {
        return mContext;
    }

    @Provides
    public WelloAlarmManager provideAlarmManager() {
        return new WelloAlarmManager();
    }

}
