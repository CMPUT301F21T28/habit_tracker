package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private Button signInButton;
    private EditText editTextUsername, editTextPassword;
    private TextView navToSignup;
    FirebaseFirestore db;
    boolean loginSuccess = false;

    Button toLoginButton;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        return view;
    }

    /**
     * This function validates user input. It checks if any of the fields is empty and if the username and password is a valid match.
     * Then it calls setLoginSuccess to proceed to the next step
     * @param username
     * @param password
     * @param view
     */
    public void validateInput(String username, String password, View view){
        if(username.isEmpty()){
            setLoginSuccess(false,view,username);
        }
        if(password.isEmpty()){
            setLoginSuccess(false,view,username);
        }
        db = FirebaseFirestore.getInstance();
        final CollectionReference usernameRef = db.collection("Users");
        Query query = usernameRef.whereEqualTo("username", username);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String correctPassword = documentSnapshot.getString("password");

                        // Converting password to hashed password
                        String hashedPw = null;
                        try {
                            hashedPw = toHexString(getSHA(password));
                        } catch (NoSuchAlgorithmException e) {
                            // SHOULD NEVER OCCUR GIVEN THAT SHA-256 IS A THING
                            e.printStackTrace();
                        }
                        // If password is correct
                        if (correctPassword.equals(hashedPw)) {
                            Log.d("Success", "Log in successful.");
                            setLoginSuccess(true,view,username);

                        } else {
                            // If password is incorrect
                            Log.d("Check Password", "Password does not match.");
                            setLoginSuccess(false,view,username);
                        }
                    }
                } if(task.getResult().size() == 0 ){
                    // If username does not exist in firestore
                    Log.d("Failure", "User not Exists");
                    setLoginSuccess(false,view,username);
                    //You can store new user information here

                }

            }
        });
    }

    /**
     * After validating user input, this function decides if the user could go to the main page or throw an error saying input is invalid.
     * @param value
     * @param view
     * @param username
     */
    private void setLoginSuccess(boolean value, View view, String username){
        loginSuccess = value;
        if (value == true){
            //takes successful logged in users to the main page
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            NavController controller = Navigation.findNavController(view);
            controller.navigate(R.id.action_loginFragment_to_mainPageFragment, bundle);
        }
        else {
            //asks unsuccessful users to retry
            Log.d("Failure", "login failed");
            editTextUsername.setError("Login failed. Please check your username and password.");
            editTextPassword.setError("Login failed. Please check your username and password.");
        }
    }

    /**
     * testing function for user input validation
     * @return
     * returns boolean of if user input is valid
     */
    public boolean getLoginSuccess(){
        return loginSuccess;
    }

    /**
     * This function sets up the fragment of log in. It takes the user input of username and password, then calls the function that checks if it matches any record in firestore database.
     * If it does, it leads the user to the main page. Otherwise, it suggests the user that either username does not exist or password does not match.
     * The user can choose to retry if password does not match or sign up if username does not exist
     * @param
     * view, bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInButton = (Button) getView().findViewById(R.id.Nav_to_mainpage);
        editTextUsername = (EditText) getView().findViewById(R.id.username);
        editTextPassword = (EditText) getView().findViewById(R.id.password);
        navToSignup = (TextView) getView().findViewById(R.id.Nav_to_signup);

        db = FirebaseFirestore.getInstance();

        navToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_loginFragment_to_signupFragment);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                validateInput(username,password,view);

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

}