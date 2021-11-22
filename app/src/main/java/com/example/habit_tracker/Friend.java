package com.example.habit_tracker;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable{
    private String userName;
    private String actualName;

    Friend(String userName, String actualName) {
        this.userName = userName;
        this.actualName = actualName;
    }

    protected Friend(Parcel in) {
        userName = in.readString();
        actualName = in.readString();
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    public String getUserName() {return this.userName;}
    public String getActualName() {return this.actualName;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(actualName);
    }
}
