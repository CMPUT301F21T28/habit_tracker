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
import android.widget.Toast;

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
    Button toLoginButton;
    String realname;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getView().findViewById(R.id.Nav_to_mainpage).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavController controller = Navigation.findNavController(view);
//                controller.navigate(R.id.action_loginFragment_to_mainPageFragment);
//            }
//        });
        //View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login, null);
        signInButton = (Button) getView().findViewById(R.id.Nav_to_mainpage);
        editTextUsername = (EditText) getView().findViewById(R.id.username);
        editTextPassword = (EditText) getView().findViewById(R.id.password);
        navToSignup = (TextView) getView().findViewById(R.id.Nav_to_signup);
        db = FirebaseFirestore.getInstance();

        // Check if bundle from signup exists
        try {
            Bundle bundle = getArguments();
            if (!bundle.isEmpty()) {
                editTextUsername.setText(bundle.getString("username"));
            }
        } catch (Exception e) {}

        // Defining the onclicklisteners
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

                if(username.isEmpty()){
                    //Log.d("Fail", "empty field");
                    editTextUsername.setError("Username is required!");
                    editTextUsername.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    //Log.d("Fail", "empty field");
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }

                final CollectionReference usernameRef = db.collection("Users");
                Query query = usernameRef.whereEqualTo("username", username);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String correctPassword = documentSnapshot.getString("password");
                                realname = documentSnapshot.getString("realname");
                                // Converting password to hashed password
                                String hashedPw = null;
                                try {
                                    hashedPw = toHexString(getSHA(password));
                                } catch (NoSuchAlgorithmException e) {
                                    // SHOULD NEVER OCCUR GIVEN THAT SHA-256 IS A THING
                                    e.printStackTrace();
                                }

                                if (validPassword(password,correctPassword)) {
                                    Log.d("Success", "Log in successful.");
                                    // Creating bundle to pass information to next fragment
                                    Bundle outgoingBundle = new Bundle();
                                    outgoingBundle.putString("username", username);
                                    outgoingBundle.putString("realname", realname);

                                    // Navigating to the next fragment
                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_loginFragment_to_mainPageFragment, outgoingBundle);

                                } else {
                                    Log.d("Check Password", "Password does not match.");
                                    editTextPassword.setError("Password is incorrect. Please try again.");
                                }
                            }
                        } if(task.getResult().size() == 0 ){
                            Log.d("Failure", "User not Exists");
                            editTextUsername.setError("User does not exist. Please sign up first.");
                            //You can store new user information here
                        }
                    }
                });
            }
        });

    }

    public boolean validPassword(String inputPassword, String dbPassword){
        // Converting password to hashed password
        String hashedPw = null;
        try {
            hashedPw = toHexString(getSHA(inputPassword));
        } catch (NoSuchAlgorithmException e) {
            // SHOULD NEVER OCCUR GIVEN THAT SHA-256 IS A THING
            e.printStackTrace();
        }
        if (dbPassword.equals(hashedPw)){
            return true;
        } else {
            return false;
        }
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