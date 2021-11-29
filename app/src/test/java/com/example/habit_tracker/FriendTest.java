package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FriendTest {

    @Test
    void getUserName() {
        Friend mockFriend = mockFriend();
        assertEquals("mockUser", mockFriend.getUserName());
    }

    @Test
    void getActualName() {
        Friend mockFriend = mockFriend();
        assertEquals("mockName", mockFriend.getActualName());
    }

    @Test
    void describeContents() {
        Friend mockFriend = mockFriend();
        assertEquals(0, mockFriend.describeContents());
    }

    private Friend mockFriend() {
        return new Friend("mockUser", "mockName");
    }
}