package com.example.habit_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.habit_tracker.viewholders.TextViewHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment {

    private static final String TAG = "MyActivity";

    String username = null;
    Habit habit = null;
    Event event = null;
    String habitID = null;
    String eventID = null;

    RecyclerView eventList;
    ArrayList<Event> eventDataList;
    GenericAdapter<Event> eventAdapter;

    FirebaseFirestore db;
    CollectionReference collectionReference;

    Event deletedEvent;

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create view for HabitAddFragment, extract necessities (e.g. username) from bundle
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_event_list, container, false);

        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        if (bundle.containsKey("Habit")) {
            habit = bundle.getParcelable("Habit");
            habitID = habit.getHabitID();
        } else {
            habitID = bundle.getString("habitID");
        }

        eventDataList = new ArrayList<>();
        eventList = (RecyclerView) rootView.findViewById(R.id.event_list);

        eventAdapter = new GenericAdapter<Event>(getActivity(), eventDataList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.general_list_row, parent, false));
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Event val) {
                ((TextViewHolder) holder).getTextView().setText(val.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("habitID", val.getHabitID());
                        bundle.putParcelable("Event", val);

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_eventListFragment_to_eventDetailFragment, bundle);
                    }
                });
            }
        };

        eventList.setAdapter(eventAdapter);
        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // initialize ItemTouchHelper for swipe function
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(eventList);

        return rootView;
    }

    /**
     * Initialize all other parts that could cause the fragment status change
     * Connect to firebase DB, retrieve habits fields to local and store in Habit instance and pass to recyclerView
     * Fragment change by navigation
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //connecting to firebase
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("habit").document(habitID).collection("EventList");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                eventDataList.clear();
                for (QueryDocumentSnapshot doc: value){
                    String eventID = doc.getId();
                    String eventName = (String) doc.getData().get("event name");
                    String eventComment = (String) doc.getData().get("event comment");
                    String eventImage = (String) doc.getData().get("event image");
                    Double longitude = (Double) doc.getData().get("Longitude");
                    Double latitude = (Double) doc.getData().get("Latitude");
                    // TODO image & location
                    eventDataList.add(new Event(username, habitID, eventID, eventName, eventComment, longitude, latitude, eventImage));
                }
                eventAdapter.notifyDataSetChanged();
            }
        });



        //press add button to add
        FloatingActionButton add_event = getView().findViewById(R.id.floatingActionButtonAdd);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("habitID", habitID);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_eventListFragment_to_eventAddFragment,bundle);
            }
        });

        // For tooltip button
        getView().findViewById(R.id.floatingActionButton_tooltip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Swipe Right to Delete\nTap to View Details", Toast.LENGTH_LONG).show();
            }
        });

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.RIGHT:

                    // create a dialog to confirm delete of the habit
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure to delete this event?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Delete the event
                            deletedEvent = eventDataList.get(position);
                            collectionReference.document(deletedEvent.getEventID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    db.collection("Users").document(username).collection("HabitList").document(habitID).update("finish", FieldValue.increment(-1));
                                }
                            });
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            eventAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
            }
        }
    };
}

