package com.volkovmedia.perfo.welloalarm.objects;

import android.os.Parcel;
import android.os.Parcelable;

import static com.volkovmedia.perfo.welloalarm.general.Constants.INT_NO_VALUE;

public class Alarm implements Parcelable, DataContainer {

    public final static String KEY_ALARM = "alarm";

    private int mIdentifier = INT_NO_VALUE;
    private int mHours, mMinutes;

    private String mName;
    private String mSound;

    private boolean mEnabled, mVibrate;
    private boolean[] mWeeks, mDays;

    public Alarm(int identifier, int hours, int minutes, String name, String sound, boolean enabled, boolean vibrate, boolean[] weeks, boolean[] days) {
        init(identifier, hours, minutes, name, sound, enabled, vibrate, weeks, days);
    }

    public Alarm(int hours, int minutes, String name, String sound, boolean enabled, boolean vibrate, boolean[] weeks, boolean[] days) {
        init(hours, minutes, name, sound, enabled, vibrate, weeks, days);
    }

    protected Alarm(Parcel in) {
        mIdentifier = in.readInt();
        mHours = in.readInt();
        mMinutes = in.readInt();
        mName = in.readString();
        mSound = in.readString();
        mEnabled = in.readByte() != 0;
        mVibrate = in.readByte() != 0;
        mWeeks = in.createBooleanArray();
        mDays = in.createBooleanArray();
    }

    private void init(int hours, int minutes, String name, String sound, boolean enabled, boolean vibrate, boolean[] weeks, boolean[] days) {
        this.mHours = hours;
        this.mMinutes = minutes;
        this.mName = name;
        this.mSound = sound;
        this.mEnabled = enabled;
        this.mVibrate = vibrate;
        this.mWeeks = weeks;
        this.mDays = days;
    }

    private void init(int identifier, int hours, int minutes, String name, String sound, boolean enabled, boolean vibrate, boolean[] weeks, boolean[] days) {
        this.mIdentifier = identifier;
        init(hours, minutes, name, sound, enabled, vibrate, weeks, days);
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public int getId() {
        return mIdentifier;
    }

    @Override
    public void setId(int identifier) {
        this.mIdentifier = identifier;
    }

    public int getHours() {
        return mHours;
    }

    public void setHours(int hours) {
        this.mHours = hours;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public void setMinutes(int minutes) {
        this.mMinutes = minutes;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSound() {
        return mSound;
    }

    public void setSound(String sound) {
        this.mSound = sound;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isVibrate() {
        return mVibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.mVibrate = vibrate;
    }

    public boolean[] getWeeks() {
        return mWeeks;
    }

    public void setWeeks(boolean[] weeks) {
        this.mWeeks = weeks;
    }

    public boolean[] getDays() {
        return mDays;
    }

    public void setDays(boolean[] days) {
        this.mDays = days;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mIdentifier);
        parcel.writeInt(mHours);
        parcel.writeInt(mMinutes);
        parcel.writeString(mName);
        parcel.writeString(mSound);
        parcel.writeByte((byte) (mEnabled ? 1 : 0));
        parcel.writeByte((byte) (mVibrate ? 1 : 0));
        parcel.writeBooleanArray(mWeeks);
        parcel.writeBooleanArray(mDays);
    }
}
