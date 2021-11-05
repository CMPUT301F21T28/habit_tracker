package com.example.habit_tracker;

import android.widget.EditText;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;

public class SignupFragmentTest {

    /**
     * Testing if checking username valid follows guidelines
     */
    @Test
    public void checkUsernameValidTest() {
        String mockUsername;

        mockUsername = "mockUsername"; // longer than 0 characters
        assert(SignupFragment.checkUsernameValid(mockUsername));

        mockUsername = ""; // 0 characters
        assertFalse(SignupFragment.checkUsernameValid(mockUsername));

        mockUsername = "somethingthatissuperlongandover20characters"; // longer than 20 characters
        assertFalse(SignupFragment.checkUsernameValid(mockUsername));
    }

    /**
     * Testing if checkRealNameValid follows guidelines
     */
    @Test
    public void checkRealnameValidTest() {
        String mockRealname;

        mockRealname = "John Doe";
        assert(SignupFragment.checkRealnameValid(mockRealname));

        mockRealname = "";
        assertFalse(SignupFragment.checkRealnameValid(mockRealname));
    }

    /**
     * Testing if checkFirstPassValid follows rules
     */
    @Test
    public void checkFirstPassValidTest() {
        String mockFirstPass;

        mockFirstPass = "a"; // shorter than 5
        assertFalse(SignupFragment.checkFirstPassValid(mockFirstPass));

        mockFirstPass = "asdfg"; // 5 characters
        assertFalse(SignupFragment.checkFirstPassValid(mockFirstPass));

        mockFirstPass = "goodpassword123"; // longer than 5
        assertTrue(SignupFragment.checkFirstPassValid(mockFirstPass));

    }

    /**
     * Testing if checkSecondPassValid follows rules
     */
    @Test
    public void checkSecondPassValidTest() {
        String mockFirstPass, mockSecondPass;

        mockFirstPass = "onepassword";
        mockSecondPass = "secondpassword";
        assertFalse(SignupFragment.checkSecondPassValid(mockFirstPass, mockSecondPass));

        mockFirstPass = "samepass";
        mockSecondPass = "samepass";
        assertTrue(SignupFragment.checkSecondPassValid(mockFirstPass, mockSecondPass));
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

        assertEquals(SignupFragment.toHexString(SignupFragment.getSHA(inputString)), correctHash);
        assertNotEquals(SignupFragment.toHexString(SignupFragment.getSHA(inputString)), incorrectHash);

    }

}
