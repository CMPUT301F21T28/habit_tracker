package com.example.habit_tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;


public class LoginFragmentTest {
    private LoginFragment mockLogin;

    @BeforeEach
    public void createLogin(){
        mockLogin = new LoginFragment();
    }

    /**
     * testing login attempt success
     */
    @Test
    public void testValidPasswordSuccess(){
        assertEquals(true,mockLogin.validPassword("qwerty", "a669458a604859eb35572502f7b17d7103bc34a8384aa0b531b03d2ff16517cf","\uD845\uDE18�\u001D�K�'�\t��\u0010x"));
    }

    /**
     * testing login attempt fail
     */
    @Test
    public void testValidPasswordFailure(){
        assertEquals(false,mockLogin.validPassword("qwerty", "a669458a604859eb35572502f7b17d7103bc34a8384aa0b531b03d2ff16517cf","\uD845\uDE18�\u001D�K�'�\t��\u0010x"));
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

        assertEquals(LoginFragment.toHexString(LoginFragment.getSHA(inputString)), correctHash);
        assertNotEquals(LoginFragment.toHexString(LoginFragment.getSHA(inputString)), incorrectHash);

    }
}
