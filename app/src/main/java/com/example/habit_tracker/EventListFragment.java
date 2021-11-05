package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListFragment extends Fragment {

    private static final String TAG = "MyActivity";

    String username = null;
    Habit habit = null;
    Event event = null;
    String habitID = null;
    String eventID = null;

    RecyclerView eventList;
    EventListAdapter recyclerAdapter;

    ArrayList<Event> eventDataList;

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
        recyclerAdapter = new EventListAdapter(getActivity(), eventDataList);
        eventList.setAdapter(recyclerAdapter);
        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // initialize ItemTouchHelper for swipe function
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(eventList);

        // prompt user how to interact
        Toast.makeText(getActivity(), "Right Swipe to Delete", Toast.LENGTH_SHORT).show();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        //implement my thing here
        event_list.add("event1");
        //event_list.add("evwnt2");
        ListView listView = view.findViewById(R.id.list_view);
        ArrayAdapter listViewAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,event_list);
        listView.setAdapter(listViewAdapter);
        */

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
                    // TODO image & location
                    eventDataList.add(new Event(username, habitID, eventID, eventName, eventComment));
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        /*
        //long click list item to delete. return true will not triger clickListener
        ArrayList<String> id_list = new ArrayList<>();
        db.collection("habit").document(testHabitId).collection("EventList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            id_list.add(document.getId());
                            //Log.d("data from fire", document.getId() + " => " + document.getData());
                        }

                    }
                });
        */
        /*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(),id_list.get(i),Toast.LENGTH_SHORT).show();
                //parameter i should be the position of long click happened.
                db.collection("habit").document(testHabitId)
                        .collection("EventList")
                        .document(id_list.get(i))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),id_list.get(i)+" deleted",Toast.LENGTH_SHORT).show();
                                listViewAdapter.notifyDataSetChanged();
                            }
                        });
                /*
                db.collection("habit").document(testHabitId).collection("EventList")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("data from fire", document.getId() + " => " + document.getData());
                                }

                            }
                        });
                return true;
                //return false;
            }
        });

        */

        //press add button to add
        FloatingActionButton add_event = getView().findViewById(R.id.floatingActionButtonAdd);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("habitID", habitID);

                Log.d(TAG, "onClick: habitID" + habitID);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_eventListFragment_to_eventAddFragment,bundle);
            }
        });

//        FloatingActionButton back = getView().findViewById(R.id.floatingActionButtonBack);
//        add_event.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //move to habit list fragment
//                Log.d(TAG, "onClick: wrong button");
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//
//                NavController controller = Navigation.findNavController(view);
//                controller.navigate(R.id.action_eventListFragment_to_habitListFragment,bundle);
//            }
//        });

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Bundle bundle = new Bundle();

                bundle.putString("HabitID", "0NyZLjRumQo45JOmXish" );
                bundle.putParcelable("EventList", habitevent);
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_addHabbitEventFragment_to_viewHabitEventFragment);


                    }
        });
        */


        /*listView.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_addHabbitEventFragment_to_viewHabitEventFragment);
            }
        });*/

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
                    deletedEvent = eventDataList.get(position);
                    collectionReference.document(deletedEvent.getEventID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });

                    Snackbar.make(eventList, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("event name", deletedEvent.getName());
                            data.put("event comment", deletedEvent.getComment());
                            collectionReference.document(deletedEvent.getEventID()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Restored", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).show();
                    break;
            }
        }
    };
}

