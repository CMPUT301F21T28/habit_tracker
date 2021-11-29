package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

class SecUtilityTest {

    /**
     * Testing if the salts that are generated are unique and not repeating
     */
    @Test
    void getNextSaltTest() {
        String prevSalt = SecUtility.getNextSalt();
        String nextSalt = SecUtility.getNextSalt();

        assertFalse(prevSalt.equals(nextSalt));
    }

    /**
     * Testing if hashing functions compute correct hash
     * @throws NoSuchAlgorithmException
     *      Should never occur as long as SHA-256 is a thing
     */
    @Test
    public void getSHAandHexToStringTest() throws NoSuchAlgorithmException {
        String inputString = "hello world";
        String correctHash = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";
        String incorrectHash = "112e476505aab51b05aeb2246c02a11df03e1187e886f7c55d4e9935c290ade";

        assertEquals(SecUtility.toHexString(SecUtility.getSHA(inputString)), correctHash);
        assertNotEquals(SecUtility.toHexString(SecUtility.getSHA(inputString)), incorrectHash);
    }

    @Test
    void convertImageToString() {
    }
}