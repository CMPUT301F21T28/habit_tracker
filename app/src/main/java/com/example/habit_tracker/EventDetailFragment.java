package com.example.habit_tracker;

import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import java.util.Base64;

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
    private Double locationLongitude;
    private Double locationLatitude;
    private Button edit;
    private ImageView imageView;
    private TextView viewLocation;
    private FloatingActionButton editButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        // maybe add validation check for bundle arguments? - darren
        username = bundle.getString("username");
        habitID = bundle.getString("habitID");
        event = bundle.getParcelable("Event");

        eventName= rootView.findViewById(R.id.textView_detail_eventName_view);
        commentContent = rootView.findViewById(R.id.nameContent);
        viewLocation = (TextView) rootView.findViewById(R.id.viewLocation);
        eventName.setText(event.getName());
        commentContent.setText(event.getComment());
        locationLongitude = event.getLocationLongitude();
        locationLatitude = event.getLocationLatitude();

        imageView = rootView.findViewById(R.id.imageView);

        String imageString = event.getEventImage();
        if (imageString != null) {
            Bitmap bitmap = stringToBitmap(imageString);
            imageView.setImageBitmap(bitmap);
        }

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

        if(locationLatitude != null && locationLongitude!= null){
            viewLocation.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",locationLongitude,locationLatitude );
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent); }
        });
        } else{
            viewLocation.setText("Location is optional... you didn't set one");
        }

        editButton = (FloatingActionButton) getView().findViewById(R.id.Edit);
        editButton.setOnClickListener(new View.OnClickListener() {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap stringToBitmap(String imageString){
        byte[] bitmapArray;
        bitmapArray = Base64.getDecoder().decode(imageString);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        //imageView.setImageBitmap(bitmap);
        return bitmap;
    }
}























