package com.volkovmedia.perfo.welloalarm;

import android.support.annotation.NonNull;

import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.logic.TimeManager;
import com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager;
import com.volkovmedia.perfo.welloalarm.logic.WelloCalendar;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.volkovmedia.perfo.welloalarm.general.Constants.DAYS_TYPES;
import static com.volkovmedia.perfo.welloalarm.general.Constants.HOURS_IN_DAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.MINUTES_IN_HOUR;
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEKS_TYPES;
import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.getClosestTimeDifference;
import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.getNextDay;
import static com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager.findNearestAlarm;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AlarmCalendarTest {

    private static WelloCalendar calendar;
    private static Alarm nearestAlarm;

    private static UniqueList<Alarm> alarmUniqueList;

    @BeforeClass
    public static void init() {
        calendar = new WelloCalendar();
        nearestAlarm = new Alarm(1, 59, "Name",
                null, true, false,
                new boolean[]{false, true},
                new boolean[]{false, false, true, false, false, false, false});

        alarmUniqueList = new UniqueList<>();

        for (int i = 0; i < 1500000; i++) {
            alarmUniqueList.add(generateRandomAlarm());
        }

        //MockitoAnnotations.initMocks(this);

    }

//    @Test
//    public void nearestAlarm() {
//        List<Alarm> alarms = Arrays.asList(
//                new Alarm(10, 10, "Name",
//                        null, true, false,
//                        new boolean[]{true, true},
//                        new boolean[]{true, true, true, true, true, false, false}),
//
//                new Alarm(15, 41, "Name",
//                        null, true, false,
//                        new boolean[]{false, true},
//                        new boolean[]{false, false, false, false, false, false, false}),
//
//                new Alarm(19, 22, "Name",
//                        null, true, false,
//                        new boolean[]{true, false},
//                        new boolean[]{true, true, true, true, true, true, true}),
//
//                new Alarm(0, 0, "Name",
//                        null, true, false,
//                        new boolean[]{true, true},
//                        new boolean[]{false, false, true, false, false, false, false}),
//
//                new Alarm(0, 40, "Name",
//                        null, true, false,
//                        new boolean[]{true, false},
//                        new boolean[]{false, true, true, true, false, false, false}),
//
//                nearestAlarm);
//
//        //assertEquals(nearestAlarm, findNearestAlarm(alarms));
//    }

//    @Test
//    public void closestDaySearch_once() {
//        Alarm alarm = new Alarm(10, 10, "Name",
//                null, true, false,
//                new boolean[]{false, false},
//                new boolean[]{false, false, false, false, false, false, false});
//
//        if (calendar.getCurrentHour() < alarm.getHours() || (calendar.getCurrentHour() == alarm.getHours()) && calendar.getCurrentMinute() < alarm.getMinutes()) {
//            assertEquals(calendar.getCurrentDay(), TimeManager.findClosestDay(calendar, alarm));
//        } else {
//            assertEquals(getNextDay(calendar.getCurrentDay()), TimeManager.findClosestDay(calendar, alarm));
//        }
//    }
//
//    @Test
//    public void closestDaySearch_1() {
//        Alarm alarm = new Alarm(10, 10, "Name",
//                null, true, false,
//                new boolean[]{false, true},
//                new boolean[]{false, true, false, false, false, false, false});
//
//        assertEquals(2, TimeManager.findClosestDay(calendar, alarm));
//    }

    @Test
    public void findNearestAlarm() {
        assertEquals(WelloAlarmManager.findNearestAlarm(alarmUniqueList), oldFindNearestAlarm(alarmUniqueList));
    }

    public static Alarm findNearestAlarm(UniqueList<Alarm> alarmList) {
        alarmList = getEnabledAlarms(alarmList);

        switch (alarmList.size()) {
            case 0:  return null;
            case 1:  return alarmList.get(0);
            default: return findNearestAlarm(alarmList, alarmList.get(0));
        }
    }

    public static Alarm getNextAlarm(UniqueList<Alarm> alarmList, Alarm alarm) {
        if (alarm == null || !alarm.isEnabled()) return findNearestAlarm(alarmList);

        return findNearestAlarm(alarmList, alarm);
    }

    private static Alarm findNearestAlarm(UniqueList<Alarm> alarmList, @NonNull Alarm alarm) {
        Alarm nearestAlarm = alarm;
        long nearestTimeDifference = getClosestTimeDifference(nearestAlarm);

        for (int i = 1; i < alarmList.size(); i++) {
            Alarm currentAlarm = alarmList.get(i);

            long currentTimeDifference = getClosestTimeDifference(currentAlarm);

            if (currentTimeDifference < nearestTimeDifference) {
                nearestAlarm = currentAlarm;
                nearestTimeDifference = currentTimeDifference;
            }
        }

        return nearestAlarm;
    }

    public static Alarm oldFindNearestAlarm(UniqueList<Alarm> alarmList) {
        Alarm nearestAlarm = null;
        int position = 0;

        for (; position < alarmList.size(); position++) {
            Alarm currentAlarm = alarmList.get(position);
            if (currentAlarm.isEnabled()) {
                nearestAlarm = currentAlarm;
                break;
            }
        }

        long nearestTimeDifference = getClosestTimeDifference(nearestAlarm);

        for (position++; position < alarmList.size(); position++) {
            Alarm currentAlarm = alarmList.get(position);
            if (!currentAlarm.isEnabled()) continue;

            long currentTimeDifference = getClosestTimeDifference(currentAlarm);

            if (nearestTimeDifference > currentTimeDifference) {
                nearestAlarm = currentAlarm;
                nearestTimeDifference = currentTimeDifference;
            }
        }

        return nearestAlarm;
    }

    private static UniqueList<Alarm> getEnabledAlarms(UniqueList<Alarm> alarmList) {
        UniqueList<Alarm> alarms = new UniqueList<>();

        for (int i = 0; i < alarmList.size(); i++) {
            Alarm currentAlarm = alarmList.get(i);
            if (currentAlarm.isEnabled()) alarms.add(currentAlarm);
        }

        return alarms;
    }

    private static Alarm generateRandomAlarm() {
        Random random = new Random();

        return new Alarm(random.nextInt(HOURS_IN_DAY), random.nextInt(MINUTES_IN_HOUR), "Name",
                null, true, false,
                generateBooleanArray(WEEKS_TYPES),
                generateBooleanArray(DAYS_TYPES));
    }

    private static boolean[] generateBooleanArray(int size) {
        boolean[] result = new boolean[size];

        for (int i = 0; i < size; i++) {
            result[i] = new Random().nextBoolean();
        }

        return result;
    }

}
