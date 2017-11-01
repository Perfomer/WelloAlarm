package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import static com.volkovmedia.perfo.welloalarm.general.Constants.MILLISECONDS_IN_SECOND;
import static com.volkovmedia.perfo.welloalarm.general.Constants.SECONDS_IN_MINUTE;
import static com.volkovmedia.perfo.welloalarm.general.GeneralMethods.isAlarmDisposable;
import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class AlertActivity extends AppCompatActivity {

    private Alarm mAlarm;

    private PendingIntent aPendingIntent;
    private AlarmManager aAlarmManager;

    private MediaPlayer aMediaPlayer;
    private Vibrator aVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        turnOnScreen();
        setContentView(R.layout.activity_alert);

        mAlarm = getIntent().getParcelableExtra(KEY_ALARM);
        if (isAlarmDisposable(mAlarm)) {
            Log.d("WOW4", "It's worked!");
//            aIDManager.enableAlarm(mAlarm.getIdentifier(), false);
            //aIDManager.saveAlarm(mAlarm);
        }

        initViews();
        initPlayer();
        prepareForCalls();

        aAlarmManager.cancel(aPendingIntent);
    }

    private void initViews() {
        TextView timeView = findViewById(R.id.alarmact_time),
                nameView = findViewById(R.id.alarmact_name);

        String name = mAlarm.getName();
        if (name.isEmpty()) nameView.setVisibility(View.GONE);
        else nameView.setText(name);

//        CurrentTimeManager currentTimeManager = new CurrentTimeManager();

//        timeView.setText(Algorithms.getTimeText(currentTimeManager.getCurrentHour(), currentTimeManager.getCurrentMinute()));
//        timeView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_flashing));
    }

    private void initPlayer() {
        aMediaPlayer = new MediaPlayer();

        if (mAlarm.isVibrate()) {
            aVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {1000, 200, 200, 200};
            aVibrator.vibrate(pattern, 0);
        }
        try {
            aMediaPlayer.setVolume(1.0f, 1.0f);
            aMediaPlayer.setDataSource(this, Uri.parse(mAlarm.getSound()));
            aMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            aMediaPlayer.setLooping(true);
            aMediaPlayer.setOnCompletionListener(mp -> {
                mp.reset();
                mp.release();
            });
            aMediaPlayer.prepare();
            aMediaPlayer.start();

        } catch (Exception e) {
            aMediaPlayer.release();
        }


    }

    private void turnOnScreen() {
        final Window window = getWindow();
        window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        );
    }

    private void finishActivity() {
        finish();
    }

    public void onAlarmTurnOffClick(View v) {
        finishActivity();
    }

    public void onAlarmSetAsideClick(View v) {
        aAlarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 5 * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND,
                aPendingIntent);
        finishActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            if (aVibrator != null) aVibrator.cancel();
            aMediaPlayer.stop();
            aMediaPlayer.release();
        } catch (Exception e) {
        }

        super.onDestroy();
    }

    private void prepareForCalls() {
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        try {
                            aMediaPlayer.pause();
                        } catch (IllegalStateException e) {
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        try {
                            aMediaPlayer.start();
                        } catch (IllegalStateException e) {
                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }
}