package com.example.habit_tracker;

import android.os.Parcel;
import android.os.Parcelable;

public class habitDataList implements Parcelable {
    private String userName;
    private String habitName;
    private String habitID;
    private String dateOfStarting;
    private String reason;
    private String repeat;
    private Boolean isPrivate;

    habitDataList(String userName, String habitName, String habitID, String dateOfStarting, String reason, String repeat, boolean isPrivate) {
        this.userName = userName;
        this.habitName = habitName;
        this.habitID = habitID;
        this.dateOfStarting = dateOfStarting;
        this.reason = reason;
        this.repeat = repeat;
        this.isPrivate = isPrivate;
    }

    protected habitDataList(Parcel in) {
        userName = in.readString();
        habitName = in.readString();
        habitID = in.readString();
        dateOfStarting = in.readString();
        reason = in.readString();
        repeat = in.readString();
        byte tmpIsPrivate = in.readByte();
        isPrivate = tmpIsPrivate == 0 ? null : tmpIsPrivate == 1;
    }

    public static final Creator<habitDataList> CREATOR = new Creator<habitDataList>() {
        @Override
        public habitDataList createFromParcel(Parcel in) {
            return new habitDataList(in);
        }

        @Override
        public habitDataList[] newArray(int size) {
            return new habitDataList[size];
        }
    };

    String getUserName() {return this.userName;}
    String getHabitName() {return this.habitName;}
    String getHabitID() {return this.habitID;}
    String getDateOfStarting() {return this.dateOfStarting;}
    String getReason() {return this.reason;}
    String getRepeat() {return  this.repeat;}
    Boolean getIsPrivate() {return this.isPrivate;}

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
