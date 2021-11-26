package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilityTest {


    @BeforeEach
    void setUp() {
        // Create new test user in firestore db
    }

    @AfterEach
    void tearDownTest() {
    }

    @Test
    void removeRequestTest() {
    }

    @Test
    void addRequestTest() {
    }

    @Test
    void removeFriendTest() {
    }

    @Test
    void addFriendTest() {
    }

    @Test
    void getNextSaltTest() {
        assertNotEquals(Utility.getNextSalt(), Utility.getNextSalt());
    }
}