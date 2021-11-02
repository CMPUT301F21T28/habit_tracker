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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HabitListFragment extends Fragment {
    
    private static final String TAG = "MyActivity";

    RecyclerView habitList;
    HabitListAdapter recyclerAdapter;

    FirebaseFirestore db;

    String s1[] = {"Habit1", "habit2"};
    Integer int1[] = {1,2};

    String userName = null;
    String habitID = null;

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

        if (bundle.containsKey("habitID")) {
            habitID = bundle.getString("habitID");
        }
        /*
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Users").document(userName).collection("HabitList");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

            }
        });




        habitList = (RecyclerView) rootView.findViewById(R.id.habit_list);

        recyclerAdapter = new HabitListAdapter(getActivity(), s1, int1);
        habitList.setAdapter(recyclerAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(getActivity()));
        */
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*String deletedHabit = null;


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();


                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        Log.d(TAG, "onSwiped: Delete");
                        recyclerAdapter.notifyItemRemoved(position);
                        break;
                    case ItemTouchHelper.RIGHT:

                        break;

                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);

        habitList = (RecyclerView) getView().findViewById(R.id.habit_list);
        itemTouchHelper.attachToRecyclerView(habitList);

         */

        getView().findViewById(R.id.add_habit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_habitAddFragment);
            }
        });

        getView().findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_habitDetailFragment);
            }
        });
    }

}