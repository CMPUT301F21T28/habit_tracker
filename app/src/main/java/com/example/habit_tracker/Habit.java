package com.example.habit_tracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Habit class stores Habit related information pulled from db
 */

public class Habit implements Parcelable, Info {
    private String userName;
    private String habitName;
    private String habitID;
    private String dateOfStarting;
    private String reason;
    private String repeat;
    private Boolean isPrivate;
    private Integer order;
    private Integer plan;
    private Integer finish;
    private Float progress;

    /**
     * Habit Class has 7 variables, each co-responding to either a field in Habit or a reference (e.g. username)
     * @param userName
     * @param habitName
     * @param habitID
     * @param dateOfStarting
     * @param reason
     * @param repeat
     * @param isPrivate
     */
    Habit(String userName, String habitName, String habitID, String dateOfStarting, String reason, String repeat, boolean isPrivate, Integer order, Integer plan, Integer finish) {
        this.userName = userName;
        this.habitName = habitName;
        this.habitID = habitID;
        this.dateOfStarting = dateOfStarting;
        this.reason = reason;
        this.repeat = repeat;
        this.isPrivate = isPrivate;
        this.order = order;
        this.plan = plan;
        this.finish = finish;
        this.progress = finish.floatValue() / plan.floatValue() * 100;
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

    /**
     * set when will the habit is going to repeat
     * @param repeat a string says when the habit is going to repeat
     */
    void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    /**
     * set the reason why there is a such habit (optional comment)
     * @param reason a string of reason
     */
    void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * set the date when the habit starts
     * @param dateOfStarting teh start date of the habit
     */
    void setDateOfStarting(String dateOfStarting){
        this.dateOfStarting = dateOfStarting;
    }

    /**
     * set if the habit is private
     * @param isPrivate a boolean if the habit is private
     */
    void setPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * set if the habit title
     * @param habitName a string of title
     */
    void setHabitTitle(String habitName) {
        this.habitName = habitName;
    }

    /**
     * Get username
     * @return
     *      Returns username
     */
    public String getUsername() {return this.userName;}

    /**
     * Get habit name
     * @return
     *      Returns habit name
     */
    public String getName() {return this.habitName;}

    /**
     * Get HabitID
     * @return  Returns HabitID
     */
    public String getHabitID() {return this.habitID;}

    /**
     * Gets starting date
     * @return  Returns date of start
     */
    public String getDateOfStarting() {return this.dateOfStarting;}

    /**
     * Get habit reason
     * @return Returns habit reason
     */
    public String getComment() {return this.reason;}

    /**
     * Get the repeat days
     * @return Returns a string of the repeats days (in full eg. Monday)
     */
    public String getRepeat() {return  this.repeat;}

    /**
     * Returns if the habit is private
     * @return Returns isPrivate as a boolean
     */
    public Boolean getIsPrivate() {return this.isPrivate;}

    /**
     * Gets the placement order of the habit for consistent ordering
     * @return  Return the ordering index of the habit
     */
    public Integer getOrder() {return this.order;}

    /**
     * Gets the planned number of times to complete the habit
     * @return     Returns # of times this habit is to be completed
     */
    public Integer getPlan() {return this.plan;}

    /**
     * Gets the number of times this habit has been finished already. This is counted
     * by the number of habitevents the habit has
     * @return  Returns the number of times the habit has been finished already.
     */
    public Integer getFinish() {return this.finish;}

    /**
     * Get the progress of the current habit (as a percentage)
     * @return Return the progress of the current habit as a percentage (already multiplied by 100)
     */
    public Float getProgress() {return this.progress;}

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
