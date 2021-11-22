package com.example.habit_tracker;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.CollationElementIterator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
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
 * create an instance of Add Habit Fragment.
 */
public class HabitAddFragment extends Fragment {

    private Button submitButton;
    private EditText habitTitle;
    private EditText habitReason;
    private EditText dateOfStarting;
//    private EditText repeat;
    private TextView repeatDay;
    private boolean[] selectedDay;
    private ArrayList<Integer> dayList = new ArrayList<>();
    private String[] dayArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String selectedDayString;

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
        // get the bundle passed from the previous fragment
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("username")){
            username = bundle.getString("username");
        }
        return rootView;

    }

    /**
     * function to check if the string is in the form of yyyy/mm/dd
     * @param date
     * @return a boolean, true if the string is in yyyy/mm/dd, return false otherwise
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
        dateOfStarting = (EditText) getView().findViewById(R.id.editText_dateOfStarting);
//        repeat = (EditText) getView().findViewById(R.id.editText_repeat);
        repeatDay = (TextView) getView().findViewById(R.id.textView_select_day);
        isPrivate = (EditText) getView().findViewById(R.id.editText_isPrivate);

        submitButton = (Button) getView().findViewById(R.id.submit_button);


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
                            System.out.println("add:" + i);
                            System.out.println(dayList);
                        }else {
                            //When checkbox is unselected, remove position from the list
                            System.out.println("unselect:" + i);
                            dayList.remove(Integer.valueOf(i));
                            System.out.println("Delete" + i);
                            System.out.println(dayList);
                        }

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
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



        CollectionReference collectionReference = db.collection("Users").document(username).collection("HabitList");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final boolean[] isValid = {true};

//                isValid[0] = isTitleValid(habitTitle.getText().toString());
//                isValid[1] = isReasonValid(habitReason.getText().toString());
//                isValid[2] = isDateValid(dateOfStarting.getText().toString());
//                isValid[3] = isRepeatValid(repeat.getText().toString());
//                isValid[4] = isPrivateValid(isPrivate.getText().toString());

                if (0 >= habitTitle.getText().toString().length() || 20 <= habitTitle.getText().toString().length()) {
                    isValid[0] = false;
                    habitTitle.setError("Habit name not valid. Please ensure that it is between 0 and 20 characters.");
                    return;
                }
                // if the 
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

                if (selectedDayString == null){
                    isValid[0] = false;
                    repeatDay.setError("Please Select at least one day");
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

                if (isValid[0]) {
                    data.put("title", habitTitle.getText().toString());
                    data.put("reason", habitReason.getText().toString());
                    data.put("repeat", selectedDayString);
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

    public Boolean isTitleValid(String string) {
        Boolean isValid = true;
        if (0 >= string.length() || 20 <= string.length()) {
            isValid = false;
            habitTitle.setError("Habit name not valid. Please ensure that it is between 0 and 20 characters.");
        }
        return isValid;
    }

//    public Boolean isReasonValid(String string) {
//        Boolean isValid = true;
//        if (0 >= string.length() || 30 <= string.length()) {
//            isValid = false;
//            habitReason.setError("Habit Reason is not valid. Please ensure that it is between 0 and 30 characters.");
//        }
//        return isValid;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public Boolean isDateValid(String string){
//        Boolean isValid = true;
//        if (string.length() >= 0){
//            isValid = checkDateValidity(string);
//            if (isValid == false){
//                dateOfStarting.setError("Invalid date format! Please enter date in yyyy/mm/dd.");
//            }
//        }
//        return isValid;
//    }
//
//    public Boolean isRepeatValid(String string) {
//        Boolean isValid = true;
//        if (0 >= string.length() || 30 <= string.length()) {
//            isValid = false;
//            repeat.setError("The reason is not valid. Please ensure that it is between 0 and 30 characters.");
//        }
//        return isValid;
//    }
//
//    public Boolean isPrivateValid(String string){
//        Boolean isValid = true;
//        if (string.toLowerCase().equals("yes")) {
//            isPrivateBoolean = true;
//        } else if (string.toLowerCase().equals("no")){
//            isPrivateBoolean = false;
//        }else {
//            isValid = false;
//            isPrivate.setError("Your input should be Yes or No.");
//        }
//        return isValid;
//    }
}