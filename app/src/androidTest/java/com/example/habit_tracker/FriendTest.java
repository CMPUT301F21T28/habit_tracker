package com.example.habit_tracker;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FriendTest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void AcceptRequestTest() {
        addRequest("robotiumUser", "testRequest1User", "testRequest1Name");

        solo.clickOnButton("Accept");

        assertTrue(solo.waitForText("testRequest1Name"));
    }

    @Test
    public void RejectRequestTest() {
        addRequest("robotiumUser", "testRequest1User", "testRequest1Name");

        solo.clickOnButton("Deny");

        assertFalse(solo.waitForText("testRequest1Name", 1, 5));
    }

    @Test
    public void navigateToAddFriendsTest() {
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_friend_button));
        assertNotNull(solo.getView(R.id.friend_search_constraint_layout));
    }

    @Test
    public void AddFriendTest() throws NoSuchAlgorithmException {
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_friend_button));
        assertNotNull(solo.getView(R.id.friend_search_constraint_layout));

        // create test user
        String friendName = "robotiumFriend";

        // send friend request
        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName), friendName);
        solo.clickOnButton("Search");
        solo.clickOnButton("Request");


        // Logging into friend acc
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarHomeButton();
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.clearEditText((EditText) solo.getView(R.id.password));
        solo.enterText((EditText) solo.getView(R.id.username), "robotiumFriend");
        solo.enterText((EditText) solo.getView(R.id.password), "robotiumPw");
        solo.clickOnButton("LOG IN");
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.friend_button));
        assertNotNull(solo.getView(R.id.friend_list_constraint_layout));

        // check if request is there
        assertTrue(solo.waitForText("robotiumName"));
    }

    @Test
    public void AddFriendCancelTest() {
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_friend_button));
        assertNotNull(solo.getView(R.id.friend_search_constraint_layout));

        // create test user
        String friendName = "robotiumFriend";

        // send friend request
        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName), friendName);
        solo.clickOnButton("Search");
        solo.clickOnButton("Request");
        solo.clickOnButton("Cancel");


        // Logging into friend acc
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarHomeButton();
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.clearEditText((EditText) solo.getView(R.id.password));
        solo.enterText((EditText) solo.getView(R.id.username), "robotiumFriend");
        solo.enterText((EditText) solo.getView(R.id.password), "robotiumPw");
        solo.clickOnButton("LOG IN");
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.friend_button));
        assertNotNull(solo.getView(R.id.friend_list_constraint_layout));

        // check if request is there
        assertFalse(solo.waitForText("robotiumName", 1, 5));
    }

    @Test
    public void ViewFriendInfoTest() {
        // Create and add request
        addRequest("robotiumUser", "testRequest1User", "testRequest1Name");
        solo.clickOnButton("Accept");
        assertTrue(solo.waitForText("testRequest1Name"));

        // Click on friend to view info
        solo.clickOnText("testRequest1Name");
        assertNotNull(solo.getView(R.id.friend_info_constraint_layout));
    }

    @Test
    public void UnfriendUserTest() {
        // Create and add request
        addRequest("robotiumUser", "testRequest1User", "testRequest1Name");
        solo.clickOnButton("Accept");
        assertTrue(solo.waitForText("testRequest1Name"));

        // Click on friend to view info
        solo.clickOnText("testRequest1Name");
        assertNotNull(solo.getView(R.id.friend_info_constraint_layout));

        // Click on unfriend button
        solo.clickOnButton("Unfriend");

        // Click on confirmation
        solo.clickOnText("YES");

        // Make sure that the name is removed from friend list.
        assertNotNull(solo.getView(R.id.friend_list_constraint_layout));
        assertFalse(solo.waitForText("testRequest1Name", 1, 5));
    }

    @Test
    public void UndoUnfriendUserTest() {
        // Create and add request
        addRequest("robotiumUser", "testRequest1User", "testRequest1Name");
        solo.clickOnButton("Accept");
        assertTrue(solo.waitForText("testRequest1Name"));

        // Click on friend to view info
        solo.clickOnText("testRequest1Name");
        assertNotNull(solo.getView(R.id.friend_info_constraint_layout));

        // Click on unfriend button
        solo.clickOnButton("Unfriend");

        // Click on confirmation
        solo.clickOnText("YES");

        // Make sure that the name is removed from friend list.
        assertNotNull(solo.getView(R.id.friend_list_constraint_layout));
        assertFalse(solo.waitForText("testRequest1Name", 1, 5));

        // Click on snackbar
        // TODO: Click on snackbar
        solo.clickOnView(solo.getView(R.id.snackbar_action));
    }

    /**
     * Runs before all the tests
     * Creates an account and leaves the user at the friendListFragment homepage.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("WrongActivity", MainActivity.class);

        // Creating Main User
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));
        solo.enterText((EditText) solo.getView(R.id.editText_username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "robotiumName");
        solo.enterText((EditText) solo.getView(R.id.editText_first_password), "robotiumPw");
        solo.enterText((EditText) solo.getView(R.id.editText_second_password), "robotiumPw");
        solo.clickOnButton("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));

        // Creating Friend
        solo.waitForText("robotiumUser");
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));
        solo.enterText((EditText) solo.getView(R.id.editText_username), "robotiumFriend");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "robotiumFriendName");
        solo.enterText((EditText) solo.getView(R.id.editText_first_password), "robotiumPw");
        solo.enterText((EditText) solo.getView(R.id.editText_second_password), "robotiumPw");
        solo.clickOnButton("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));

        // Enter in fields for login
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.enterText((EditText) solo.getView(R.id.username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.password), "robotiumPw");

        // Click log in
        solo.clickOnButton("LOG IN");

        // Check if entered into habitlistfragment
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.friend_button));

        // Check if entered into friendlistpage
        assertNotNull(solo.getView(R.id.friend_list_constraint_layout));
    }

    /**
     * Runs after all the tests
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
        removeAccount("robotiumUser");
        removeAccount("robotiumFriend");
    }

    private void removeAccount(String username) {
        db.collection("Users").document(username)
                .delete();
    }

    private void addRequest(String currentUsername, String newUsername, String newRealname) {
        Utility.addRequest(currentUsername, new Friend(newUsername, newRealname));
    }
}
