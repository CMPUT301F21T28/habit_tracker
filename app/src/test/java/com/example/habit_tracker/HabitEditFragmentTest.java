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
    public void testCheckDateValiditySuccess(){
        String mockDate = "2020/10/30";
        //assertEquals(true,mockHabitEdit.checkDateValidity(mockDate));
    }

    @Test
    public void testCheckDateValidityFailure(){
        //check 30 31 days availability
        String mockDate = "2020/09/31";
        //assertEquals(false,mockHabitEdit.checkDateValidity(mockDate));
        //check invalid input format
        mockDate = "20200820";
        //assertEquals(false,mockHabitEdit.checkDateValidity(mockDate));
    }
}
