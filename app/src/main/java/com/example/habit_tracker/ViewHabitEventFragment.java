package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

    private HabitEvent habitevent;
    private TextView comment;
    private TextView commentContent;
    private TextView location;
    private TextView locationContent;
    /*private TextView picture;
    private ImageView Image;*/
    private Button edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_habit_event, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("HabitID")){
            habitevent = bundle.getParcelable("EventList");
        }

        comment= rootView.findViewById(R.id.Comment);
        commentContent = rootView.findViewById(R.id.CommentContent);
        location= rootView.findViewById(R.id.Location);
        locationContent = rootView.findViewById(R.id.LocationContent);
        /*picture= rootView.findViewById(R.id.Picture);
        Image = rootView.findViewById(R.id.PictureContent);*/

        commentContent.setText(habitevent.getComment());
        locationContent.setText(habitevent.getLocation());

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        edit = (Button)getView().findViewById(R.id.Edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putString("HabitID", "0NyZLjRumQo45JOmXish" );
                bundle.putParcelable("EventList", habitevent);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_viewHabitEventFragment_to_editHabitEventFragment, bundle);
            }
        });
    }

}























