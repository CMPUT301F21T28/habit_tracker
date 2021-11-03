package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HabitListFragment extends Fragment {
    
    private static final String TAG = "MyActivity";

    RecyclerView habitList;
    HabitListAdapter recyclerAdapter;

    FirebaseFirestore db;

    ArrayList<Habit> habitDataList;

    String userName = null;

    Habit deletedHabit = null;

    CollectionReference collectionReference;

    public HabitListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit_list, container, false);


        Bundle bundle = this.getArguments();
        userName = bundle.getString("username");

        habitDataList = new ArrayList<>();
        habitList = (RecyclerView) rootView.findViewById(R.id.habit_list);
        recyclerAdapter = new HabitListAdapter(getActivity(), habitDataList);
        habitList.setAdapter(recyclerAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // initialize ItemTouchHelper for swipe & reorder function
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(habitList);

        // prompt user how to interact
        Toast.makeText(getActivity(), "Right Swipe to Delete, Left Swipe to See Events", Toast.LENGTH_SHORT).show();

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users").document("testuser").collection("HabitList");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String habitID = doc.getId();
                    String habitName = (String) doc.getData().get("title");
                    String habitDateOfStarting = (String) doc.getData().get("dateOfStarting");
                    String habitReason = (String) doc.getData().get("reason");
                    String habitRepeat = (String) doc.getData().get("repeat");
                    //Boolean habitIsPrivate = (Boolean) doc.getData().get("isPrivate");
                    Boolean habitIsPrivate = false;
                    habitDataList.add(new Habit(userName, habitName, habitID, habitDateOfStarting, habitReason, habitRepeat, false));
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        });


        // add a habit (go to new fragment)
        getView().findViewById(R.id.add_habit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_habitAddFragment);
            }
        });
    }

    // swipe to delete & drag to reorder the list
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // TODO drage to reorder the list (possible solution: put arrayList in mainActivity
            /*
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(habitDataList, fromPosition, toPosition);
            habitList.getAdapter().notifyItemMoved(fromPosition, toPosition);

             */
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.RIGHT:

                    deletedHabit = habitDataList.get(position);
                    collectionReference.document(habitDataList.get(position).getHabitID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });

                    Snackbar.make(habitList, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //habitDataList.add(position, deletedHabit);
                            HashMap<String, String> data = new HashMap<>();
                            data.put("title", deletedHabit.getUserName());
                            data.put("reason", deletedHabit.getReason());
                            data.put("repeat", deletedHabit.getRepeat());
                            data.put("dateOfStarting", deletedHabit.getDateOfStarting());
                            data.put("isPrivate", Boolean.toString(deletedHabit.getIsPrivate()));
                            collectionReference.document(deletedHabit.getHabitID()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Restored", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).show();
                    break;
                case ItemTouchHelper.LEFT:

                    // TODO link to the event fragment

                    break;
            }
        }
    };

}