package com.example.habit_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.habit_tracker.adapters.HabitListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HabitListFragment extends Fragment {
    Switch todayHabitSwitch;

    RecyclerView habitList;
    HabitListAdapter recyclerAdapter;
    HabitListAdapter todayRecyclerAdapter;

    FirebaseFirestore db;

    ArrayList<Habit> habitDataList;
    ArrayList<Habit> todayHabitDataList = new ArrayList<Habit>();

    String username = null;
    String realname = null;

    CollectionReference collectionReference;



    public HabitListFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_habit_list, container, false);


        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        realname = bundle.getString("realname");

        habitDataList = new ArrayList<>();
        habitList = (RecyclerView) rootView.findViewById(R.id.habit_list);
        recyclerAdapter = new HabitListAdapter(getActivity(), habitDataList);
        habitList.setAdapter(recyclerAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(getActivity()));

        todayRecyclerAdapter = new HabitListAdapter(getActivity(), todayHabitDataList);

        // initialize ItemTouchHelper for swipe & reorder function
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(habitList);

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

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users").document(username).collection("HabitList");
        collectionReference.orderBy("order").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String habitID = doc.getId();
                    String habitName = (String) doc.getData().get("title");
                    String habitDateOfStarting = (String) doc.getData().get("dateOfStarting");
                    String habitReason = (String) doc.getData().get("reason");
                    String habitRepeat = (String) doc.getData().get("repeat");
                    Boolean habitIsPrivate = (Boolean) doc.getData().get("isPrivate");
                    Integer habitOrder = Integer.parseInt(String.valueOf(doc.getData().get("order")));
                    Integer habitPlan = Integer.parseInt(String.valueOf(doc.getData().get("plan")));
                    Integer habitFinish = Integer.parseInt(String.valueOf(doc.getData().get("finish")));
                    habitDataList.add(new Habit(username, habitName, habitID, habitDateOfStarting, habitReason, habitRepeat, habitIsPrivate, habitOrder, habitPlan, habitFinish));
                }
                recyclerAdapter.notifyDataSetChanged();

                todayHabitDataList.clear();
                for (Habit habit: habitDataList){
                    String repeatString = habit.getRepeat();
                    LocalDate date = LocalDate.now();
                    DayOfWeek dow = date.getDayOfWeek();
                    String dayName = dow.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
                    if (repeatString.contains(dayName)) {
                        todayHabitDataList.add(habit);
                    }
                }
            }
        });

        //Add filter to only show today's list.
        todayHabitSwitch = getView().findViewById(R.id.today_habit_switch);
        todayHabitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habitList.setAdapter(todayRecyclerAdapter);
                    todayRecyclerAdapter.notifyDataSetChanged();
                    //System.out.println(todayHabitDataList);
                }else {
                    habitList.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });


        // add a habit (go to new fragment)
        getView().findViewById(R.id.add_habit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putInt("habitsSize", habitDataList.size());

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_habitAddFragment, bundle);
            }
        });

        getView().findViewById(R.id.friend_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("realname", realname);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_friendListFragment, bundle);
            }
        });

        // For tooltip

    }

    // swipe to delete & drag to reorder the list
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // TODO drag to reorder the list (possible solution: put arrayList in mainActivity)

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(habitDataList, fromPosition, toPosition);
            habitList.getAdapter().notifyItemMoved(fromPosition, toPosition);

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition+1; i++) {
                    collectionReference.document(habitDataList.get(i).getHabitID()).update("order", i+1);
                }
            } else {
                for (int i = toPosition; i < fromPosition+1; i++) {
                    collectionReference.document(habitDataList.get(i).getHabitID()).update("order", i+1);
                }
            }

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
                    builder.setMessage("Are you sure to delete this habit?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Delete the habit
                            collectionReference.document(habitDataList.get(position).getHabitID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });
                            for (int i = position+1; i < habitDataList.size(); i++) {
                                collectionReference.document(habitDataList.get(i).getHabitID()).update("order", i);
                            }
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
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