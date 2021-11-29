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
    private Double locationLongitude;
    private Double locationLatitude;
    private String eventImage;

    /**
     * Habit Class has 7 variables, each co-responding to either a field in Event or a reference (e.g. username)
     * @param username
     * @param habitID
     * @param eventID
     * @param eventName
     * @param eventComment
     * @param locationLongitude
     * @param locationLatitude
     */

    // TODO location, picture
    Event(String username, String habitID, String eventID, String eventName, String eventComment,  Double locationLongitude,Double locationLatitude ,String eventImage) {
        this.username = username;
        this.habitID = habitID;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventComment = eventComment;
        this.locationLatitude = locationLongitude;
        this.locationLongitude = locationLatitude;
        this.eventImage = eventImage;

    }


    protected Event(Parcel in) {
        username = in.readString();
        habitID = in.readString();
        eventID = in.readString();
        eventName = in.readString();
        eventComment = in.readString();
        locationLongitude = in.readDouble();
        locationLatitude = in.readDouble();
        eventImage = in.readString();
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
    void setLocationLongitude(Double longitude){this.locationLongitude= longitude;}
    void setLocationLatitude(Double latitude){this.locationLatitude= latitude;}

    void setEventImage(String imageString) { this.eventImage = imageString; }
    @Override


    public String getUsername() {return this.username;}
    public String getHabitID() {return this.habitID;}
    public String getEventImage() {return this.eventImage;}
    public String getEventID() {return this.eventID;}
    public String getName() {return this.eventName;}
    public String getComment() {return this.eventComment;}
    public Double getLocationLongitude() {return this.locationLongitude;}
    public Double getLocationLatitude() {return this.locationLatitude;}

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
        parcel.writeDouble(locationLongitude);
        parcel.writeDouble(locationLatitude);
        parcel.writeString(eventImage);
    }
}


