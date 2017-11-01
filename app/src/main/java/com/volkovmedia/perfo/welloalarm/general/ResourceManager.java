package com.volkovmedia.perfo.welloalarm.general;

import android.content.res.Resources;

import com.volkovmedia.perfo.welloalarm.R;

import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_FRIDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_MONDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_SATURDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_SUNDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_THURSDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_TUESDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_WEDNESDAY;
import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.isWeekEven;

public class ResourceManager {

    public static String getDayName(Resources res, int day, boolean longName) {
        switch (day) {
            case DAY_MONDAY:
                return longName ? res.getString(R.string.monday) : res.getString(R.string.mon);
            case DAY_TUESDAY:
                return longName ? res.getString(R.string.tuesday) : res.getString(R.string.tue);
            case DAY_WEDNESDAY:
                return longName ? res.getString(R.string.wednesday) : res.getString(R.string.wed);
            case DAY_THURSDAY:
                return longName ? res.getString(R.string.thursday) : res.getString(R.string.thu);
            case DAY_FRIDAY:
                return longName ? res.getString(R.string.friday) : res.getString(R.string.fri);
            case DAY_SATURDAY:
                return longName ? res.getString(R.string.saturday) : res.getString(R.string.sat);
            case DAY_SUNDAY:
                return longName ? res.getString(R.string.sunday) : res.getString(R.string.sun);
            default:
                return res.getString(R.string.not_found);
        }
    }


    public static String getWeekName(Resources res, int week) {
        return isWeekEven(week) ? res.getString(R.string.even) : res.getString(R.string.odd);
    }

}
