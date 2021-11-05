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
        assertEquals(true,mockLogin.validPassword("qwe","489cd5dbc708c7e541de4d7cd91ce6d0f1613573b7fc5b40d3942ccb9555cf35"));
    }

    /**
     * testing login attempt fail
     */
    @Test
    public void testValidPasswordFailure(){
        assertEquals(false,mockLogin.validPassword("qwe","89cd5dbc708c7e541de4d7cd91ce6d0f1613573b7fc5b40d3942ccb9555cf35"));
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
