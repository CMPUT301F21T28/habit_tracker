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
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_signup, null);
        // return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bind editText fields to their variable
        username = getView().findViewById(R.id.editText_username);
        realName = getView().findViewById(R.id.editText_name);
        gender = getView().findViewById(R.id.editText_gender);
        email = getView().findViewById(R.id.editText_email_address);
        firstPassword = getView().findViewById(R.id.editText_first_password);
        secondPassword = getView().findViewById(R.id.editText_second_password);
        signupButton = getView().findViewById(R.id.signupButton);
        returnToLoginButton = getView().findViewById(R.id.returnToLoginFloatingActionButton);

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_signupFragment_to_loginFragment);
            }
        });

        // setting onclick listeners for the buttons
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // **** check if fields are valid
                final boolean[] isValid = {true};
                // check if username valid
                if (0 >= username.getText().toString().length() || username.toString().length() >= 20) {
                    username.setError("Username not valid. Please ensure that it is between 0 and 20 characters.");
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
                                Log.d("Check Username", "Username already exists.");
                                username.setError("Username already exists. Please choose another");
                                isValid[0] = false;
                            } else {
                                Log.d("Check Username", "Username is not taken.");
                            }
                        } else {
                            Log.d("Document Get", "Failed");
                            Toast.makeText(getContext(), "Error accessing database", Toast.LENGTH_SHORT).show();
                            isValid[0] = false;
                        }
                    }
                });

                // check realname - as long as not empty
                if (realName.getText().toString().isEmpty()) {
                    realName.setError("This field cannot be empty.");
                    isValid[0] = false;
                }

                // check if password is empty
                if (firstPassword.getText().toString().isEmpty()) {
                    firstPassword.setError("Password cannot be empty. Please try again.");
                    isValid[0] = false;
                }

                // check password matching
                if (!firstPassword.getText().toString().equals(secondPassword.getText().toString())) {
                    secondPassword.setError("Passwords do not match. Please try again.");
                    isValid[0] = false;
                }

                // **** if not all fields are valid return errors
                // **** if all fields are valid write to the database
                if (isValid[0] == false) {
                    // there are errors
                    Toast.makeText(getContext(), "Some fields are invalid. Please try again.", Toast.LENGTH_LONG).show();
                } else {
                    // no errors
                    Map<String, Object> data = new HashMap<>();
                    data.put("username", username.getText().toString());
                    data.put("realname", realName.getText().toString());
                    data.put("gender", gender.getText().toString());
                    data.put("emailaddress", email.getText().toString());
                    data.put("password", firstPassword.getText().toString());
                    CollectionReference Users = db.collection("Users");
                    Users.document(username.getText().toString()).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Successful Registration. Welcome to the Habit Tracker!", Toast.LENGTH_SHORT).show();
                                    // Sets all fields empty in even of success
                                    username.setText("");
                                    realName.setText("");
                                    gender.setText("");
                                    email.setText("");
                                    firstPassword.setText("");
                                    secondPassword.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Registration Failure - Failed to insert into database.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    public void sendError(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setNegativeButton("OK", null)
                .show();
    }
}