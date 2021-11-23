package com.example.habit_tracker;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void removeRequest(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("requests", FieldValue.arrayRemove(targetFriend));
    }

    public void addRequest(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("requests", FieldValue.arrayUnion(targetFriend));
    }


    public void removeFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayRemove(targetFriend));
    }

    public void addFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayUnion(targetFriend));
    }
}
