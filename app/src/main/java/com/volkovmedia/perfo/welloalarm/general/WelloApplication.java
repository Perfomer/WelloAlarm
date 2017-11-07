package com.volkovmedia.perfo.welloalarm.general;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.volkovmedia.perfo.welloalarm.di.components.AppComponent;
import com.volkovmedia.perfo.welloalarm.di.components.DaggerAppComponent;
import com.volkovmedia.perfo.welloalarm.di.modules.AppModule;

public class WelloApplication extends Application {

    private static AppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        mComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();

//        dataComponent = DaggerDataComponent.builder()
//                .dataModule(new DataModule(this))
//                .build();
    }

    public static AppComponent getComponent() {
        return mComponent;
    }

}
