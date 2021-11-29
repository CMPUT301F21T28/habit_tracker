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
        assertEquals(false,mockLogin.validPassword("qwerty", "b669458a604859eb35572502f7b17d7103bc34a8384aa0b531b03d2ff16517cf","\uD845\uDE18�\u001D�K�'�\t��\u0010x"));
    }
}
