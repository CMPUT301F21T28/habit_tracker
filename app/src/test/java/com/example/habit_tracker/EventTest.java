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
        Double locationLongitude = 0.0;
        Double locationLatitude = 0.0;
        mockEvent = new Event("mockUsername", "mockHabitId", "mockEventId",
                "mockName", "mockComment", locationLongitude,locationLatitude,"mockImage");
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

    /**
     * Testing the Image string getter of Event
     */
    @Test
    public void getEventImageTest() {
        assertEquals(mockEvent.getEventImage(), "mockImage");
    }

    /**
     * Testing the location longitude getter of Event
     */
    @Test
    public void getLocationLongitudeTest() {
        assertEquals(mockEvent.getLocationLongitude(), java.util.Optional.of(0.0));
    }

    /**
     * Testing the location latitude getter of Event
     */
    @Test
    public void getLocationLatitudeTest() {
        assertEquals(mockEvent.getLocationLatitude(), java.util.Optional.of(0.0));
    }

    /**
     * Testing the EventName setter for Event
     */
    @Test
    public void setEventNameTest() {
        assertEquals(mockEvent.getName(), "mockName");
        mockEvent.setEventName("newMockName");
        assertEquals(mockEvent.getName(), "newMockName");
    }

    /**
     * Testing the EventComment setter for Event
     */
    @Test
    public void setEventComment() {
        assertEquals(mockEvent.getComment(), "mockComment");
        mockEvent.setEventComment("newMockComment");
        assertEquals(mockEvent.getComment(), "newMockComment");
    }

    /**
     * Testing the EventLongitude setter for Event
     */
    @Test
    public void setLocationLongitudeTest() {
        assertEquals(mockEvent.getLocationLongitude(), java.util.Optional.of(0.0));
        mockEvent.setLocationLongitude(0.1);
        assertEquals(mockEvent.getLocationLongitude(), java.util.Optional.of(0.1));
    }

    /**
     * Testing the EventLocationLatitude setter for Event
     */
    @Test
    public void setLocationLatitudeTest() {
        assertEquals(mockEvent.getLocationLatitude(), java.util.Optional.of(0.0));
        mockEvent.setLocationLatitude(0.1);
        assertEquals(mockEvent.getLocationLatitude(), java.util.Optional.of(0.1));
    }

    /**
     * Testing the EventImage setter for Event
     */
    @Test
    public void setEventImageTest() {
        assertEquals(mockEvent.getEventImage(), "mockImage");
        mockEvent.setEventName("newMockImage");
        assertEquals(mockEvent.getName(), "newMockImage");
    }

        // Supposed to fail: (function stub)
        //    public int describeContents()

        // Dont have to test because part of superclass
        //    public void writeToParcel(Parcel parcel, int i)


}
