package com.volkovmedia.perfo.welloalarm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "welloalarmdb";
    private static final int DATABASE_VERSION = 2;

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `" + DBConstants.TABLE_ALARMS + "` (\n" +
                "\t`" + DBConstants.FIELD_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`" + DBConstants.FIELD_ALARM_HOURS + "`\tINTEGER NOT NULL,\n" +
                "\t`" + DBConstants.FIELD_ALARM_MINUTES + "`\tINTEGER NOT NULL,\n" +
                "\t`" + DBConstants.FIELD_ALARM_NAME + "`\tTEXT,\n" +
                "\t`" + DBConstants.FIELD_ALARM_SOUND + "`\tTEXT,\n" +
                "\t`" + DBConstants.FIELD_ALARM_ENABLED + "`\tINTEGER,\n" +
                "\t`" + DBConstants.FIELD_ALARM_VIBRATE + "`\tINTEGER\n" +
                ");");

        db.execSQL("CREATE TABLE `" + DBConstants.TABLE_WEEKS + "` (\n" +
                "\t`" + DBConstants.FIELD_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`" + DBConstants.FIELD_VALUE + "`\tINTEGER NOT NULL,\n" +
                "\t`" + DBConstants.FIELD_ALARM_ID + "`\tINTEGER NOT NULL\n" +
                ");");

        db.execSQL("CREATE TABLE `" + DBConstants.TABLE_DAYS + "` (\n" +
                "\t`" + DBConstants.FIELD_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`" + DBConstants.FIELD_VALUE + "`\tINTEGER NOT NULL,\n" +
                "\t`" + DBConstants.FIELD_ALARM_ID + "`\tINTEGER NOT NULL\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DBConstants.TABLE_ALARMS);
        db.execSQL("drop table if exists " + DBConstants.TABLE_WEEKS);
        db.execSQL("drop table if exists " + DBConstants.TABLE_DAYS);

        onCreate(db);
    }
}