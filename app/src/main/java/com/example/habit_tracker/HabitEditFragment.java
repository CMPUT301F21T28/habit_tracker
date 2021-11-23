package com.example.habit_tracker;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HabitEditFragment extends Fragment {
    private Button submitButton;
    private EditText habitTitle;
    private EditText habitReason;
    private EditText dateOfStarting;

    private TextView repeatDay;
    private boolean[] selectedDay;
    private ArrayList<Integer> dayList = new ArrayList<>();
    private String[] dayArray = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    private String selectedDayString;

    private RadioGroup radioGroup;
    private FirebaseFirestore db;
    private String username;

    private Habit habit;

    private Boolean isPrivate;
    private Integer habitOrder;

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

        username = bundle.getString("username");
        habit = bundle.getParcelable("Habit");
        habitOrder = habit.getOrder();
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
        repeatDay = (TextView) getView().findViewById(R.id.textView_select_day2);

        radioGroup = getView().findViewById(R.id.radioGroup2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioYes:
                        isPrivate = true;
                        Toast.makeText(getActivity(), "Set the habit to private", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioNo:
                        isPrivate = false;
                        Toast.makeText(getActivity(), "Set the habit to public", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        submitButton = (Button) getView().findViewById(R.id.button_submit);

        habitTitle.setText(habit.getName());
        habitReason.setText(habit.getComment());
        dateOfStarting.setText(habit.getDateOfStarting());
        repeatDay.setText(habit.getRepeat());
        selectedDayString = habit.getRepeat();



        if (habit.getIsPrivate() == true) {
            radioGroup.check(R.id.radioYes);
        } else {
            radioGroup.check(R.id.radioNo);
        }



        selectedDay = new boolean[dayArray.length];
        repeatDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: repeat clicked");
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getContext()
                );
                builder.setTitle("Select Day");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b){
                            //WHen checkbox is selected add position in day list
                            dayList.add(i);
                            //Sort dayList
                            Collections.sort(dayList);
                        }else {
                            //When checkbox is unselected, remove position from the list
                            dayList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (dayList.size() > 0) {
                            for (int j = 0; j < dayList.size(); j ++){
                                //concat array value
                                stringBuilder.append(dayArray[dayList.get(j)]);
                                //check condition
                                if (j != dayList.size() - 1) {
                                    stringBuilder.append(", ");
                                }
                            }
                            //set text on textView
                            selectedDayString = stringBuilder.toString();
                            repeatDay.setText(selectedDayString);
                        } else {
                            selectedDayString = null;
                            repeatDay.setText("Select Day");
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //Use for loop
                        for (int j = 0; j < selectedDay.length; j++) {
                            //remove all selection
                            selectedDay[j] = false;
                            dayList.clear();
                            repeatDay.setText("Select Day");
                            selectedDayString = null;
                        }
                    }
                });
                //show dialog
                builder.show();

            }
        });


        CollectionReference collectionReference = db.collection("Users").document(username).collection("HabitList");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //final boolean[] isValid = {true};
                //Check if input is in range
                boolean inputValid = checkInputValidity(habitTitle,0,20) && checkInputValidity(habitReason,0,30)
                        && checkInputValidity(dateOfStarting,0,20) && selectedDayString != null && isPrivate != null;
                if (inputValid == false){
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if input date is valid
                if (checkDateValidity(dateOfStarting.getText().toString()) == false){
                    dateOfStarting.setError("Invalid date format! Please enter date in yyyy/mm/dd.");
                    inputValid = false;
                    return;
                }


                //set the isPrivate to be true if the user enters yes, false if the user enters no.

                HashMap<String, Object> data = new HashMap<>();

                if (inputValid) {
                    data.put("title", habitTitle.getText().toString());
                    data.put("reason", habitReason.getText().toString());
                    data.put("repeat", selectedDayString);
                    data.put("dateOfStarting", dateOfStarting.getText().toString());
                    data.put("isPrivate", isPrivate);
                    data.put("order", habitOrder);

                    habit.setHabitTitle(habitTitle.getText().toString());
                    habit.setDateOfStarting(dateOfStarting.getText().toString());
                    habit.setReason(habitReason.getText().toString());
                    habit.setRepeat(selectedDayString);
                    habit.setPrivate(isPrivate);

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