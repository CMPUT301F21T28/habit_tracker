package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class HabitTest {

    // The functions not being tested are from superclass no need to test.

    private Habit mockHabit;

    /**
     * Creates a mock habit into var mockHabit before each test is run.
     */
    @BeforeEach
    public void createHabit(){
        mockHabit= new Habit("usernameMock","habitNameMock", "habitIDMock", "dateOfStartingMock", "reasonMock","repeatMock",false, 1, 4, 2);
    }

    /**
     * Tesitng if the setRepeat of habit works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testSetRepeat() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setRepeat("Monday");
        final Field field = mockHabit.getClass().getDeclaredField("repeat");
        field.setAccessible(true);
        assertEquals("Monday", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if the setReason of Habit works
     */
    @Test
    public void testSetReason() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setReason("Stay healthy");
        final Field field = mockHabit.getClass().getDeclaredField("reason");
        field.setAccessible(true);
        assertEquals("Stay healthy", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if setDateofStarting of Habit works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testSetDateOfStarting() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setDateOfStarting("2020/10/12");
        final Field field = mockHabit.getClass().getDeclaredField("dateOfStarting");
        field.setAccessible(true);
        assertEquals("2020/10/12", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if setPrivate of Habit works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testSetPrivate() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setPrivate(true);
        final Field field = mockHabit.getClass().getDeclaredField("isPrivate");
        field.setAccessible(true);
        assertEquals(true, field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if setHabitTitle of Habit works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testSetHabitTitle() throws NoSuchFieldException, IllegalAccessException {
        mockHabit.setHabitTitle("New Title");
        final Field field = mockHabit.getClass().getDeclaredField("habitName");
        field.setAccessible(true);
        assertEquals("New Title", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if testGetusername works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetUsername() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("userName");
        field.setAccessible(true);
        assertEquals("usernameMock", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if getName of Habit works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetName() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("habitName");
        field.setAccessible(true);
        assertEquals("habitNameMock", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if getHabitId of Habit class works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetHabitID() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("habitID");
        field.setAccessible(true);
        assertEquals("habitIDMock", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing if getSTartDate of Habit works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetDateOfStarting() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("dateOfStarting");
        field.setAccessible(true);
        assertEquals("dateOfStartingMock", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing get comment
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetComment() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("reason");
        field.setAccessible(true);
        assertEquals("reasonMock", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing getRepeat
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetRepeat() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("repeat");
        field.setAccessible(true);
        assertEquals("repeatMock", field.get(mockHabit), "Fields didn't match");
    }

    /**
     * Testing isPrivate
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testIsPrivate() throws NoSuchFieldException, IllegalAccessException {
        final Field field = mockHabit.getClass().getDeclaredField("isPrivate");
        field.setAccessible(true);
        assertEquals(false, field.get(mockHabit), "Fields didn't match");
    }

    @Test
    void getUsername() {
        assertEquals("usernameMock", mockHabit.getUsername());
    }

    @Test
    void getName() {
        assertEquals("habitNameMock", mockHabit.getName());
    }

    @Test
    void getHabitID() {
        assertEquals("habitIDMock", mockHabit.getHabitID());
    }

    @Test
    void getDateOfStarting() {
        assertEquals("dateOfStartingMock", mockHabit.getDateOfStarting());
    }

    @Test
    void getComment() {
        assertEquals("reasonMock", mockHabit.getComment());
    }

    @Test
    void getRepeat() {
        assertEquals("repeatMock", mockHabit.getRepeat());
    }

    @Test
    void getIsPrivate() {
        assertEquals(false, mockHabit.getIsPrivate());
    }

    @Test
    void getOrder() {
        assertEquals((Integer) 1, mockHabit.getOrder());
    }

    @Test
    void getPlan() {
        assertEquals((Integer) 4, mockHabit.getPlan());
    }

    @Test
    void getFinish() {
        assertEquals((Integer) 2, mockHabit.getFinish());
    }

    @Test
    void getProgress() {
        assertEquals((Float) 50.0f, mockHabit.getProgress());
    }
}
