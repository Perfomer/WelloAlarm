package com.volkovmedia.perfo.welloalarm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
import static com.volkovmedia.perfo.welloalarm.general.Constants.WEEKS_TYPES;

class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "welloalarmdb";
    private static final int DATABASE_VERSION = 3;

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `" + TABLE_ALARMS + "` (\n" +
                "\t`" + FIELD_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`" + FIELD_ALARM_HOURS + "`\tINTEGER NOT NULL,\n" +
                "\t`" + FIELD_ALARM_MINUTES + "`\tINTEGER NOT NULL,\n" +
                "\t`" + FIELD_ALARM_NAME + "`\tTEXT,\n" +
                "\t`" + FIELD_ALARM_SOUND + "`\tTEXT,\n" +
                "\t`" + FIELD_ALARM_ENABLED + "`\tINTEGER NOT NULL,\n" +
                "\t`" + FIELD_ALARM_VIBRATE + "`\tINTEGER\n" +
                ");");

        db.execSQL("CREATE TABLE `" + TABLE_WEEKS + "` (\n" +
                "\t`" + FIELD_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`" + FIELD_VALUE + "`\tINTEGER NOT NULL CHECK (" + FIELD_VALUE + " < " + WEEKS_TYPES + "),\n" +
//                "\t`" + FIELD_ALARM_ID + "`\tINTEGER NOT NULL,\n" +
                "\t`" + FIELD_ALARM_ID + "`\t REFERENCES " + TABLE_ALARMS + "(" + FIELD_ID + ")\n" +
                ");");

        db.execSQL("CREATE TABLE `" + TABLE_DAYS + "` (\n" +
                "\t`" + FIELD_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`" + FIELD_VALUE + "`\tINTEGER NOT NULL,\n" +
                "\t`" + FIELD_ALARM_ID + "`\tINTEGER NOT NULL\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_ALARMS);
        db.execSQL("drop table if exists " + TABLE_WEEKS);
        db.execSQL("drop table if exists " + TABLE_DAYS);

        onCreate(db);
    }
}