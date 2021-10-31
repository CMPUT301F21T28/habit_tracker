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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventButtomClickedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventButtomClickedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEventButtomClickedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventButtomClickedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventButtomClickedFragment newInstance(String param1, String param2) {
        AddEventButtomClickedFragment fragment = new AddEventButtomClickedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event_buttom_clicked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextEventName = view.findViewById(R.id.editTextName);
        EditText editTextEventCommit = view.findViewById(R.id.editTextComments);

        TextView test = getView().findViewById(R.id.textView5);
        String habit = getArguments().getString("habit");

        Image image = null;
        ImageView imageView;
        final File[] file = new File[1];

        ImageButton imageButton = getView().findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //file[0] = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+habit+".jpg")
                Intent intent = new Intent(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file[0]));
                //startActivityForResult(intent,100);
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("events");

        Button submitButtom = view.findViewById(R.id.submitButton);
        submitButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit something.
                String event_name = editTextEventName.getText().toString();
                String event_commit = editTextEventCommit.getText().toString();

                HashMap<String,Object> data = new HashMap<>();
                if (event_name.length()>0){
                    data.put("event name",event_name);
                    data.put("event Commit",event_commit);
                    data.put("event image",image);
                    collectionReference
                            .document(habit)
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
                }else{
                    Toast.makeText(getContext(),"need a event name",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}