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
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class EditHabitEventFragment extends Fragment {
    private Button submit;
    private Button cancel;
    private EditText commentContent;
    private EditText locationContent;
    private TextView comment;
    private TextView location;
    private FirebaseFirestore db;
    private String HabitID;

    private HabitEvent habitevent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_habit_event, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("HabitID")){
            HabitID = bundle.getString("HabitID", "0NyZLjRumQo45JOmXish");
            habitevent = bundle.getParcelable("EventList");
        }




        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        comment = (TextView) getView().findViewById(R.id.Comment);
        location = (TextView) getView().findViewById(R.id.Location);
        commentContent = (EditText)getView().findViewById(R.id.CommentContent);
        locationContent = (EditText)getView().findViewById(R.id.LocationContent);
        submit= (Button) getView().findViewById(R.id.Submit);
        cancel= (Button) getView().findViewById(R.id.Cancel);


        commentContent.setText(habitevent.getComment());
        locationContent.setText(habitevent.getLocation());


        CollectionReference collectionReference = db.collection("habit").document(HabitID).collection("EventList");

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final boolean[] isValid = {true};
                if ( 20 <= commentContent.getText().toString().length()) {
                    isValid[0] = false;
                    commentContent.setError("Comment may not valid. Please ensure that it is between 0 and 20 characters.");
                    return;
                }
                if (20 <= locationContent.getText().toString().length()) {
                    isValid[0] = false;
                    locationContent.setError("Location may not valid. Please ensure that it is between 0 and 20 characters.");
                    return;
                }

                HashMap<String, String> data = new HashMap<>();

                if (isValid[0] == true) {
                    data.put("Comment", commentContent.getText().toString());
                    data.put("Location", locationContent.getText().toString());


                    habitevent.setComment(commentContent.getText().toString());
                    habitevent.setLocation(locationContent.getText().toString());


                    collectionReference
                            .document("0NyZLjRumQo45JOmXish")
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("HabitID", "0NyZLjRumQo45JOmXish");
                                    bundle.putParcelable("EventList", habitevent);

                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_editHabitEventFragment_to_addHabbitEventFragment, bundle);
                                    //Toast.makeText(getContext(), "Success - Successfully added this habitevent to the database", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_editHabitEventFragment_to_addHabbitEventFragment);
                                    //Toast.makeText(getContext(), "Failure - Failed to insert into database.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_editHabitEventFragment_to_addHabbitEventFragment);
            }
        });

    }
}