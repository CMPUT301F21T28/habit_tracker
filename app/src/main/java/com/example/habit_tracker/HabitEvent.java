package com.example.habit_tracker;

import android.os.Parcel;
import android.os.Parcelable;

public class HabitEvent implements Parcelable
{
    private String habitEventName;
    private String Comment;
    private String Location;

    HabitEvent(String habitEventName,String Comment,String Location) {
        this.habitEventName = habitEventName;
        this.Comment = Comment;
        this.Location = Location;
    }


    void setHabitEventName(String habitEventName) {
        this.habitEventName = habitEventName;
    }
    void setComment(String Comment) {
        this.Comment = Comment;
    }
    void setLocation(String Location){
        this.Location = Location;
    }

    String getHabitEventName() {return this.habitEventName;}
    String getComment() {return this.Comment;}
    String getLocation() {return this.Location;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}


