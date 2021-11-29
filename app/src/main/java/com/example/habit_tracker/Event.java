package com.example.habit_tracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Event class stores Event related information pulled from db
 */
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
    //private String eventLocation;

    /**
     * Habit Class has 5 variables, each co-responding to either a field in Event or a reference (e.g. username)
     * @param username
     * @param habitID
     * @param eventID
     * @param eventName
     * @param eventComment
     */

    Event(String username, String habitID, String eventID, String eventName, String eventComment,  Double locationLongitude,Double locationLatitude ,String eventImage) {
        this.username = username;
        this.habitID = habitID;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventComment = eventComment;
        this.locationLatitude = locationLongitude;
        this.locationLongitude = locationLatitude;
        this.eventImage = eventImage;
        //this.eventLocation = eventLocation;
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

    /**
     * set the event name
     * @param eventName the event name
     */
    void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * set the comment of the event if the user write one
     * @param Comment the comment
     */
    void setEventComment(String Comment) { this.eventComment = Comment; }

    /**
     * set the longitude of the location of the user
     * @param longitude the double of longitude
     */
    void setLocationLongitude(Double longitude){this.locationLongitude= longitude;}

    /**
     * set the latitude of the location of the user
     * @param latitude the double of latitude
     */
    void setLocationLatitude(Double latitude){this.locationLatitude= latitude;}

    /**
     * set the image as a string, if the
     * @param imageString the image string
     */
    void setEventImage(String imageString) { this.eventImage = imageString; }


    /**
     * get the username of the user
     * @return string Username
     */
    @Override
    public String getUsername() {return this.username;}

    /**
     * get the habitID of the specific habit
     * @return string of habitID
     */
    public String getHabitID() {return this.habitID;}

    /**
     * get the optional event image
     * @return string of event image, will be converted to a image
     */
    public String getEventImage() {return this.eventImage;}

    /**
     * get the id of the selected event
     * @return string of the event id
     */
    public String getEventID() {return this.eventID;}

    /**
     * get the name of the selected event
     * @return string of event name
     */
    public String getName() {return this.eventName;}

    /**
     * get the optional comment
     * @return string of optional comment
     */
    public String getComment() {return this.eventComment;}

    /**
     * get the longitude of the location if the user set one
     * @return double of the location (longitude)
     */
    public Double getLocationLongitude() {return this.locationLongitude;}

    /**
     * get the latitude of the location if the user set one
     * @return double of the location (latitude)
     */
    public Double getLocationLatitude() {return this.locationLatitude;}

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * the necessary function to make event parcelable
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(habitID);
        parcel.writeString(eventID);
        parcel.writeString(eventName);
        parcel.writeString(eventComment);;
        //parcel.writeDouble(locationLongitude);
        //parcel.writeDouble(locationLatitude);
        //parcel.writeString(eventLocation);
        parcel.writeString(eventImage);
    }
}


