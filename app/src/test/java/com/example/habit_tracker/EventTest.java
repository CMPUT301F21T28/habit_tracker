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
        
        mockEvent = new Event("mockUsername", "mockHabitId", "mockEventId", "mockName", "mockComment", locationLongitude, locationLatitude, "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");
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
