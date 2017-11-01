package com.volkovmedia.perfo.welloalarm.logic;

import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import java.util.ArrayList;
import java.util.Collections;

import static com.volkovmedia.perfo.welloalarm.general.Constants.DAYS_IN_WEEK;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_MONDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAY_SUNDAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.HOURS_IN_DAY;
import static com.volkovmedia.perfo.welloalarm.general.Constants.MILLISECONDS_IN_SECOND;
import static com.volkovmedia.perfo.welloalarm.general.Constants.MINUTES_IN_HOUR;
import static com.volkovmedia.perfo.welloalarm.general.Constants.SECONDS_IN_MINUTE;
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEK_EVEN;
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEK_ODD;

public class TimeManager {

    public static long getClosestTimeDifference(Alarm alarm) {
        WelloCalendar manager = new WelloCalendar();
        return getTimeDifference(manager, alarm, findClosestDay(manager, alarm));
    }

    public static ArrayList<Long> getTimeDifferences(Alarm alarm) {
        return getTimeDifferences(new WelloCalendar(), alarm);
    }

    public static ArrayList<Long> getTimeDifferences(WelloCalendar now, Alarm alarm) {
        boolean days[] = alarm.getDays();
        ArrayList<Long> timeDifferences = new ArrayList<>();

        for (int i = 0; i < DAYS_IN_WEEK; i++)
            if (days[i]) timeDifferences.add(getTimeDifference(now, alarm, i + 1));

        Collections.sort(timeDifferences);

        return timeDifferences;
    }

    private static long getTimeDifference(WelloCalendar now, Alarm alarm, int destinationDay) {
        boolean[] weeks = alarm.getWeeks();
        boolean isCurrentWeekEven = now.isCurrentWeekEven(),
                isAlarmWeekEven = weeks[WEEK_EVEN],
                isAlarmWeekOdd = weeks[WEEK_ODD],
                isAlarmWeekly = isAlarmWeekEven == isAlarmWeekOdd;

        int today = now.getCurrentDay(), daysCount = 0,
                currentHour = now.getCurrentHour(), currentMinute = now.getCurrentMinute(),
                currentSecond = now.getCurrentSecond(),
                destinationHour = alarm.getHours(), destinationMinute = alarm.getMinutes(),
                destinationSecond = 0,
                hoursCount = 0, minutesCount = 0, secondsCount = 0;


        if (destinationDay == today && (currentHour * MINUTES_IN_HOUR * SECONDS_IN_MINUTE + currentMinute * SECONDS_IN_MINUTE + currentSecond) < (destinationHour * MINUTES_IN_HOUR * SECONDS_IN_MINUTE + destinationMinute * SECONDS_IN_MINUTE))
            return castToMillis(0, 0, 0, (destinationHour * MINUTES_IN_HOUR * SECONDS_IN_MINUTE + destinationMinute * SECONDS_IN_MINUTE) - (currentHour * MINUTES_IN_HOUR * SECONDS_IN_MINUTE + currentMinute * SECONDS_IN_MINUTE + currentSecond)); //costyle

        if (destinationDay > today) {
            daysCount = destinationDay - today;
            if (!isAlarmWeekly && (isCurrentWeekEven ? isAlarmWeekOdd : isAlarmWeekEven))
                daysCount += DAYS_IN_WEEK;

        } else if (destinationDay < today) {
            daysCount = DAYS_IN_WEEK - today + destinationDay;
            if (!isAlarmWeekly && (isCurrentWeekEven ? isAlarmWeekEven : isAlarmWeekOdd))
                daysCount += DAYS_IN_WEEK;
        }

        if (destinationHour > currentHour) {
            hoursCount = destinationHour - currentHour;
        } else {
            hoursCount = HOURS_IN_DAY - currentHour + destinationHour;
            if (hoursCount > HOURS_IN_DAY) {
                hoursCount -= HOURS_IN_DAY;
                daysCount++;
            }
            if (daysCount > 0) daysCount--;
        }

        if (destinationMinute > currentMinute)
            minutesCount = destinationMinute - currentMinute;
        else {
            minutesCount = MINUTES_IN_HOUR - currentMinute + destinationMinute;
            if (minutesCount > MINUTES_IN_HOUR) {
                minutesCount -= MINUTES_IN_HOUR;
                hoursCount++;
            }
            if (hoursCount > 0) hoursCount--;
        }


        secondsCount = SECONDS_IN_MINUTE - currentSecond;
        if (secondsCount > SECONDS_IN_MINUTE) {
            secondsCount -= SECONDS_IN_MINUTE;
            minutesCount++;
        }
        if (minutesCount > 0) minutesCount--;


        return castToMillis(daysCount, hoursCount, minutesCount, secondsCount);
    }


    public static int findClosestDay(WelloCalendar tm, Alarm alarm) {
        int day = -1, today = tm.getCurrentDay();

        boolean days[] = alarm.getDays(),
                weeks[] = alarm.getWeeks(),
                isToday = (tm.getCurrentHour() * MINUTES_IN_HOUR + tm.getCurrentMinute()) <= (alarm.getHours() * MINUTES_IN_HOUR + alarm.getMinutes()),
                isAlarmWeekEven = weeks[WEEK_EVEN],
                isAlarmWeekOdd = weeks[WEEK_ODD];

        if (!(isAlarmWeekEven && isAlarmWeekOdd) && (tm.isCurrentWeekEven() ? isAlarmWeekOdd : isAlarmWeekEven))
            for (int i = 0; i < days.length; i++) if (days[i]) return i + 1;

        if (days[today - 1] && isToday) return today;
        for (int i = today; i < days.length; i++) if (days[i]) return i + 1;
        for (int i = 0; i < today; i++) if (days[i]) return i + 1;

        return isToday ? today : getNextDay(today);
    }

    private static long castToMillis(int days, int hours, int minutes, int seconds) {
        return (long) (days * HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND +
                hours * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND +
                minutes * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND) +
                seconds * MILLISECONDS_IN_SECOND;
    }

    public static int getNextDay(int currentDay) {
        return (currentDay == DAY_SUNDAY) ? DAY_MONDAY : currentDay + 1;
    }

    public static boolean isWeekEven(int week) {
        return (week % 2 == 0);
    }

}