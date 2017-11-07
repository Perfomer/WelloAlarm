package com.volkovmedia.perfo.welloalarm.database;

public final class DBConstants {

    static final String TABLE_ALARMS = "alarms", TABLE_DAYS = "days", TABLE_WEEKS = "weeks";

    static final String FIELD_ID = "_id";

    static final String FIELD_ALARM_ID = "alarm_id", FIELD_ALARM_HOURS = "hours", FIELD_ALARM_MINUTES  = "minutes",
            FIELD_ALARM_NAME = "name", FIELD_ALARM_SOUND = "sound", FIELD_ALARM_ENABLED = "enabled",
            FIELD_ALARM_VIBRATE = "vibrate";

    static final String FIELD_VALUE = "value";

    public static final int VALUE_DAY_MONDAY = 0, VALUE_DAY_TUESDAY = 1, VALUE_DAY_WEDNESDAY = 2,
            VALUE_DAY_THURSDAY = 3, VALUE_DAY_FRIDAY = 4, VALUE_DAY_SATURDAY = 5,
            VALUE_DAY_SUNDAY = 6;

    public static final int VALUE_WEEK_ODD = 0, VALUE_WEEK_EVEN = 1;

}