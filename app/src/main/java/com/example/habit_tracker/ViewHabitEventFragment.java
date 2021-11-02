package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ViewHabitEventFragment extends Fragment {

    private TextView comment;
    private TextView commentContent;
    private TextView location;
    private TextView locationContent;
    private TextView picture;
    private ImageView Image;
    private Button edit;
    FirebaseFirestore db;

    public ViewHabitEventFragment() {
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
        return inflater.inflate(R.layout.fragment_view_habit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

       comment= (TextView)getView().findViewById(R.id.Comment);
       commentContent = (TextView)getView().findViewById(R.id.CommentContent);
       location= (TextView)getView().findViewById(R.id.Location);
       locationContent = (TextView)getView().findViewById(R.id.LocationContent);
       picture= (TextView)getView().findViewById(R.id.Picture);
       Image = (ImageView) getView().findViewById(R.id.PictureContent);
       edit = (Button)getView().findViewById(R.id.Edit);

    }
}