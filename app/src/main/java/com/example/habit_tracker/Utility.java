package com.example.habit_tracker;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * Utility is a class specific for handling db retrieve in Friend's related fragment and password salting
 */

public class Utility {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Random _random = new SecureRandom();

    /**
     * Removes a request from the user's request list
     * @param username
     *      User to remove the request from
     * @param targetFriend
     *      Request to remove from the user's request list
     */
    public static void removeRequest(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("requests", FieldValue.arrayRemove(targetFriend));
    }

    /**
     * Adds a request to the user's request list
     * @param username
     *      User to add the request to
     * @param targetFriend
     *      The friend object request to add
     */
    public static void addRequest(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("requests", FieldValue.arrayUnion(targetFriend));
    }

    /**
     * Removes a friend from the user's friend list
     * @param username
     *      User to remove friend from
     * @param targetFriend
     *      Friend to remove
     */
    public static void removeFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayRemove(targetFriend));
    }

    /**
     * Adds a friend from the user's friend list
     * @param username
     *      User to add friend from
     * @param targetFriend
     *      Friend to add
     */
    public static void addFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayUnion(targetFriend));
    }
}
