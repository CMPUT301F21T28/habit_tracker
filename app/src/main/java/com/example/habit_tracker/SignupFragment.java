package com.example.habit_tracker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
    private OnFragmentInteractionListener listener;
    private FirebaseFirestore db;

    public SignupFragment() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        void onSubmitPressed(); // args to pass to MainActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // bind editText fields to their variable
        username = getView().findViewById(R.id.editText_username);
        realName = getView().findViewById(R.id.editText_name);
        gender = getView().findViewById(R.id.editText_gender);
        email = getView().findViewById(R.id.editText_email_address);
        firstPassword = getView().findViewById(R.id.editText_first_password);
        secondPassword = getView().findViewById(R.id.editText_second_password);
        signupButton = getView().findViewById(R.id.signupButton);
        returnToLoginButton = getView().findViewById(R.id.returnToLoginFloatingActionButton);

        // setting onclick listeners for the buttons
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        // **** check if fields are valid
        boolean isValid = true;
        // check if username valid
        if (0 >= username.getText().toString().length() || username.toString().length() >= 20) {
            username.setError("Username not valid. Please ensure that it is between 0 and 20 characters.");
        }
        // check if username already exists in db
        // https://stackoverflow.com/questions/52861391/firestore-checking-if-username-already-exists
        CollectionReference usernameRef = db.collection("Users");
        Query query = usernameRef.whereEqualTo("username", username);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String user = documentSnapshot.getString("username");
                        if (user.equals(username.getText().toString())) {
                            Log.d("Check Username", "Username already exists.");
                            username.setError("Username already exists. Please choose another");
                        }
                    }
                }
                if (task.getResult().size() == 0) {
                    Log.d("Check Username", "Username is not taken.");
                }
            }
        });
        // check realname - as long as not empty
        if (realName.getText().toString().isEmpty()) {
            realName.setError("This field cannot be empty.");
            isValid = false;
        }

        // check password matching
        if (!firstPassword.getText().toString().equals(secondPassword.getText().toString())) {
            secondPassword.setError("Passwords do not match. Please try again.");
        }


        // **** if all fields are valid write to the database


        // **** if not all fields are valid return errors

        return view;
    }

    public void sendError(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setNegativeButton("OK", null)
                .show();
    }
}