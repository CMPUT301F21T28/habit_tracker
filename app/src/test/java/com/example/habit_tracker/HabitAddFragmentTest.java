package com.example.habit_tracker;

import android.widget.EditText;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HabitAddFragmentTest {
    // Test this function
    //public static boolean checkDateValidity(final String date)
    private HabitAddFragment mockHabitAdd;

    @BeforeEach
    public void createHabitAdd(){
        mockHabitAdd = new HabitAddFragment();
    }

    @Test
    public void testCheckInputValiditySuccess(){
        String habitTitle = "habitA";
        String habitReason = "";
        assertEquals(true, mockHabitAdd.isStringValid(habitTitle, 0, 20));
        assertEquals(true, mockHabitAdd.isStringValid(habitReason, -1, 30));
    }

    @Test
    public void testCheckInputValidityFailure(){
        String habitTitle = "";
        String habitReason = "12345678901234567890123456789012345";
        assertEquals(false, mockHabitAdd.isStringValid(habitTitle, 0, 20));
        assertEquals(false, mockHabitAdd.isStringValid(habitReason, -1, 30));
    }

//    @Test
//    public void testCheckDateValiditySuccess(){
//        String mockDate = "2020/10/30";
//        assertEquals(true,mockHabitAdd.checkDateValidity(mockDate));
//    }
//
//    @Test
//    public void testCheckDateValidityFailure(){
//        //check 30 31 days availability
//        String mockDate = "2020/09/31";
//        assertEquals(false,mockHabitAdd.checkDateValidity(mockDate));
//        //check invalid input format
//        mockDate = "20200820";
//        assertEquals(false,mockHabitAdd.checkDateValidity(mockDate));
//    }
}
