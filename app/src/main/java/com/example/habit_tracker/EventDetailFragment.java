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
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment {

    private String username;
    private String habitID;
    private Event event;
    private TextView eventName;
    private TextView commentContent;
    private TextView location;
    //private TextView locationContent;
    /*private TextView picture;
    private ImageView Image;*/
    private Button edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create view for EventDetailFragment
     * Extract necessities (e.g. username, instance of Habit class) from bundle, set TextViews to their corresponding values
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);

        Bundle bundle = this.getArguments();

        username = bundle.getString("username");
        habitID = bundle.getString("habitID");
        event = bundle.getParcelable("Event");

        eventName= rootView.findViewById(R.id.textView_detail_eventName_view);
        commentContent = rootView.findViewById(R.id.CommentContent);
        /* TODO image & location
        location= rootView.findViewById(R.id.Location);
        locationContent = rootView.findViewById(R.id.LocationContent);
        picture= rootView.findViewById(R.id.Picture);
        Image = rootView.findViewById(R.id.PictureContent);
        */

        eventName.setText(event.getName());
        commentContent.setText(event.getComment());
        //locationContent.setText(habitevent.getEventLocation());

        return rootView;

    }

    /**
     * Initialize all other parts that could cause the fragment status change
     * Fragment change by navigation
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit = (Button)getView().findViewById(R.id.Edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("habitID", habitID);
                bundle.putParcelable("Event", event);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_eventDetailFragment_to_eventEditFragment, bundle);
            }
        });
    }

}























