package com.volkovmedia.perfo.welloalarm.logic.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

    private boolean abrAlarmsLoaded = false;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        Log.d(this.getClass().getSimpleName(),"onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("WOW3","onStartCommand()");
//
//        Intent alarmActivity = new Intent(this, AlarmActivity.class);
//
//        alarmActivity.putExtra(ALARM_KEY, intent.getParcelableExtra(ALARM_KEY));
//        alarmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        AlarmHandler alarmHandler = new AlarmHandler(this, new ItemDatabaseManager(this, this, new ArrayList<AlarmModel>()));
//        alarmHandler.scheduleNearestAlarm();
//        try {
//            while(!abrAlarmsLoaded) {
//                alarmHandler.scheduleNearestAlarm();
//                Thread.sleep(250);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.e("WOW3", "Lolkek");
//
//        startActivity(alarmActivity);
        return START_NOT_STICKY;
    }

    public void notifyAlarmsDBLoaded() {
        abrAlarmsLoaded = true;
    }

}