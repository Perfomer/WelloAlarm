package com.volkovmedia.perfo.welloalarm.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context mContext;

    public AppModule(@NonNull Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mContext;
    }

}
