package com.example.habit_tracker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends DialogFragment {

    // Defining edittext fields in the xml
    private Button signupButton;
    private TextView returnToLoginButton;
    private EditText username;
    private EditText realName;
    private EditText firstPassword;
    private EditText secondPassword;
    private FirebaseFirestore db;
    private static final Random _random = new SecureRandom();

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    /**
     * Called after the view is created. User Interface fields (eg. edittext) are bound to their variables,
     * firestore db is initialised, and onClickListeners are set up for each button.
     *
     * returnToLoginButton - onclick listener is set up to return to the login fragment, discarding any
     *      progress the user has made on this fragment
     *
     * signupButton - onclick listener starts the process of checking field validity, the
     *      updating the database with the new user if all the information is valid.
     *      It also returns a bundle with the user's username to the login fragment to
     *      make signin more streamlined.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // intializing connection to firestore
        db = FirebaseFirestore.getInstance();
        // bind editText fields to their variable
        username = (EditText) getView().findViewById(R.id.editText_username);
        realName = (EditText) getView().findViewById(R.id.editText_name);
        firstPassword = (EditText) getView().findViewById(R.id.editText_first_password);
        secondPassword = (EditText) getView().findViewById(R.id.editText_second_password);
        signupButton = (Button) getView().findViewById(R.id.signupButton);
        returnToLoginButton = (TextView) getView().findViewById(R.id.returnToLoginTextView);

        /**
         * Clicking button returns to login
         */
        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_signupFragment_to_loginFragment);
            }
        });

        /**
         * Clicking button starts the process of checking field validity, the updating the database
         * with the new user if all the information is valid. It also returns a bundle with the
         * user's username to the login fragment to make signin easier.
         */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // **** check if fields are valid, setError on those that arent valid
                // **** if all fields are valid, check if username already exists
                // **** if username does exist, return error
                // **** if username does NOT exist, then we sign them up
                // check if username valid
                if (!checkUsernameValid(username.getText().toString())) {
                    username.setError("Username not valid. Please ensure that it is between 0 and 20 characters.");
                    username.requestFocus();
                    return; // breaks the onclick, so that user can change input
                }

                // check realname - as long as not empty
                if (!checkRealnameValid(realName.getText().toString())) {
                    realName.setError("This field cannot be empty.");
                    realName.requestFocus();
                    return; // breaks the onclick, so that user can change input
                }

                // check if password is valid
                if (!checkFirstPassValid(firstPassword.getText().toString())) {
                    firstPassword.setError("Password must be longer than 5 characters");
                    firstPassword.requestFocus();
                    return;
                }
                // check password matching
                if (!checkSecondPassValid(firstPassword.getText().toString(), secondPassword.getText().toString())) {
                    secondPassword.setError("Passwords do not match. Please try again.");
                    secondPassword.requestFocus();
                    return; // breaks the onclick, so that user can change input
                }

                // check if username already exists in db
                // https://stackoverflow.com/questions/52861391/firestore-checking-if-username-already-exists
                DocumentReference usersRef = db.collection("Users").document(username.getText().toString());
                usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // if the username is taken
                                Log.d("Check Username", "Username already exists.");
                                username.setError("Username already exists. Please choose another");
                                username.requestFocus();
                                return;
                            } else { // if the username is not taken
                                Log.d("Check Username", "Username is not taken.");

                                // **** hashing pw
                                String hashedPw = null;
                                String salt = Utility.getNextSalt();
                                try {
                                    // Salts password at the same time
                                    hashedPw = Utility.toHexString(Utility.getSHA(firstPassword.getText().toString().concat(salt)));
                                } catch (NoSuchAlgorithmException e) {
                                    // SHOULD NEVER OCCUR GIVEN THAT SHA-256 IS A THING
                                    e.printStackTrace();
                                }

                                // username not taken
                                Map<String, Object> data = new HashMap<>();
                                data.put("username", username.getText().toString());
                                data.put("realname", realName.getText().toString());
                                data.put("password", hashedPw);
                                data.put("salt", salt);
                                data.put("friends", Collections.emptyList()); // empty list since there are no friends on the acc yet
                                data.put("requests", Collections.emptyList()); // empty list since there are no friend requests yet
                                CollectionReference Users = db.collection("Users");
                                Users.document(username.getText().toString()).set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Successful Registration. Welcome to the Habit Tracker!", Toast.LENGTH_SHORT).show();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("username", username.getText().toString());

                                                NavController controller = Navigation.findNavController(view);
                                                controller.navigate(R.id.action_signupFragment_to_loginFragment, bundle);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Registration Failure - Failed to insert into database.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            Log.d("Document Get", "Failed");
                            Toast.makeText(getContext(), "Error accessing database", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }

    /**
     * Checking if inputted username is valid (follows length guidelines)
     * @param username
     *      The String of the username that is being tested
     * @return
     *      return True if everything is ok
     */
    public static boolean checkUsernameValid(String username) {
        // testing username bounds
        return 0 < username.length() && username.length() < 20;
    }

    /**
     * Checking if inputted username is valid (follow not null guidelines)
     * @param realName
     *      the String of the realname
     * @return
     *      return True if everything is ok
     */
    public static boolean checkRealnameValid(String realName) {
        return !realName.isEmpty();
    }

    /**
     * Checks if the password and the comfirm password fields are valid.
     * (longer than 5 characters, passwords match)
     * @param firstpass
     *      the String of the firstpassword input
     * @return
     *      return True if everything is ok
     */
    public static boolean checkFirstPassValid(String firstpass) {
        // check if password is empty
        return firstpass.length() > 5;
    }

    public static boolean checkSecondPassValid(String firstpass, String secondpass) {
        // check second pass password matching
        return firstpass.equals(secondpass);
    }
}