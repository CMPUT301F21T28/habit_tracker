package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class HabitTest {

    // The functions not being tested are from superclass no need to test.

    private Habit mockHabit;

    @BeforeEach
    public void createHabit(){
        mockHabit= new Habit("usernameMock","habitNameMock", "habitIDMock", "dateOfStartingMock", "reasonMock","repeatMock",false);
    }

    @Test
    public void testSetRepeat() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setRepeat("Monday");
        final Field field = mockHabit.getClass().getDeclaredField("repeat");
        field.setAccessible(true);
        assertEquals("Monday", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testSetReason() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setReason("Stay healthy");
        final Field field = mockHabit.getClass().getDeclaredField("reason");
        field.setAccessible(true);
        assertEquals("Stay healthy", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testSetDateOfStarting() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setDateOfStarting("2020/10/12");
        final Field field = mockHabit.getClass().getDeclaredField("dateOfStarting");
        field.setAccessible(true);
        assertEquals("2020/10/12", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testSetPrivate() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setPrivate(true);
        final Field field = mockHabit.getClass().getDeclaredField("isPrivate");
        field.setAccessible(true);
        assertEquals(true, field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testSetHabitTitle() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setHabitTitle("New Title");
        final Field field = mockHabit.getClass().getDeclaredField("habitName");
        field.setAccessible(true);
        assertEquals("New Title", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testGetUsername() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("userName");
        field.setAccessible(true);
        assertEquals("usernameMock", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testGetName() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("habitName");
        field.setAccessible(true);
        assertEquals("habitNameMock", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testGetHabitID() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("habitID");
        field.setAccessible(true);
        assertEquals("habitIDMock", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testGetDateOfStarting() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("dateOfStarting");
        field.setAccessible(true);
        assertEquals("dateOfStartingMock", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testGetComment() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("reason");
        field.setAccessible(true);
        assertEquals("reasonMock", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testGetRepeat() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("repeat");
        field.setAccessible(true);
        assertEquals("repeatMock", field.get(mockHabit), "Fields didn't match");
    }

    @Test
    public void testIsPrivate() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("isPrivate");
        field.setAccessible(true);
        assertEquals(false, field.get(mockHabit), "Fields didn't match");
    }

}
