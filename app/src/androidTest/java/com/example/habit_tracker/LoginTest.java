/*
Disclaimer regarding tests with fragments: Due to Robotium's fragment listening services not working at all
eg. solo.waitForFragment etc.
under the consultation of our Mentoring TA, we decided to use our Layout Views as the indicator that we
are in the correct view.
 */

package com.example.habit_tracker;

import android.app.Activity;
import android.widget.EditText;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginTest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Solo solo;

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
    public void NavToSignup() {
        solo.assertCurrentActivity("WrongActivity", MainActivity.class);
        // Ensuring that we're in the login fragment
        assertNotNull(solo.getView(R.id.login_frag_linear_layout));

        solo.clickOnText("Sign Up");
        // checking if we are in Signup fragment
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));
    }

    @Test
    public void NavFromSignup() {
        solo.assertCurrentActivity("WrongActivity", MainActivity.class);
        assertNotNull(solo.getView(R.id.login_frag_linear_layout));
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));

        // Click cancel and ensure that it goes back to login
        solo.clickOnText("Cancel");
        assertNotNull(solo.getView(R.id.login_frag_linear_layout));
    }

    @Test
    public void CreateAccount() {
        solo.assertCurrentActivity("WrongActivity", MainActivity.class);
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));

        // Enter in fields
        solo.enterText((EditText) solo.getView(R.id.editText_username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.editText_first_password), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.editText_second_password), "robotiumUser");

        // Click Button
        solo.clickOnButton("Sign Up");

        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));
    }

    @Test
    public void Login() {
        solo.assertCurrentActivity("WrongActivity", MainActivity.class);
        solo.clickOnText("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));

        // Enter in fields
        solo.enterText((EditText) solo.getView(R.id.editText_username), "robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "robotiumName");
        solo.enterText((EditText) solo.getView(R.id.editText_first_password), "robotiumPw");
        solo.enterText((EditText) solo.getView(R.id.editText_second_password), "robotiumPw");

        // Click Signup
        solo.clickOnButton("Sign Up");
        assertNotNull(solo.getView(R.id.signup_fragment_constraint_layout));

        // Enter in fields for login
        solo.waitForText("robotiumUser");
        solo.enterText((EditText) solo.getView(R.id.password), "robotiumPw");

        // Click log in
        solo.clickOnButton("LOG IN");

        // Check if entered into habitlistfragment
        assertNotNull(solo.getView(R.id.habit_list_constraint_layout));
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
        removeAccount("robotiumUser");
    }


    public void removeAccount(String username) {
        db.collection("Users").document(username)
                .delete();
    }
}

