package com.volkovmedia.perfo.welloalarm.general;

import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.getClosestTimeDifference;

public class GeneralMethods {

    public static boolean[] castToBooleanArray(LinkedHashMap<Integer, Integer> map, int size) {
        boolean[] result = new boolean[size];

        for (int value : new ArrayList<>(map.values())) {
            result[value] = true;
        }

        return result;
    }

    public static String getTimeText(int hours, int minutes) {
        return String.valueOf(hours) + ":" + ((minutes >= 10) ? String.valueOf(minutes) : "0" + String.valueOf(minutes));
    }

    public static int getTrueItemsCount(boolean[] items) {
        int count = 0;
        for (boolean item : items)
            if (item) count++;
        return count;
    }

    public static boolean hasEnabledAlarms(UniqueList<Alarm> alarms) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).isEnabled()) return true;
        }

        return false;
    }

    public static boolean isAlarmDisposable(Alarm alarm) { return getTrueItemsCount(alarm.getDays()) == 0;}
}
