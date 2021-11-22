package com.example.habit_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;

public class EventAddFragment extends Fragment {

    private static final String TAG = "MyActivity";

    String username = null;
    String habitID = null;
    FusedLocationProviderClient client;


    public EventAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    /*public void onLocationChanged(Task<Location> location){

        LocationHelper helper = new LocationHelper(
                location.getResult().getLongitude();
                location.getResult().getLatitude();
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_add, container, false);

        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        habitID = bundle.getString("habitID");

        Log.d(TAG, "onCreateView: habit id" + habitID);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        EditText editTextEventName = view.findViewById(R.id.editTextName);
        EditText editTextEventCommit = view.findViewById(R.id.editTextComments);

        Button submitButton = view.findViewById(R.id.submitButton);
        Button locationButton = view.findViewById(R.id.locationButton);

        //String habit = getArguments().getString("habitId");//"0NyZLjRumQo45JOmXish";//getArguments().getString("habit");

        Image image = null;
        ImageView imageView;
        final File[] file = new File[1];
        final Double[] currentLongitude = {null};
        final Double[] currentLatitude = {null};


        /* TODO image adding
        ImageButton imageButton = getView().findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //file[0] = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+habit+".jpg")
                //Intent intent = new Intent(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file[0]));
                //startActivityForResult(intent,100);
            }
        });
        */

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("habit");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("habitID", habitID);

                String event_name = editTextEventName.getText().toString();
                String event_commit = editTextEventCommit.getText().toString();

                HashMap<String, Object> data = new HashMap<>();
                if (event_name.length() > 0 && event_commit.length() <= 20 ) {
                    data.put("event name", event_name);
                    data.put("event comment", event_commit);
                    data.put("Longitude", currentLongitude[0]);
                    data.put("Latitude", currentLatitude[0]);
                    //data.put("event image",image);
                    //System.currentTimeMillis() return long

                    Log.d(TAG, "onClick: " + habitID);
                    collectionReference.document(habitID).collection("EventList")
                            .document(event_name + String.valueOf(System.currentTimeMillis()))
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "submit success", Toast.LENGTH_SHORT).show();

                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_eventAddFragment_to_eventListFragment, bundle);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "submit fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Invalid information.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(view.getContext().getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    client = LocationServices.getFusedLocationProviderClient(view.getContext());
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>(){
                    @Override
                    public void onSuccess(Location location){
                        HashMap<String,Object> data = new HashMap<>();
                        if (location!= null){
                            currentLongitude[0] =location.getLongitude();
                            currentLatitude[0] = location.getLatitude();


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

