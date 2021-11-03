package com.example.habit_tracker;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HabitAddFragment extends Fragment {

    private Button submitButton;
    private EditText habitTitle;
    private EditText habitReason;
    private EditText dateOfStarting;
    private EditText repeat;
    private EditText isPrivate;
    private FirebaseFirestore db;
    private String username;
    private Boolean isPrivateBoolean;


    public HabitAddFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_habit_add, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("username")){
            username = bundle.getString("username");
        }


        return rootView;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean checkDateValidity(final String date) {
        boolean valid = false;
        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu/MM/dd")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            valid = true;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        habitTitle = (EditText) getView().findViewById(R.id.editText_habitTitle);
        habitReason = (EditText) getView().findViewById(R.id.editText_habitReason);
        dateOfStarting = (EditText) getView().findViewById(R.id.editText_dateOfStarting);
        repeat = (EditText) getView().findViewById(R.id.editText_repeat);
        isPrivate = (EditText) getView().findViewById(R.id.editText_isPrivate);

        submitButton = (Button) getView().findViewById(R.id.submit_button);


        CollectionReference collectionReference = db.collection("Users").document(username).collection("HabitList");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final boolean[] isValid = {true};

                if (0 >= habitTitle.getText().toString().length() || 20 <= habitTitle.getText().toString().length()) {
                    isValid[0] = false;
                    habitTitle.setError("Habit name not valid. Please ensure that it is between 0 and 20 characters.");
                    return;
                }

                if (30 <= habitReason.getText().toString().length()) {
                    isValid[0] = false;
                    habitReason.setError("The reason should be less than 30 characters.");
                    return;
                }

                if (dateOfStarting.getText().toString().length() >= 0){
                    isValid[0] = checkDateValidity(dateOfStarting.getText().toString());
                    if (isValid[0] == false){
                        dateOfStarting.setError("Invalid date format! Please enter date in yyyy/mm/dd.");
                        return;
                    }

                }

                if (repeat.getText().toString().length() >= 30){
                    isValid[0] = false;
                    repeat.setError("Please enter a string less than 30 characters.");
                    return;
                }

                if (isPrivate.getText().toString().toLowerCase().equals("yes")) {
                    isPrivateBoolean = true;
                } else if (isPrivate.getText().toString().toLowerCase().equals("no")){
                    isPrivateBoolean = false;
                }else {
                    isValid[0] = false;
                    isPrivate.setError("Your input should be Yes or No.");
                    return;
                }

                HashMap<String, String> data = new HashMap<>();

                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString();

                if (isValid[0] == true) {
                    data.put("title", habitTitle.getText().toString());
                    data.put("reason", habitReason.getText().toString());
                    data.put("repeat", repeat.getText().toString());
                    data.put("dateOfStarting", dateOfStarting.getText().toString());
                    data.put("isPrivate", isPrivateBoolean.toString());
                    collectionReference
                            .document(uuidString)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", username);

                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_habitAddFragment_to_habitListFragment, bundle);
                                    Toast.makeText(getContext(), "Success - Successfully added this habit to the database", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_habitAddFragment_to_habitListFragment);
                                    Toast.makeText(getContext(), "Failure - Failed to insert into database.", Toast.LENGTH_LONG).show();
                                }
                            });

                }


            }
        });

    }
}