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
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends DialogFragment {

    // Defining edittext fields in the xml
    private Button signupButton;
    private FloatingActionButton returnToLoginButton;
    private EditText username;
    private EditText realName;
    private EditText gender;
    private EditText email;
    private EditText firstPassword;
    private EditText secondPassword;
    private FirebaseFirestore db;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // intializing connection to firestore
        db = FirebaseFirestore.getInstance();
        // bind editText fields to their variable
        username = (EditText) getView().findViewById(R.id.editText_username);
        realName = (EditText) getView().findViewById(R.id.editText_name);
        gender = (EditText) getView().findViewById(R.id.editText_gender);
        email = (EditText) getView().findViewById(R.id.editText_email_address);
        firstPassword = (EditText) getView().findViewById(R.id.editText_first_password);
        secondPassword = (EditText) getView().findViewById(R.id.editText_second_password);
        signupButton = (Button) getView().findViewById(R.id.signupButton);
        returnToLoginButton = (FloatingActionButton) getView().findViewById(R.id.returnToLoginFloatingActionButton);

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavController controller = Navigation.findNavController(view);
//                controller.navigate(R.id.action_signupFragment_to_loginFragment);
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_signupFragment_to_loginFragment);
                Toast.makeText(getContext(), "Return to Login Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // setting onclick listeners for the buttons
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // **** check if fields are valid, setError on those that arent valid
                // **** if all fields are valid, check if username already exists
                // **** if username does exist, return error
                // **** if username does NOT exist, then we sign them up
                final boolean[] isValid = {true};
                // check if username valid
                if (0 >= username.getText().toString().length() || username.getText().toString().length() >= 20) {
                    isValid[0] = false;
                    username.setError("Username not valid. Please ensure that it is between 0 and 20 characters.");
                    return;
                }

                // check realname - as long as not empty
                if (realName.getText().toString().isEmpty()) {
                    isValid[0] = false;
                    realName.setError("This field cannot be empty.");
                    return;
                }

                // check if password is empty
                if (firstPassword.getText().toString().isEmpty()) {
                    isValid[0] = false;
                    firstPassword.setError("Password cannot be empty. Please try again.");
                    return;
                }

                // check password matching
                if (!firstPassword.getText().toString().equals(secondPassword.getText().toString())) {
                    isValid[0] = false;
                    secondPassword.setError("Passwords do not match. Please try again.");
                    return;
                }

                // check if username already exists in db
                // https://stackoverflow.com/questions/52861391/firestore-checking-if-username-already-exists
                Toast.makeText(getContext(), username.getText().toString(), Toast.LENGTH_SHORT).show();
                DocumentReference usersRef = db.collection("Users").document(username.getText().toString());
                usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // if the username is taken
                                Log.d("Check Username", "Username already exists.");
                                isValid[0] = false;
                                username.setError("Username already exists. Please choose another");
                                return;
                            } else { // if the username is not taken
                                Log.d("Check Username", "Username is not taken.");

                                // **** hashing pw
                                String hashedPw = null;
                                try {
                                    hashedPw = toHexString(getSHA(firstPassword.getText().toString()));
                                } catch (NoSuchAlgorithmException e) {
                                    // SHOULD NEVER OCCUR GIVEN THAT SHA-256 IS A THING
                                    e.printStackTrace();
                                }

                                // username not taken
                                Map<String, Object> data = new HashMap<>();
                                data.put("username", username.getText().toString());
                                data.put("realname", realName.getText().toString());
                                data.put("gender", gender.getText().toString());
                                data.put("emailaddress", email.getText().toString());
                                data.put("password", hashedPw);
                                CollectionReference Users = db.collection("Users");
                                Users.document(username.getText().toString()).set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Successful Registration. Welcome to the Habit Tracker!", Toast.LENGTH_SHORT).show();
                                                NavController controller = Navigation.findNavController(view);
                                                controller.navigate(R.id.action_signupFragment_to_loginFragment);
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
                            isValid[0] = false;
                            return;
                        }
                    }
                });
            }
        });
    }

    /**
     * Hashing support function. SRC: https://www.geeksforgeeks.org/sha-256-hash-in-java/
     * @param input
     *      string to be hashed
     * @return
     *      returns the hashed result as a bytearray
     * @throws NoSuchAlgorithmException
     *      if the hash function specified does not exist. SHOULD NOT OCCUR
     */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Hashing support function. SRC: https://www.geeksforgeeks.org/sha-256-hash-in-java/
     * @param hash
     *      byte array input of something that is hashed in SHA256
     * @return
     *      returns the string representation of the hash in hex
     */
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public void sendError(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setNegativeButton("OK", null)
                .show();
    }
}