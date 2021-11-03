package com.example.habit_tracker;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;

public class EventAddFragment extends Fragment {

    String username = null;
    Habit habit = null;
    String habitID = null;

    public EventAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_add, container, false);

        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        habit = bundle.getParcelable("habitID");
        habitID = habit.getHabitID();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextEventName = view.findViewById(R.id.editTextName);
        EditText editTextEventCommit = view.findViewById(R.id.editTextComments);

        Button submitButton = view.findViewById(R.id.submitButton);

        //String habit = getArguments().getString("habitId");//"0NyZLjRumQo45JOmXish";//getArguments().getString("habit");

        Image image = null;
        ImageView imageView;
        final File[] file = new File[1];


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
        final CollectionReference collectionReference = db.collection("habit").document(habitID).collection("EventList");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit something.
                String event_name = editTextEventName.getText().toString();
                String event_commit = editTextEventCommit.getText().toString();

                HashMap<String,Object> data = new HashMap<>();
                if (event_name.length()>0){
                    data.put("event name",event_name);
                    data.put("event Commit",event_commit);
                    //data.put("event image",image);
                    //System.currentTimeMillis() return long

                    collectionReference
                            .document(event_name + String.valueOf(System.currentTimeMillis()))
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(),"submit success",Toast.LENGTH_SHORT).show();
                                    //NavController controller = Navigation.findNavController(view);
                                    //controller.navigate(R.id.action_Add);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"submit fail",Toast.LENGTH_SHORT).show();
                                }
                            });
                            //.add(data);
                    NavController controller = Navigation.findNavController(view);
                    controller.navigate(R.id.action_addEventButtomClickedFragment_to_addHabbitEventFragment);
                }else{
                    Toast.makeText(getContext(),"need a event name",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}