package com.example.habit_tracker;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable, Info
{
    private String username;
    private String habitID;
    private String eventID;
    private String eventName;
    private String eventComment;
    //private String eventLocation;

    /**
     * Habit Class has 5 variables, each co-responding to either a field in Event or a reference (e.g. username)
     * @param username
     * @param habitID
     * @param eventID
     * @param eventName
     * @param eventComment
     */

    // TODO location, picture
    Event(String username, String habitID, String eventID, String eventName, String eventComment) {
        this.username = username;
        this.habitID = habitID;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventComment = eventComment;
        //this.eventLocation = eventLocation;
    }


    protected Event(Parcel in) {
        username = in.readString();
        habitID = in.readString();
        eventID = in.readString();
        eventName = in.readString();
        eventComment = in.readString();
        //eventLocation = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    void setEventName(String eventName) {
        this.eventName = eventName;
    }
    void setEventComment(String Comment) {
        this.eventComment = Comment;
    }
    //void setEventLocation(String Location){ this.eventLocation = Location;}

    public String getUsername() {return this.username;}
    public String getHabitID() {return this.habitID;}
    public String getEventID() {return this.eventID;}
    public String getName() {return this.eventName;}
    public String getComment() {return this.eventComment;}
    //String getEventLocation() {return this.eventLocation;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(habitID);
        parcel.writeString(eventID);
        parcel.writeString(eventName);
        parcel.writeString(eventComment);
        //parcel.writeString(eventLocation);
    }
}


