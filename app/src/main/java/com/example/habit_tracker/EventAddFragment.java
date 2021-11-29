package com.example.habit_tracker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.RequiresApi;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Context;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;

public class EventAddFragment extends Fragment {

    private static final String TAG = "MyActivity";

    String username = null;
    String habitID = null;
    FusedLocationProviderClient client;
    Double currentLongitude = null;
    Double currentLatitude = null;

    Bitmap originBitmap;

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
        Button removeLocationButton = view.findViewById(R.id.removeLocationButton);
        removeLocationButton.setVisibility(View.GONE);

        //String habit = getArguments().getString("habitId");//"0NyZLjRumQo45JOmXish";//getArguments().getString("habit");

        Image image = null;
        ImageView imageView;
        final File[] file = new File[1];


        ImageButton imageButton = getView().findViewById(R.id.imageButton);
        ActivityResultLauncher<Intent> activityResultLauncher;
        Bitmap bit = null;
        //Boolean needImage;
        originBitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
        Toast.makeText(getContext(),"long click image to delete",Toast.LENGTH_SHORT).show();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //
                        if (result.getData() != null) {
                            Bundle b = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) b.get("data");
                            //bit = (Bitmap) b.get("data");
                            //imageBitmap = (Bitmap) b.get("data");
                            imageButton.setImageBitmap(bitmap);
                        }
                    }
                });


        //ImageButton imageButton = getView().findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //file[0] = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+habit+".jpg")
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//EXTRA_OUTPUT, Uri.fromFile(file[0]));
                activityResultLauncher.launch(intent);
                //getActivity().startActivityForResult(intent,100);
                /*
                ActivityResultLauncher<Image> imageResult = registerForActivityResult(new ActivityResultContracts.GetContent(),
                        new ActivityResultCallback<Uri>() {
                            @Override
                            public void onActivityResult(Uri result) {
                                //
                            }
                        })*/
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


        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);
        //Location loaction = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("habit");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("habitID", habitID);

                String event_name = editTextEventName.getText().toString();
                String event_commit = editTextEventCommit.getText().toString();

                Bitmap imageBitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
                String imageString;
                if (imageBitmap != originBitmap) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imageByte = byteArrayOutputStream.toByteArray();
                    //String imageString = imageByte.toString();
                    //imageString.
                    imageString = Base64.getEncoder().encodeToString(imageByte);
                }else {
                    imageString = null;
                }


                HashMap<String, Object> data = new HashMap<>();
                if (event_name.length() > 0 && event_commit.length() <= 20 ) {
                    data.put("event name", event_name);
                    data.put("event comment", event_commit);
                    data.put("Longitude", currentLongitude);
                    data.put("Latitude", currentLatitude);
                    data.put("event image",imageString);
                    //data.put("event image",image);
                    //System.currentTimeMillis() return long

                    Log.d(TAG, "onClick: " + habitID);
                    //String eventID = event_name + String.valueOf(System.currentTimeMillis());
                    String eventID = String.valueOf(System.currentTimeMillis()) + event_name;

                    collectionReference.document(habitID).collection("EventList")
                            .document(eventID)
                            //.document(event_name + String.valueOf(System.currentTimeMillis()))
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "submit success", Toast.LENGTH_SHORT).show();
                                    db.collection("Users").document(username).collection("HabitList").document(habitID).update("finish", FieldValue.increment(1));

                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", username);
                                    bundle.putString("habitID", habitID);

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
                    /*client.requestLocationUpdates(null, null);*/
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>(){
                    @Override
                    public void onSuccess(Location location){
                        if (location!= null) {
                            currentLongitude = location.getLongitude();
                            currentLatitude = location.getLatitude();
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


                        }}

                });}

                else{
                    ActivityCompat.requestPermissions((Activity) view.getContext(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44
                           );

                }
            }
        });

        removeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLongitude = null;
                currentLatitude = null ;
                removeLocationButton.setVisibility(View.GONE);
                locationButton.setVisibility(View.VISIBLE);

            }


        });

    }
}

