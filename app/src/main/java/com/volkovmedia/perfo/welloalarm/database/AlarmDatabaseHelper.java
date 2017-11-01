package com.volkovmedia.perfo.welloalarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ALARM_ENABLED;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ALARM_HOURS;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ALARM_ID;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ALARM_MINUTES;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ALARM_NAME;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ALARM_SOUND;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ALARM_VIBRATE;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_ID;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.FIELD_VALUE;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.TABLE_ALARMS;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.TABLE_DAYS;
import static com.volkovmedia.perfo.welloalarm.database.DBConstants.TABLE_WEEKS;
import static com.volkovmedia.perfo.welloalarm.general.Constants.DAYS_TYPES;
import static com.volkovmedia.perfo.welloalarm.general.Constants.INT_NO_VALUE;
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEKS_TYPES;
import static com.volkovmedia.perfo.welloalarm.general.GeneralMethods.castToBooleanArray;

public class AlarmDatabaseHelper {

    private SQLiteHelper mDatabaseHelper;

    public AlarmDatabaseHelper(Context context) {
        mDatabaseHelper = new SQLiteHelper(context);
    }

    public int saveAlarm(Alarm alarm) {
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        int id = alarm.getId();

        if (id != INT_NO_VALUE) contentValues.put(FIELD_ID, id);

        contentValues.put(FIELD_ALARM_HOURS, alarm.getHours());
        contentValues.put(FIELD_ALARM_MINUTES, alarm.getMinutes());
        contentValues.put(FIELD_ALARM_NAME, alarm.getName());
        contentValues.put(FIELD_ALARM_SOUND, alarm.getSound());
        contentValues.put(FIELD_ALARM_ENABLED, alarm.isEnabled() ? 1 : 0);
        contentValues.put(FIELD_ALARM_VIBRATE, alarm.isVibrate() ? 1 : 0);

        id = (int) database.insertWithOnConflict(TABLE_ALARMS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        saveIntegerArray(database, id, alarm.getWeeks(), TABLE_WEEKS);
        saveIntegerArray(database, id, alarm.getDays(), TABLE_DAYS);

        database.close();

        return id;
    }

    public UniqueList<Alarm> readAlarms() {
        return readAlarmsWithCondition(null, null);
    }

    public Alarm readAlarm(int id) {
        return readAlarmsWithCondition(FIELD_ID + " = ?", new String[]{String.valueOf(id)}).get(0);
    }

    private UniqueList<Alarm> readAlarmsWithCondition(String query, String[] args) {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        UniqueList<Alarm> alarms = new UniqueList<>();

        Cursor cursor = database.query(TABLE_ALARMS, null, query, args, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(FIELD_ID)),
                        hours = cursor.getInt(cursor.getColumnIndex(FIELD_ALARM_HOURS)),
                        minutes = cursor.getInt(cursor.getColumnIndex(FIELD_ALARM_MINUTES));

                String name = cursor.getString(cursor.getColumnIndex(FIELD_ALARM_NAME)),
                        sound = cursor.getString(cursor.getColumnIndex(FIELD_ALARM_SOUND));

                boolean enabled = cursor.getInt(cursor.getColumnIndex(FIELD_ALARM_ENABLED)) == 1,
                        vibrate = cursor.getInt(cursor.getColumnIndex(FIELD_ALARM_VIBRATE)) == 1;

                LinkedHashMap<Integer, Integer> weeksMap = readIntegerArray(database, id, TABLE_WEEKS),
                        daysMap = readIntegerArray(database, id, TABLE_DAYS);

                boolean[] weeks = castToBooleanArray(weeksMap, WEEKS_TYPES),
                        days = castToBooleanArray(daysMap, DAYS_TYPES);

                Alarm alarm = new Alarm(id, hours, minutes, name, sound, enabled, vibrate, weeks, days);
                alarms.add(alarm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return alarms;
    }

    public void removeAlarm(Alarm alarm) {
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        database.delete(TABLE_ALARMS, FIELD_ID + " = ?", new String[] {String.valueOf(alarm.getId())});
        removeIntegerArray(database, alarm.getId(), TABLE_WEEKS);
        removeIntegerArray(database, alarm.getId(), TABLE_DAYS);
    }

    private void saveIntegerArray(SQLiteDatabase database, int alarmId, boolean[] values, String tableName) {
        removeIntegerArray(database, alarmId, tableName);

        for (int i = 0; i < values.length; i++) {
            if (values[i]) {
                saveInteger(database, alarmId, i, tableName);
            }
        }
    }

    private void saveInteger(SQLiteDatabase database, int alarmId, int value, String tableName) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FIELD_ALARM_ID, alarmId);
        contentValues.put(FIELD_VALUE, value);

        database.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void removeIntegerArray(SQLiteDatabase database, int alarmId, String tableName) {
        database.delete(tableName, FIELD_ALARM_ID + " = ?", new String[]{String.valueOf(alarmId)});
    }

    private LinkedHashMap<Integer, Integer> readIntegerArray(SQLiteDatabase database, int alarmId, String tableName) {
        LinkedHashMap<Integer, Integer> intArray = new LinkedHashMap<>();

        Cursor cursor = database.query(tableName, null, FIELD_ALARM_ID + " = ?", new String[]{String.valueOf(alarmId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(FIELD_ID)),
                        value = cursor.getInt(cursor.getColumnIndex(FIELD_VALUE));

                intArray.put(id, value);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return intArray;
    }

}