package com.example.habit_tracker;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/**
 * HabitEditFragment creates a fragment to edit the details of a habit
 */

public class HabitEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private FloatingActionButton submitButton;
    private EditText habitTitle;
    private EditText habitReason;
    private TextView dateOfStarting;
    private RadioGroup radioGroup;
    private TextView repeatDay;
    private EditText plan;

    private boolean[] selectedDay;
    private ArrayList<Integer> dayList = new ArrayList<>();
    private String[] dayArray = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    private String selectedDayString;

    private FirebaseFirestore db;

    private String username;
    private Habit habit;

    private Boolean isPrivate;
    private Integer habitOrder;
    private String datePicked;

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
        dateOfStarting = (TextView) getView().findViewById(R.id.textDateStarting);
        repeatDay = (TextView) getView().findViewById(R.id.textView_select_day2);
        plan = (EditText) getView().findViewById(R.id.editTextTime);

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

        submitButton = (FloatingActionButton) getView().findViewById(R.id.button_submit);

        habitTitle.setText(habit.getName());
        habitReason.setText(habit.getComment());
        datePicked = habit.getDateOfStarting();
        dateOfStarting.setText("Starts on " +datePicked);
        repeatDay.setText("Repeat on every " + habit.getRepeat());
        selectedDayString = habit.getRepeat();
        plan.setHint("Plan to complete " + habit.getPlan() + " events");

        // handles the radio button, if the habit is set to private
        if (habit.getIsPrivate() == true) {
            radioGroup.check(R.id.radioYes);
        } else {
            radioGroup.check(R.id.radioNo);
        }

        // handles the repeat selector
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
                //if the user click Ok, then check if the date selected is null.
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

                //if the user click cancel, then nothing happens
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //if the user click clear all then the list is cleared.
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

        // handle the date picker
        dateOfStarting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        CollectionReference collectionReference = db.collection("Users").document(username).collection("HabitList");

        // handles the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //final boolean[] isValid = {true};
                //Check if input is in range
                boolean inputValid = checkInputValidity(habitTitle,0,20) && checkInputValidity(habitReason,-1,30)
                        && selectedDayString != null && isPrivate != null;
                if (inputValid == false){
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if the "times" work
                boolean isTimesValid = true;
                Integer times = 0;
                if (plan.getText().toString().length() == 0) {
                    times = habit.getPlan();
                } else {
                    times = Integer.parseInt(plan.getText().toString());
                    if (times > 1000 || times <= 0) {
                        isTimesValid = false;
                        plan.setError("Please enter an positive Integer less or equal to 1000 times");
                        return;
                    }
                }

                HashMap<String, Object> data = new HashMap<>();

                if (inputValid) {
                    data.put("title", habitTitle.getText().toString());
                    data.put("reason", habitReason.getText().toString());
                    data.put("repeat", selectedDayString);
                    data.put("dateOfStarting", datePicked);
                    data.put("isPrivate", isPrivate);
                    data.put("order", habitOrder);
                    data.put("plan", times);
                    data.put("finish", habit.getFinish());
                    collectionReference
                            .document(habit.getHabitID())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", username);

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


    /**
     * Check if the input title, reason, date are valid
     * @param editTextView, lower, upper
     * @return A boolean specify if the input date is valid
     */
    public boolean checkInputValidity(EditText editTextView, int lower, int upper){
        //isStringValid(editTextView.getText().toString(), lower, upper);
        if (isStringValid(editTextView.getText().toString(), lower, upper) == false){
            editTextView.setError("Not valid. Please ensure that it is between " +lower + " and "+ upper + " characters.");
            return false;
        }
        return true;
    }

    /**
     * check if the length of the string is between lower and upper
     * @param string
     * @param lower
     * @param upper
     * @return
     */
    boolean isStringValid(String string, int lower, int upper) {
        return (string.length() > lower && string.length() <= upper);
    }


    /* Create a DatePicker Dialog */
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * set the date of starting as a string
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        datePicked = year + "-" + month + "-" +dayOfMonth;
        dateOfStarting.setText("Date Selected: " + datePicked);
    }
}