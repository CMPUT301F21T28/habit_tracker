package com.example.habit_tracker;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * Utility is a class specific for handling db retrieve in Friend's related fragment and password salting
 */

public class Utility {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Random _random = new SecureRandom();

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

    /**
     * Returns a random salt to be used to hash a password.
     * Credit: https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     * @return a 16 bytes random salt
     */
    public static String getNextSalt() {
        byte[] salt = new byte[16];
        _random.nextBytes(salt);
        String saltString = new String(salt);
        return saltString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertImageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String imageString;
        imageString = Base64.getEncoder().encodeToString(imageByte);
        return imageString;
    }
}
