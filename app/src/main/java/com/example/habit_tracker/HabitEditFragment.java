package com.example.habit_tracker;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;

public class HabitEditFragment extends Fragment {
    private Button submitButton;
    private EditText habitTitle;
    private EditText habitReason;
    private EditText dateOfStarting;
    private EditText repeat;
    private EditText isPrivate;
    private FirebaseFirestore db;
    private String username;

    private Habit habit;

    private Boolean isPrivateBoolean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create view for HabitAddFragment, extract necessities (e.g. username, instance of Habit class) from the bundle
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_habit_edit, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("username")){
            username = bundle.getString("username");
            habit = bundle.getParcelable("Habit");
        }
        return rootView;
    }

    /**
     * Check if the input date is valid
     * @param date
     * @return A boolean specify if the input date is valid
     */
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

    /**
     * Check if the input title, reason, date are valid
     * @param editTextView, lower, upper
     * @return A boolean specify if the input date is valid
     */
    public boolean checkInputValidity(EditText editTextView, int lower, int upper){
        if (editTextView.getText().toString().length() < lower || editTextView.getText().toString().length() > upper){
            editTextView.setError("Not valid. Please ensure that it is between " +lower + " and "+ upper + " characters.");
            return false;
        }
        return true;
    }

    /**
     * Initialize all other parts that could cause the fragment status change
     * Connect to firebase DB, check the validity for all other inputs, send the fields to DB
     * Fragment change by navigation
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        habitTitle = (EditText) getView().findViewById(R.id.editText_habitTitle2);
        habitReason = (EditText) getView().findViewById(R.id.editText_habitReason2);
        dateOfStarting = (EditText) getView().findViewById(R.id.editText_dateOfStarting2);
        repeat = (EditText) getView().findViewById(R.id.editText_repeat2);
        isPrivate = (EditText) getView().findViewById(R.id.editText_isPrivate2);

        submitButton = (Button) getView().findViewById(R.id.button_submit);

        habitTitle.setText(habit.getName());
        habitReason.setText(habit.getComment());
        dateOfStarting.setText(habit.getDateOfStarting());
        repeat.setText(habit.getRepeat());

        if (habit.getIsPrivate() == false){
            isPrivate.setText("No");
        } else if (habit.getIsPrivate() == true){
            isPrivate.setText("Yes");
        }

        CollectionReference collectionReference = db.collection("Users").document(username).collection("HabitList");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //final boolean[] isValid = {true};
                //Check if input is in range
                boolean inputValid = checkInputValidity(habitTitle,0,20) && checkInputValidity(habitReason,0,30)
                        && checkInputValidity(dateOfStarting,0,20) && checkInputValidity(repeat,0,30);
                if (inputValid == false){
                    return;
                }
                // Check if input date is valid
                if (checkDateValidity(dateOfStarting.getText().toString()) == false){
                    dateOfStarting.setError("Invalid date format! Please enter date in yyyy/mm/dd.");
                    inputValid = false;
                    return;
                }


                //set the isPrivate to be true if the user enters yes, false if the user enters no.
                if (isPrivate.getText().toString().toLowerCase().equals("yes")) {
                    isPrivateBoolean = true;
                } else if (isPrivate.getText().toString().toLowerCase().equals("no")){
                    isPrivateBoolean = false;
                }else {
                    //isValid[0] = false;
                    inputValid = false;
                    isPrivate.setError("Your input should be Yes or No.");
                    return;
                }

                HashMap<String, String> data = new HashMap<>();

                if (inputValid == true) {
                    data.put("title", habitTitle.getText().toString());
                    data.put("reason", habitReason.getText().toString());
                    data.put("repeat", repeat.getText().toString());
                    data.put("dateOfStarting", dateOfStarting.getText().toString());
                    data.put("isPrivate", isPrivateBoolean.toString());

                    habit.setHabitTitle(habitTitle.getText().toString());
                    habit.setDateOfStarting(dateOfStarting.getText().toString());
                    habit.setReason(habitReason.getText().toString());
                    habit.setRepeat(repeat.getText().toString());
                    habit.setPrivate(isPrivateBoolean);

                    collectionReference
                            .document(habit.getHabitID())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", username);
                                    bundle.putParcelable("Habit", habit);

                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_habitEditFragment_to_habitListFragment, bundle);
                                    //Toast.makeText(getContext(), "Success - Successfully added this habit to the database", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_habitEditFragment_to_habitListFragment);
                                    //Toast.makeText(getContext(), "Failure - Failed to insert into database.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

    }
}