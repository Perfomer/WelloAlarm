package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.activities.wraps.WelloToolbarActivity;
import com.volkovmedia.perfo.welloalarm.general.GeneralMethods;
import com.volkovmedia.perfo.welloalarm.logic.WelloCalendar;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import static com.volkovmedia.perfo.welloalarm.general.Constants.INT_NO_VALUE;
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEK_EVEN;
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEK_ODD;

public class SettingsActivity extends WelloToolbarActivity {

    private boolean alarmDays[] = {false, false, false, false, false, false, false},
            alarmWeekOdd = false, alarmWeekEven = false,
            editingMode = false, isAlarmEdited = false, isAlarmEnabled = true;

    private int alarmId;

    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private WelloCalendar alarmTimeManager;
    private GregorianCalendar alarmCalendar;

    private GridLayout glDays, glWeeks;
    private TextView tvDay, tvWeek;
    private TickerView tvTime;
    private EditText etName;
    private AlertDialog.Builder adQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        displayArrowBack();

        alarmTimeManager = new WelloCalendar();
        alarmCalendar = alarmTimeManager.getCalendar();

        initViews();
        handleIncomingIntent(getIntent());
    }

    private void handleIncomingIntent(Intent data) {
        Alarm alarm = data.getParcelableExtra(Alarm.KEY_ALARM);
        editingMode = (alarm != null);

        if (editingMode) {
            setToolbarText("Настройка будильника");
            boolean[] days = alarm.getDays(), weeks = alarm.getWeeks();
            for (int i = 0; i < days.length; i++)
                if (days[i]) {
                    alarmDays[i] = days[i];
                    activateDateView(glDays.findViewWithTag(String.valueOf(i + 1)), false);
                }

            tvTime.setText(GeneralMethods.getTimeText(alarm.getHours(), alarm.getMinutes()));
            etName.setText(alarm.getName());

            alarmCalendar.set(Calendar.HOUR_OF_DAY, alarm.getHours());
            alarmCalendar.set(Calendar.MINUTE, alarm.getMinutes());

            alarmWeekEven = weeks[WEEK_EVEN];
            if (alarmWeekEven) activateDateView(glWeeks.findViewWithTag("even"), false);

            alarmWeekOdd = weeks[WEEK_ODD];
            if (alarmWeekOdd) activateDateView(glWeeks.findViewWithTag("odd"),  false);

            alarmId = alarm.getId();
            isAlarmEnabled = alarm.isEnabled();
        }

    }

    @Override
    public void onBackPressed() {
        if (isAlarmEdited) adQuit.show();
        else finish();
    }

    private Alarm saveAlarm() {
        int id = editingMode ? alarmId : INT_NO_VALUE;

        return new Alarm(id, alarmTimeManager.getCurrentHour(), alarmTimeManager.getCurrentMinute(), etName.getText().toString(), null, isAlarmEnabled, true, new boolean[] {alarmWeekOdd, alarmWeekEven}, alarmDays);
    }

    private void saveAlarmAndExit() {
        Intent intent = new Intent();
        intent.putExtra(Alarm.KEY_ALARM, saveAlarm());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void refreshCurrentTimeViews() {
        tvDay.setText(getString(R.string.stn_today) + " " + alarmTimeManager.getCurrentDayName(getResources()));
        tvWeek.setText(getString(R.string.stn_now) + " " + alarmTimeManager.getCurrentWeekParityName(getResources()) + " " + getString(R.string.stn_week));
        setInitialDateTime();
    }

    private void setInitialDateTime() {
        tvTime.setText(GeneralMethods.getTimeText(alarmTimeManager.getCurrentHour(), alarmTimeManager.getCurrentMinute()));
    }

    public void onDayButtonClick(View v) {
        isAlarmEdited = true;
        int day = Integer.parseInt(v.getTag().toString()) - 1;

        activateDateView(v, alarmDays[day]);
        alarmDays[day] = !alarmDays[day];
    }

    public void onWeekButtonClick(View v) {
        isAlarmEdited = true;
        boolean isWeekEven = v.getTag().toString().equals("even");

        activateDateView(v, isWeekEven ? alarmWeekEven : alarmWeekOdd);

        if (isWeekEven) alarmWeekEven = !alarmWeekEven;
        else alarmWeekOdd = !alarmWeekOdd;
    }

    /**
     * ACTIVATION TEXTCHECKBOXVIEW METHOD
     * [Changes state of view to normal or active]
     *
     * @param v      — View, которое будет изменяться.
     * @param normal — если необходимо перевести View в нормальное состояние, то 1, если в активное то 0.
     */
    private void activateDateView(View v, boolean normal) {
        TransitionDrawable transition = (TransitionDrawable) v.getBackground();
        int animSpeed = getResources().getInteger(R.integer.animspeed_short);

        if (normal) transition.reverseTransition(animSpeed);
        else transition.startTransition(animSpeed);

        ((TextView) v).setTextColor(
                ContextCompat.getColor(
                        this,
                        (normal ? android.R.color.tertiary_text_dark : android.R.color.white)));
    }

    public void onDeclineButtonClick(View v) {
        if (isAlarmEdited) adQuit.show();
        else finish();
    }

    public void onAcceptButtonClick(View v) {
        saveAlarmAndExit();
    }

    public void onChangeTimeClick(View v) {
        new TimePickerDialog(this, timeSetListener,
                alarmCalendar.get(Calendar.HOUR_OF_DAY),
                alarmCalendar.get(Calendar.MINUTE), true)
                .show();
    }

    private void initViews() {
        tvTime = (TickerView) findViewById(R.id.alarmstn_time);
        tvTime.setCharacterList(TickerUtils.getDefaultNumberList());

        tvDay   = (TextView)   findViewById(R.id.alarmstn_day);
        tvWeek  = (TextView)   findViewById(R.id.alarmact_week);
        etName  = (EditText)   findViewById(R.id.alarmstn_name);
        glWeeks = (GridLayout) findViewById(R.id.alarmstn_weeks);
        glDays  = (GridLayout) findViewById(R.id.alarmstn_days);

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                alarmCalendar.set(Calendar.MINUTE, minute);
                setInitialDateTime();
                isAlarmEdited = true;
            }
        };

        refreshCurrentTimeViews();

        adQuit = new AlertDialog.Builder(this);
        adQuit.setTitle(getString(R.string.stn_confirmation));  // заголовок
        adQuit.setMessage(getString(R.string.stn_confirmation_txt)); // сообщение
        adQuit.setCancelable(true);

        adQuit.setPositiveButton(getString(R.string.stn_save_alarm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(SettingsActivity.this, "Сохранено)0)", Toast.LENGTH_LONG).show();
                saveAlarmAndExit();
            }
        });
        adQuit.setNegativeButton(getString(R.string.stn_quit_without_saving), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(SettingsActivity.this, "Не сохранено((99((", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

}