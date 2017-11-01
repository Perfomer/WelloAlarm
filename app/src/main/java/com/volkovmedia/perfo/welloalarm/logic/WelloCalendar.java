package com.volkovmedia.perfo.welloalarm.logic;

import android.content.res.Resources;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.volkovmedia.perfo.welloalarm.general.Constants.DAYS_IN_WEEK;
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEKS_IN_YEAR;
import static com.volkovmedia.perfo.welloalarm.general.ResourceManager.getDayName;
import static com.volkovmedia.perfo.welloalarm.general.ResourceManager.getWeekName;
import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.isWeekEven;

public class WelloCalendar {

    private GregorianCalendar mCalendar;

    public WelloCalendar() {
        mCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
    }

    public int getCurrentHour() {
        return mCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getCurrentMinute() {
        return mCalendar.get(Calendar.MINUTE);
    }

    public int getCurrentSecond() {
        return mCalendar.get(Calendar.SECOND);
    }

    public int getCurrentDay() {
        int currentDay = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (currentDay == 0) currentDay += DAYS_IN_WEEK;

        return currentDay;
    }

    private int getCurrentWeekOfYear() {
        int currentWeek = mCalendar.get(Calendar.WEEK_OF_YEAR) - 1; //Понятия не имею, почему ГрегоринаскийКалендарь возвращает следующую неделю, а не текущую...
        if (currentWeek == 0) currentWeek += WEEKS_IN_YEAR;

        return currentWeek;
    }

    public boolean isCurrentWeekEven() {
        return isWeekEven(getCurrentWeekOfYear());
    }

    public String getCurrentDayName(Resources res) {
        return getDayName(res, getCurrentDay(), true);
    }

    public String getCurrentWeekParityName(Resources res) {
        return getWeekName(res, getCurrentWeekOfYear());
    }

    public GregorianCalendar getCalendar() {
        return mCalendar;
    }

}