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

    /**
     * Hashing support function. SRC: https://www.geeksforgeeks.org/sha-256-hash-in-java/
     * @param input
     *      string to be hashed
     * @return
     *      returns the hashed result as a bytearray
     * @throws NoSuchAlgorithmException
     *      if the hash function specified does not exist. SHOULD NOT OCCUR
     */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Hashing support function. SRC: https://www.geeksforgeeks.org/sha-256-hash-in-java/
     * @param hash
     *      byte array input of something that is hashed in SHA256
     * @return
     *      returns the string representation of the hash in hex
     */
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }


    // TODO: Javadoc for this function
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
