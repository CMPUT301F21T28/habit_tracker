package com.example.habit_tracker;

import android.os.Parcel;
import android.os.Parcelable;

public class Habit implements Parcelable, Info {
    private String userName;
    private String habitName;
    private String habitID;
    private String dateOfStarting;
    private String reason;
    private String repeat;
    private Boolean isPrivate;

    Habit(String userName, String habitName, String habitID, String dateOfStarting, String reason, String repeat, boolean isPrivate) {
        this.userName = userName;
        this.habitName = habitName;
        this.habitID = habitID;
        this.dateOfStarting = dateOfStarting;
        this.reason = reason;
        this.repeat = repeat;
        this.isPrivate = isPrivate;
    }

    protected Habit(Parcel in) {
        userName = in.readString();
        habitName = in.readString();
        habitID = in.readString();
        dateOfStarting = in.readString();
        reason = in.readString();
        repeat = in.readString();
        byte tmpIsPrivate = in.readByte();
        isPrivate = tmpIsPrivate == 0 ? null : tmpIsPrivate == 1;
    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    void setRepeat(String repeat) {
        this.repeat = repeat;
    }
    void setReason(String reason) {
        this.reason = reason;
    }
    void setDateOfStarting(String dateOfStarting){
        this.dateOfStarting = dateOfStarting;
    }
    void setPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    void setHabitTitle(String habitName) {
        this.habitName = habitName;
    }

    public String getUsername() {return this.userName;}
    public String getName() {return this.habitName;}
    public String getHabitID() {return this.habitID;}
    public String getDateOfStarting() {return this.dateOfStarting;}
    public String getComment() {return this.reason;}
    public String getRepeat() {return  this.repeat;}
    public Boolean getIsPrivate() {return this.isPrivate;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(habitName);
        parcel.writeString(habitID);
        parcel.writeString(dateOfStarting);
        parcel.writeString(reason);
        parcel.writeString(repeat);
        parcel.writeByte((byte) (isPrivate == null ? 0 : isPrivate ? 1 : 2));
    }
}
