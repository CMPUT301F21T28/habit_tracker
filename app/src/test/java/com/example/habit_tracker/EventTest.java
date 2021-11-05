package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTest {
    Event mockEvent;

    /**
     * Sets mockEvent to a new Event before each test.
     */
    @BeforeEach
    public void createMockEvent(){
        mockEvent = new Event("mockUsername", "mockHabitId", "mockEventId", "mockName", "mockComment");
    }

    /**
     * Testing the username getter of Event
     */
    @Test
    public void getUsernameTest() {
        assertEquals(mockEvent.getUsername(), "mockUsername");
    }

    /**
     * Testing the habitId getter of Event
     */
    @Test
    public void getHabitIDTest() {
        assertEquals(mockEvent.getHabitID(), "mockHabitId");
    }

    /**
     * Testing the eventId getter of Event
     */
    @Test
    public void getEventIDTest() {
        assertEquals(mockEvent.getEventID(), "mockEventId");
    }

    /**
     * Testing the eventName getter of Event
     */
    @Test
    public void getNameTest() {
        assertEquals(mockEvent.getName(), "mockName");
    }

    /**
     * Testing the comment getter of Event
     */
    @Test
    public void getCommentTest() {
        assertEquals(mockEvent.getComment(), "mockComment");
    }

    @Test
    public void setEventNameTest() {
        assertEquals(mockEvent.getName(), "mockName");
        mockEvent.setEventName("newMockName");
        assertEquals(mockEvent.getName(), "newMockName");
    }

    @Test
    public void setEventComment() {
        assertEquals(mockEvent.getComment(), "mockComment");
        mockEvent.setEventComment("newMockComment");
        assertEquals(mockEvent.getComment(), "newMockComment");
    }
        //    void setEventComment(String Comment)
    //
        // Supposed to fail: (function stub)
    //    public int describeContents()

        // Dont have to test because part of superclass
        //    public void writeToParcel(Parcel parcel, int i)


}
