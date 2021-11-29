package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.*;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class FriendListFragmentTest {
    // No tests to run as everything all the functions are tied into with firebase.






//    FriendListFragment friendListFragment = new FriendListFragment();
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    @Test
//    void updateFriendListTest() {
//        String username = "unitTestUser";
//        createMockAccount(username);
//
//        friendListFragment.updateFriendList(username);
//        assertEquals(1, friendListFragment.friendDataList.size());
//        assertEquals(0, friendListFragment.requestDataList.size());
//    }
//
//    @AfterEach
//    void tearDown() {
//        removeAccount("unitTestUser");
//    }
//
//    private void createMockAccount(String username) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("username", username);
//        data.put("realname", username);
//        data.put("password", "123456");
//        data.put("salt", "NA");
//        ArrayList<Friend> friends = new ArrayList<Friend>();
//        friends.add(new Friend("friendUser", "friendActual"));
//
//        data.put("friends", friends); // empty list since there are no friends on the acc yet
//        data.put("requests", Collections.emptyList());
//
//        db.collection("Users").document(username).set(data);
//    }
//
//    private void removeAccount(String username) {
//        db.collection("Users").document(username)
//                .delete();
//    }
}