package com.volkovmedia.perfo.welloalarm.general;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
}
