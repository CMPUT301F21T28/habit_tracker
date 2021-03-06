package com.example.habit_tracker;

import static android.content.ContentValues.TAG;

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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * HabitAddFragment creates a fragment for adding a new habit
 */
public class HabitAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private FloatingActionButton submitButton;
    private RadioGroup radioGroup;
    private EditText habitTitle;
    private EditText habitReason;
    private TextView repeatDay;
    private TextView editTextDate;
    private TextView plan;

    private boolean[] selectedDay;
    private ArrayList<Integer> dayList = new ArrayList<>();
    private String[] dayArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String selectedDayString;

    private String username;
    private Boolean isPrivate;

    private Integer habitsSize;

    private String datePicked;

    private FirebaseFirestore db;

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
        // get the bundle passed from the previous fragment
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        if (bundle.containsKey("habitsSize")) {
            Log.d(TAG, "onCreateView: yes");
        }
        habitsSize = bundle.getInt("habitsSize");
        return rootView;

    }

    /**
     * Called after the view is created. User Interface fields (eg. edittext) are bound to their variables,
     * firestore db is initialised, and onClickListeners are set up for each button.
     *
     * submitButton - onclick listener is set up to add the data to the firebase
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        habitTitle = (EditText) getView().findViewById(R.id.editText_habitTitle);
        habitReason = (EditText) getView().findViewById(R.id.editText_habitReason);
        editTextDate = (TextView) getView().findViewById(R.id.textDatePicker);
        repeatDay = (TextView) getView().findViewById(R.id.textView_select_day);
        radioGroup = getView().findViewById(R.id.radioGroup);
        submitButton = (FloatingActionButton) getView().findViewById(R.id.submit_button);
        plan = (EditText) getView().findViewById(R.id.habit_times);

        //Initialize selected repeat day
        //https://stackoverflow.com/questions/10207206/how-to-display-alertdialog-in-a-fragment
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
                            //System.out.println("add:" + i);
                            //System.out.println(dayList);
                        }else {
                            //When checkbox is unselected, remove position from the list
                            //System.out.println("unselect:" + i);
                            dayList.remove(Integer.valueOf(i));
                            //System.out.println("Delete" + i);
                            //System.out.println(dayList);
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
                        }
                    }
                });
                //show dialog
                builder.show();

            }
        });

        //Create the radio button
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

        // Calling Date Picker (date of starting)
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        CollectionReference collectionReference = db.collection("Users").document(username).collection("HabitList");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                boolean inputValid = checkInputValidity(habitTitle,0,20) && checkInputValidity(habitReason,-1,30)
                        && selectedDayString != null && isPrivate != null;

                if (TextUtils.isEmpty(datePicked)) {
                    editTextDate.setError("Date cannot be empty.");
                    return;
                }

                if (selectedDayString == null){
                    repeatDay.setError("Please Select at least one day");
                    return;
                }

                if (isPrivate == null){
                    Toast.makeText(getActivity(), "Can your followers see this habit?\nplease choose one", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isTimesValid = true;
                Integer times = 0;
                if (plan.getText().toString().length() == 0) {
                    times = 28;
                } else {
                    times = Integer.parseInt(plan.getText().toString());
                    if (times > 1000 || times <= 0) {
                        isTimesValid = false;
                        plan.setError("Please enter an positive Integer less or equal to 1000 times");
                        return;
                    }
                }

                if (inputValid == false && !isTimesValid){
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> data = new HashMap<>();

                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString();

                if (inputValid && isTimesValid) {
                    data.put("title", habitTitle.getText().toString());
                    data.put("reason", habitReason.getText().toString());
                    data.put("repeat", selectedDayString);
                    data.put("dateOfStarting", datePicked);
                    data.put("isPrivate", isPrivate);
                    data.put("order", habitsSize+1);
                    data.put("plan", times);
                    data.put("finish", 0);
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

    /**
     *  Create a DatePicker Dialog
     */
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
        editTextDate.setText("Date Selected: " + datePicked);
    }
}