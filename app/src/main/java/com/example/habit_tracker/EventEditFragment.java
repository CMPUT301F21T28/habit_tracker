package com.example.habit_tracker;

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
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;


public class EventEditFragment extends Fragment {
    private Button submit;
    private EditText commentContent;
    private EditText locationContent;
    private ImageButton imageButton;
    private FirebaseFirestore db;

    private String username;
    private String habitID;
    private Event event;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        commentContent = (EditText)getView().findViewById(R.id.CommentContent);
        //locationContent = (EditText)getView().findViewById(R.id.LocationContent);
        submit= (Button) getView().findViewById(R.id.Submit);
        imageButton = getView().findViewById(R.id.editImageButton);
        String imageString = event.getEventImage();
        byte[] bitmapArray;
        bitmapArray = Base64.getDecoder().decode(imageString);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length);
        imageButton.setImageBitmap(bitmap);


        commentContent.setText(event.getEventComment());

        ActivityResultLauncher<Intent> activityResultLauncher;
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //
                        Bundle b = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) b.get("data");
                        //bit = (Bitmap) b.get("data");
                        //imageBitmap = (Bitmap) b.get("data");
                        imageButton.setImageBitmap(bitmap);
                    }
                });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //file[0] = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+habit+".jpg")
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//EXTRA_OUTPUT, Uri.fromFile(file[0]));
                activityResultLauncher.launch(intent);

            }
        });

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
                    return;
                }
//                TODO location
//                if (20 <= locationContent.getText().toString().length()) {
//                    isValid[0] = false;
//                    locationContent.setError("Location may not valid. Please ensure that it is between 0 and 20 characters.");
//                    return;
//                }

                HashMap<String, String> data = new HashMap<>();

                if (isValid[0] == true) {
                    data.put("event comment", commentContent.getText().toString());
                    //data.put("Location", locationContent.getText().toString());


                    event.setEventComment(commentContent.getText().toString());
                    //habitevent.setEventLocation(locationContent.getText().toString());
                    Bitmap imageBitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] imageByte = byteArrayOutputStream.toByteArray();
                    String imageString = Base64.getEncoder().encodeToString(imageByte);
                    event.setEventImage(imageString);
                    data.put("event image", imageString);

                    data.put("event name", event.getEventName());
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

    }
}