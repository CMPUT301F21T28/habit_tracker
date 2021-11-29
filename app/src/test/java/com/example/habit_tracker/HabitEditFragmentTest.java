package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HabitEditFragmentTest {
    private HabitEditFragment mockHabitEdit;

    @BeforeEach
    public void createHabitEdit(){
        mockHabitEdit = new HabitEditFragment();
    }

    @Test
    public void testCheckInputValiditySuccess(){
        String habitTitle = "habitA";
        String habitReason = "";
        assertEquals(true, mockHabitEdit.isStringValid(habitTitle, 0, 20));
        assertEquals(true, mockHabitEdit.isStringValid(habitReason, -1, 30));
    }

    @Test
    public void testCheckInputValidityFailure(){
        String habitTitle = "";
        String habitReason = "12345678901234567890123456789012345";
        assertEquals(false, mockHabitEdit.isStringValid(habitTitle, 0, 20));
        assertEquals(false, mockHabitEdit.isStringValid(habitReason, -1, 30));
    }

//    @Test
//    public void testCheckDateValiditySuccess(){
//        String mockDate = "2020/10/30";
//        assertEquals(true,mockHabitEdit.checkDateValidity(mockDate));
//    }
//
//    @Test
//    public void testCheckDateValidityFailure(){
//        //check 30 31 days availability
//        String mockDate = "2020/09/31";
//        assertEquals(false,mockHabitEdit.checkDateValidity(mockDate));
//        //check invalid input format
//        mockDate = "20200820";
//        assertEquals(false,mockHabitEdit.checkDateValidity(mockDate));
//    }
}
