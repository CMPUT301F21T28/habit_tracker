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

        Button testButton = getView().findViewById(R.id.testbutton3);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_loginFragment_to_addHabbitEventFragment);
            }
        });

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
                                if (correctPassword.equals(password)) {
                                    Log.d("Success", "Log in successful.");
                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_loginFragment_to_mainPageFragment);

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

//        getView().findViewById(R.id.toSignup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavController controller = Navigation.findNavController(view);
//                controller.navigate(R.id.action_loginFragment_to_signupFragment);
//            }
//        });
    }

}