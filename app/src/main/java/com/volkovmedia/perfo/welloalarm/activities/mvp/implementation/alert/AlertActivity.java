package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alert;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.general.GeneralMethods;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.logic.WelloCalendar;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import static com.volkovmedia.perfo.welloalarm.objects.Alarm.KEY_ALARM;

public class AlertActivity extends AppCompatActivity {

    private Alarm mAlarm;

    private MediaPlayer aMediaPlayer;
    private Vibrator aVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        turnOnScreen();
        setContentView(R.layout.activity_alert);

        mAlarm = getIntent().getParcelableExtra(KEY_ALARM);

        initViews();
        initPlayer();
        prepareForCalls();
    }

    private void initViews() {
        WelloCalendar calendar = new WelloCalendar();

        TextView timeView = findViewById(R.id.alarmact_time),
                nameView = findViewById(R.id.alarmact_name);

        String name = mAlarm.getName();
        if (name.isEmpty()) nameView.setVisibility(View.GONE);
        else nameView.setText(name);

        timeView.setText(GeneralMethods.getTimeText(calendar.getCurrentHour(), calendar.getCurrentMinute()));
        timeView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_flashing));
    }

    private void initPlayer() {
        aMediaPlayer = new MediaPlayer();

        if (mAlarm.isVibrate()) {
            aVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {1000, 200, 200, 200};

            if (aVibrator != null) {
                aVibrator.vibrate(pattern, 0);
            }
        }
        try {
            String sound = mAlarm.getSound();
            if (TextUtils.isEmpty(sound)) sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();

            aMediaPlayer.setVolume(1.0f, 1.0f);
            aMediaPlayer.setDataSource(this, Uri.parse(sound));
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
        WelloAlarmManager alarmManager = new WelloAlarmManager();
//        alarmManager.setSnoozedAlarm(mAlarm);

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
        } catch (Exception ignored) { }

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
                        } catch (IllegalStateException ignored) {
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        try {
                            aMediaPlayer.start();
                        } catch (IllegalStateException ignored) {
                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        if (telephonyManager != null) {
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}