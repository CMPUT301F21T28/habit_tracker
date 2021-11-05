package com.example.habit_tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LoginFragmentTest {
    private LoginFragment mockLogin;

    @BeforeEach
    public void createLogin(){
        mockLogin = new LoginFragment();
    }

    @Test
    public void testValidPasswordSuccess(){
        assertEquals(true,mockLogin.validPassword("qwe","489cd5dbc708c7e541de4d7cd91ce6d0f1613573b7fc5b40d3942ccb9555cf35"));
    }

    @Test
    public void testValidPasswordFailure(){
        assertEquals(false,mockLogin.validPassword("qwe","89cd5dbc708c7e541de4d7cd91ce6d0f1613573b7fc5b40d3942ccb9555cf35"));
    }


}
