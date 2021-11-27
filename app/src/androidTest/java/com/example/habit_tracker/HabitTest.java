package com.example.habit_tracker;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import static org.junit.Assert.assertEquals;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitTest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void AddHabitTest() {
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
        //solo.clickOnCheckBox(3);
        solo.clickInList(1);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.radioYes));
        solo.clickOnView(solo.getView(R.id.submit_button));

        // Check current page as habit list
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));
        // Make sure the habit has been successfully added
        assertTrue(solo.waitForText("robotiumHabit"));

    }

    @Test
    public void ViewHabitTest() {
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

        //Click on the new habit
        solo.clickOnText("robotiumHabit");
        // Check current page as habit detail
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
    }

    @Test
    public void EditHabitTest() {
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

        //Click on the new habit
        solo.clickOnText("robotiumHabit");
        solo.clickOnButton("Edit");
        //Edit habit details
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
        TextView view = (TextView)solo.getView(R.id.textView_habitTitle);
        assertEquals("robotiumHabit",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_habitReason);
        assertEquals("robotiumHabitReasonNew",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_dateOfStarting);
        assertEquals("2013-3-16",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_repeat);
        assertEquals("Tuesday",view.getText().toString());
        view = (TextView)solo.getView(R.id.textView_private);
        assertEquals("false",view.getText().toString());
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

        // Setting up account for friend tests
        // account 1
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));
        solo.enterText((EditText) solo.getView(R.id.editText_username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "robotiumName");
        solo.enterText((EditText) solo.getView(R.id.editText_first_password), "robotiumPw");
        solo.enterText((EditText) solo.getView(R.id.editText_second_password), "robotiumPw");
        solo.clickOnButton("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));

        // Enter in fields for login
        solo.waitForText("robotiumUser");
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.enterText((EditText) solo.getView(R.id.username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.password), "robotiumPw");

        // Click log in
        solo.clickOnButton("LOG IN");

        // Check if entered into habitlistfragment
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));

    }

    //TODO: Delete Habit

    /**
     * Runs after all the tests
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
        removeAccount("robotiumUser");
    }

    private void removeAccount(String username) {
        db.collection("Users").document(username)
                .delete();
        //TODO: Delete nested documents to pass test
    }
}
