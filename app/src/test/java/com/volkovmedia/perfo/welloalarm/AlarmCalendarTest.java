package com.volkovmedia.perfo.welloalarm;

import com.volkovmedia.perfo.welloalarm.logic.TimeManager;
import com.volkovmedia.perfo.welloalarm.logic.WelloCalendar;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.volkovmedia.perfo.welloalarm.logic.TimeManager.getNextDay;
import static com.volkovmedia.perfo.welloalarm.logic.WelloAlarmManager.findNearestAlarm;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AlarmCalendarTest {

    private static WelloCalendar calendar;
    private static Alarm nearestAlarm;

    @BeforeClass
    public static void init() {
        calendar = new WelloCalendar();
        nearestAlarm = new Alarm(1, 59, "Name",
                null, true, false,
                new boolean[]{false, true},
                new boolean[]{false, false, true, false, false, false, false});
        //MockitoAnnotations.initMocks(this);

    }

    @Test
    public void nearestAlarm() {
        List<Alarm> alarms = Arrays.asList(
                new Alarm(10, 10, "Name",
                        null, true, false,
                        new boolean[]{true, true},
                        new boolean[]{true, true, true, true, true, false, false}),

                new Alarm(15, 41, "Name",
                        null, true, false,
                        new boolean[]{false, true},
                        new boolean[]{false, false, false, false, false, false, false}),

                new Alarm(19, 22, "Name",
                        null, true, false,
                        new boolean[]{true, false},
                        new boolean[]{true, true, true, true, true, true, true}),

                new Alarm(0, 0, "Name",
                        null, true, false,
                        new boolean[]{true, true},
                        new boolean[]{false, false, true, false, false, false, false}),

                new Alarm(0, 40, "Name",
                        null, true, false,
                        new boolean[]{true, false},
                        new boolean[]{false, true, true, true, false, false, false}),

                nearestAlarm);

        //assertEquals(nearestAlarm, findNearestAlarm(alarms));
    }

    @Test
    public void closestDaySearch_once() {
        Alarm alarm = new Alarm(10, 10, "Name",
                null, true, false,
                new boolean[]{false, false},
                new boolean[]{false, false, false, false, false, false, false});

        if (calendar.getCurrentHour() < alarm.getHours() || (calendar.getCurrentHour() == alarm.getHours()) && calendar.getCurrentMinute() < alarm.getMinutes()) {
            assertEquals(calendar.getCurrentDay(), TimeManager.findClosestDay(calendar, alarm));
        } else {
            assertEquals(getNextDay(calendar.getCurrentDay()), TimeManager.findClosestDay(calendar, alarm));
        }
    }

    @Test
    public void closestDaySearch_1() {
        Alarm alarm = new Alarm(10, 10, "Name",
                null, true, false,
                new boolean[]{false, true},
                new boolean[]{false, true, false, false, false, false, false});

        assertEquals(2, TimeManager.findClosestDay(calendar, alarm));
    }

}
