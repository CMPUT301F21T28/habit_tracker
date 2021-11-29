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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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


public class EventEditFragment extends Fragment {
    private Button submit;
    private EditText commentContent;
    private EditText locationContent;
    private EditText nameContent;
    private ImageButton imageButton;
    private FirebaseFirestore db;
    FusedLocationProviderClient client;

    private String username;
    private String habitID;
    private Event event;

    Bitmap originBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final Double[] currentLongitude = {null};
        final Double[] currentLatitude = {null};

        commentContent = (EditText)getView().findViewById(R.id.commentContent);
        submit= (Button) getView().findViewById(R.id.Submit);
        Button locationButton = view.findViewById(R.id.locationButton);
        Button removeLocationButton = view.findViewById(R.id.removeLocationButton);
        imageButton = getView().findViewById(R.id.editImageButton);
        nameContent = getView().findViewById(R.id.nameContent);
        nameContent.setText(event.getName());

        originBitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
        String imageString = event.getEventImage();
        if (imageString != null) {
            Bitmap bitmap = stringToBitmap(imageString);
            imageButton.setImageBitmap(bitmap);
        }


        commentContent.setText(event.getComment());

        if(event.getLocationLongitude() == null && event.getLocationLatitude() == null){
            removeLocationButton.setVisibility(View.GONE);
            locationButton.setVisibility(View.VISIBLE);
        }
        else{
            locationButton.setVisibility(View.GONE);
            removeLocationButton.setVisibility(View.VISIBLE);
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
                    data.put("Longitude", currentLongitude[0]);
                    data.put("Latitude", currentLatitude[0]);
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
                        Manifest.permission.ACCESS_FINE_LOCATION)
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
}