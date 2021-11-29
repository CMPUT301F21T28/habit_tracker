package com.example.habit_tracker;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UITest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Solo solo;
    LoginTest loginTest = new LoginTest();
    HabitTest habitTest = new HabitTest();

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all the tests
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void UITest() throws InterruptedException {
        // **** Creating account and logging in
        solo.assertCurrentActivity("WrongActivity", MainActivity.class);

        // Creating Main User
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));
        solo.enterText((EditText) solo.getView(R.id.editText_username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "robotiumName");
        solo.enterText((EditText) solo.getView(R.id.editText_first_password), "robotiumPw");
        solo.enterText((EditText) solo.getView(R.id.editText_second_password), "robotiumPw");
        solo.clickOnButton("Sign Up");
        assertNotNull(solo.getView(R.id.login_frag_linear_layout));

        // Creating Friend
        solo.waitForText("robotiumUser");
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));
        solo.enterText((EditText) solo.getView(R.id.editText_username), "robotiumFriend");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "robotiumFriendName");
        solo.enterText((EditText) solo.getView(R.id.editText_first_password), "robotiumPw");
        solo.enterText((EditText) solo.getView(R.id.editText_second_password), "robotiumPw");
        solo.clickOnButton("Sign Up");
        assertNotNull(solo.getView(R.id.login_frag_linear_layout));

        // Enter in fields for login
        solo.waitForText("robotiumFriend");
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.enterText((EditText) solo.getView(R.id.username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.password), "robotiumPw");

        // Click log in
        solo.clickOnButton("LOG IN");

        // Check if entered into habitlistfragment
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));

        // **** Creating, Viewing, Editing Habit
        //create a habit
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        assertNotNull(solo.getView(R.id.habit_add_constraint_layout));

        //set details for habit
        solo.enterText((EditText) solo.getView(R.id.editText_habitTitle), "robotiumHabit");
        solo.enterText((EditText) solo.getView(R.id.editText_habitReason), "robotiumHabitReason");
        solo.clickOnView(solo.getView(R.id.textDatePicker));
        solo.setDatePicker(0, 2012, 2, 16);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.textView_select_day));
        solo.clickInList(1);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.radioYes));
        solo.clickOnView(solo.getView(R.id.submit_button));

        // Check current page as habit list
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));

        // Viewing habit
        solo.clickOnText("robotiumHabit");
        assertNotNull(solo.getView(R.id.habit_detail_layout));
        TextView view = (TextView)solo.getView(R.id.textView_habitTitle);
        assertEquals("robotiumHabit",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_habitReason);
        assertEquals("robotiumHabitReason",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_dateOfStarting);
        assertEquals("2012-3-16",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_repeat);
        assertEquals("Monday",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_private);
        assertEquals("true",view.getText().toString());

        // Editing habit
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.editText_habitReason2));
        solo.enterText((EditText) solo.getView(R.id.editText_habitReason2), "robotiumHabitReasonNew");
        solo.clickOnView(solo.getView(R.id.textDateStarting));
        solo.setDatePicker(0, 2013, 2, 16);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.textView_select_day2));
        solo.clickInList(2);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.radioNo));
        solo.clickOnView(solo.getView(R.id.button_submit));

        // Check current page as habit list
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));

        //Click on the new habit
        solo.clickOnText("robotiumHabit");
        // Check current page as habit detail
        assertNotNull(solo.getView(R.id.habit_detail_layout));

        //Check newly updated habit
        assertNotNull(solo.getView(R.id.habit_detail_layout));
        view = (TextView)solo.getView(R.id.textView_habitTitle);
        assertEquals("robotiumHabit",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_habitReason);
        assertEquals("robotiumHabitReasonNew",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_dateOfStarting);
        assertEquals("2013-3-16",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_repeat);
        assertEquals("Tuesday",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_private);
        assertEquals("false",view.getText().toString());
        // back out from habit details
        solo.clickOnActionBarHomeButton();


        // **** Creating new habit event
        // Add an event
        solo.clickOnView(solo.getView(R.id.habit_progress_row));
        assertNotNull(solo.getView(R.id.event_list_layout));

        // Add an event
        solo.clickOnView(solo.getView(R.id.floatingActionButtonAdd));
        // Check current page as event add page
        assertNotNull(solo.getView(R.id.event_add_constraint_layout));

        // Add event detail
        solo.enterText((EditText) solo.getView(R.id.editTextName), "robotiumHabitEvent");

        solo.clickOnButton("SUBMIT");
        assertNotNull(solo.getView(R.id.event_list_layout));
        assertTrue(solo.waitForText("robotiumHabitEvent", 1 ,5));

        // Click on new habit event
        solo.clickOnText("robotiumHabitEvent");
        // Check current page as habit event detail
        assertNotNull(solo.getView(R.id.habit_event_detail_constrant_layout));
        view = (TextView)solo.getView(R.id.textView_detail_eventName_view);
        assertEquals("robotiumHabitEvent",view.getText().toString());

        // Edit event
        solo.clickOnButton("Edit");
        assertNotNull(solo.getView(R.id.event_edit_constraint_layout));
        // Add event detail
        solo.clearEditText((EditText) solo.getView(R.id.nameContent));
        solo.enterText((EditText) solo.getView(R.id.nameContent), "robotiumEventNew");
        solo.clickOnButton("Submit");
        assertNotNull(solo.getView(R.id.event_list_layout));
        assertTrue(solo.waitForText("robotiumEventNew"));

        // TODO: FIX THIS - after
        solo.clickOnActionBarHomeButton();

        // Swipe to delete current habit
        // https://stackoverflow.com/questions/24664730/writing-a-robotium-test-to-swipe-open-an-item-on-a-swipeable-listview
        int fromX, toX, fromY, toY;
        int[] location = new int[2];
        View row = solo.getText("robotiumHabit");
        row.getLocationInWindow(location);
        fromX = location[0];
        fromY = location[1];

        toX = location[0]+100;
        toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 10);

        solo.clickOnText("YES");
        // Confirm habit is deleted from list
        assertFalse(solo.waitForText("robotiumHabit", 1, 5));


        // **** Friend Stuff
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.friend_button));
        // Check if entered into friendlistpage
        assertNotNull(solo.getView(R.id.friend_list_constraint_layout));

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

        // wait for snackbar to disappear (10 second wait)
        solo.sleep(5000);

        // **** Search and add friend
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_friend_button));
        assertNotNull(solo.getView(R.id.friend_search_constraint_layout));

        // create test user
        String friendName = "robotiumFriend";

        // send friend request
        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName), friendName);
        solo.clickOnButton("Search");
        solo.clickOnButton("Request");

        // Logging into friend acc
        // TODO: FIX THIS (MERGE WITH LUCAS' BRANCH)
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarHomeButton();
        assertNotNull(solo.getView(R.id.login_frag_linear_layout));
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.clearEditText((EditText) solo.getView(R.id.password));
        solo.enterText((EditText) solo.getView(R.id.username), "robotiumFriend");
        solo.enterText((EditText) solo.getView(R.id.password), "robotiumPw");
        solo.clickOnButton("LOG IN");
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.friend_button));
        assertNotNull(solo.getView(R.id.friend_list_constraint_layout));

        // check if request is there
        assertFalse(solo.waitForText("robotiumName(robotiumUser)", 1, 5));
    }

    private void addRequest(String currentUsername, String newUsername, String newRealname) {
        Utility.addRequest(currentUsername, new Friend(newUsername, newRealname));
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
        removeAccount("robotiumUser");
        removeAccount("robotiumFriend");
    }

    private void removeAccount(String username) {
        db.collection("Users").document(username).collection("HabitList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String habitId = document.getId();
                                db.collection("habit").document(habitId).collection("EventList")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        //Log.d("Success", "To get " + document.toObject(String.class));
                                                        db.collection("habit").document(habitId).collection("EventList").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("TAG", "Deleted " + document.getId() + " => " + document.getData());
                                                            }
                                                        });

                                                    }
                                                } else {
                                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                db.collection("Users").document(username).collection("HabitList").document(habitId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "Deleted " + document.getId() + " => " + document.getData());
                                    }
                                });

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("Users").document(username)
                .delete();
    }

}
