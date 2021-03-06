package com.example.habit_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;

/**
 * EventEditFragment creates a fragment for editing an event's details
 */

public class EventEditFragment extends Fragment {
    private FloatingActionButton submit;
    private EditText commentContent;
    private EditText nameContent;
    private ImageButton imageButton;
    private FirebaseFirestore db;
    private TextView hasLocation;
    private TextView noLocation;
    FusedLocationProviderClient client;
    Double currentLongitude = null;
    Double currentLatitude = null;

    private String username;
    private String habitID;
    private Event event;

    Bitmap originBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * inflate the app bar for me to edit
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tooltip_info, menu);
    }

    /**
     * Gives app bar a certain button and its functions
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tooltip_info_button:
                Toast.makeText(getContext(), "Photo is optional\nLong click your added photo to delete", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Create view for EventEditFragment, extract necessities (e.g. username, instance of Habit class) from the bundle
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

        // get things out of bundle
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        habitID = bundle.getString("habitID");
        event = bundle.getParcelable("Event");

        return rootView;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
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


        commentContent = (EditText)getView().findViewById(R.id.commentContent);
        submit= (FloatingActionButton) getView().findViewById(R.id.Submit);
        noLocation = view.findViewById(R.id.textViewNoLocation);
        hasLocation = view.findViewById(R.id.textViewHasLocation);
        imageButton = getView().findViewById(R.id.editImageButton);
        nameContent = getView().findViewById(R.id.nameContent);
        nameContent.setText(event.getName());
        commentContent.setText(event.getComment());

        // get the image
        originBitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
        String imageString = event.getEventImage();
        if (imageString != null) {
            Bitmap bitmap = stringToBitmap(imageString);
            imageButton.setImageBitmap(bitmap);
        }

        ActivityResultLauncher<Intent> activityResultLauncher;
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //
                        if (result.getData() != null) {
                            Bundle b = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) b.get("data");
                            imageButton.setImageBitmap(bitmap);
                        }
                    }
                });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);

            }
        });
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //return false;

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to delete this image?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // set image origin
                        imageButton.setImageBitmap(originBitmap);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                //imageButton.setImageBitmap(originBitmap);

                return true;
            }
        });

        // get the location
        if(event.getLocationLongitude() == null && event.getLocationLatitude() == null){
            hasLocation.setVisibility(View.GONE);
            noLocation.setVisibility(View.VISIBLE);
        }
        else{
            noLocation.setVisibility(View.GONE);
            hasLocation.setVisibility(View.VISIBLE);
        }

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
                if ( 20 <= nameContent.getText().toString().length()) {
                    isValid[0] = false;
                    nameContent.setError("name may not valid. Please ensure that it is between 0 and 20 characters.");
                    nameContent.requestFocus();
                    return;
                }

                HashMap<String, Object> data = new HashMap<>();

                // isValid doesnt actually do anything... please check this -- darren
                if (isValid[0] == true) {
                    data.put("event comment", commentContent.getText().toString());
                    data.put("Longitude", currentLongitude);
                    data.put("Latitude", currentLatitude);
                    //data.put("Location", locationContent.getText().toString());
                    data.put("event name",nameContent.getText().toString());

                    event.setEventComment(commentContent.getText().toString());
                    Bitmap imageBitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
                    String imageString = event.getEventImage();
                    if (imageBitmap == originBitmap){
                        imageString = null;
                    }else {
                        imageString = imageToString(imageBitmap);
                    }
                    event.setEventImage(imageString);
                    data.put("event image", imageString);

                    //data.put("event name", event.getName());
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


        hasLocation.setOnClickListener(new View.OnClickListener() {
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
                                hasLocation.setVisibility(View.GONE);
                                noLocation.setVisibility(View.VISIBLE);
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


        noLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(view.getContext().getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),"Add success",Toast.LENGTH_SHORT).show();
                    LocationRequest mLocationRequest = LocationRequest.create();
                    mLocationRequest.setInterval(60000);
                    mLocationRequest.setFastestInterval(5000);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationCallback mLocationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult == null) {
                                return;
                            }
                            for (Location location : locationResult.getLocations()) {
                                if (location != null) {
                                    //TODO: UI updates.
                                }
                            }
                        }
                    };
                    LocationServices.getFusedLocationProviderClient(view.getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                    client = LocationServices.getFusedLocationProviderClient(view.getContext());
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>(){
                        @Override
                        public void onSuccess(Location location){
                            HashMap<String,Object> data = new HashMap<>();
                            if (location!= null){
                                currentLongitude =location.getLongitude();
                                currentLatitude = location.getLatitude();
                                noLocation.setVisibility(View.GONE);
                                hasLocation.setVisibility(View.VISIBLE);
                            }

                        }

                    });}

                else{
                    ActivityCompat.requestPermissions((Activity) view.getContext(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44
                    );

                }
            }
        });
    }

    public Boolean goodString(String string){
        if (string.length() > 0 && string.length() < 20){
            return true;
        }else {
            return false;
        }

    }

    // class function to help with the image translation
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String imageToString(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String imageString;
        imageString = Base64.getEncoder().encodeToString(imageByte);
        return imageString;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap stringToBitmap(String imageString){
        byte[] bitmapArray;
        bitmapArray = Base64.getDecoder().decode(imageString);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        //imageView.setImageBitmap(bitmap);
        return bitmap;
    }
    boolean isStringValid(String string, int lower, int upper) {
        return (string.length() > lower && string.length() <= upper);
    }
}