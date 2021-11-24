package com.example.habit_tracker;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class EventEditFragment extends Fragment {
    private Button submit;
    private EditText commentContent;
    private EditText locationContent;
    private FirebaseFirestore db;
    FusedLocationProviderClient client;

    private String username;
    private String habitID;
    private Event event;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create view for EventAddFragment, extract necessities (e.g. username, instance of Habit class) from the bundle
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_edit, container, false);

        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        habitID = bundle.getString("habitID");
        event = bundle.getParcelable("Event");

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
        final Double[] currentLongitude = {null};
        final Double[] currentLatitude = {null};

        commentContent = (EditText)getView().findViewById(R.id.CommentContent);
        //locationContent = (EditText)getView().findViewById(R.id.LocationContent);
        submit= (Button) getView().findViewById(R.id.Submit);
        Button locationButton = view.findViewById(R.id.locationButton);
        Button removeLocationButton = view.findViewById(R.id.removeLocationButton);
        locationButton.setVisibility(View.GONE);


        commentContent.setText(event.getComment());
        //locationContent.setText(habitevent.getEventLocation());


        CollectionReference collectionReference = db.collection("habit").document(habitID).collection("EventList");

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final boolean[] isValid = {true};
                if ( 20 <= commentContent.getText().toString().length()) {
                    isValid[0] = false;
                    commentContent.setError("Comment may not valid. Please ensure that it is between 0 and 20 characters.");
                    commentContent.requestFocus();
                    return;
                }
//                TODO location & image
//                if (20 <= locationContent.getText().toString().length()) {
//                    isValid[0] = false;
//                    locationContent.setError("Location may not valid. Please ensure that it is between 0 and 20 characters.");
//                    return;
//                }

                HashMap<String, Object> data = new HashMap<>();

                // isValid doesnt actually do anything... please check this -- darren
                if (isValid[0] == true) {
                    data.put("event comment", commentContent.getText().toString());
                    data.put("Longitude", currentLongitude[0]);
                    data.put("Latitude", currentLatitude[0]);
                    //data.put("Location", locationContent.getText().toString());

                    event.setEventComment(commentContent.getText().toString());
                    //habitevent.setEventLocation(locationContent.getText().toString());

                    data.put("event name", event.getName());
                    collectionReference
                            .document(event.getEventID())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", username);
                                    bundle.putString("habitID", habitID);

                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_eventEditFragment_to_eventListFragment, bundle);
                                    //Toast.makeText(getContext(), "Success - Successfully added this habitevent to the database", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failure - Failed to insert into database.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });


        removeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("Longitude", FieldValue.delete());
                data.put("Latitude", FieldValue.delete());
                collectionReference
                        .document(event.getEventID())
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                removeLocationButton.setVisibility(View.GONE);
                                locationButton.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "Success - Successfully delete it.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failure - Failed to delete it .", Toast.LENGTH_LONG).show();
                            }
                        });}

            });


        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(view.getContext().getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),"Add success",Toast.LENGTH_SHORT).show();
                    client = LocationServices.getFusedLocationProviderClient(view.getContext());
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>(){
                        @Override
                        public void onSuccess(Location location){
                            HashMap<String,Object> data = new HashMap<>();
                            if (location!= null){
                                currentLongitude[0] =location.getLongitude();
                                currentLatitude[0] = location.getLatitude();
                                locationButton.setVisibility(View.GONE);
                                removeLocationButton.setVisibility(View.VISIBLE);




                            /*data.put("Longitude", location.getLongitude());
                            data.put("Latitude", location.getLatitude());
                            Log.d(TAG, "onClick: " + habitID);
                            collectionReference.document(habitID).collection("EventList")
                                    .document(event_name + String.valueOf(System.currentTimeMillis()))
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "submit success", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "submit fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });*/
                            }

                        }

                    });}

                else{
                    ActivityCompat.requestPermissions((Activity) view.getContext(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44
                    );

                }
            }
        });

    }
}