package com.example.habit_tracker;

import static android.content.ContentValues.TAG;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habit_tracker.viewholders.TextProgressViewHolder;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
 * HabitListFragment creates a fragment with a recyclerView to view all the habit's titles with their progress
 */
public class HabitListFragment extends Fragment {

    FirebaseFirestore db;
    CollectionReference collectionReference;
    CollectionReference collectionReferenceEvent;

    RecyclerView habitList;
    ArrayList<Habit> habitDataList;
    GenericAdapter<Habit> habitAdapter;
    ArrayList<Habit> todayHabitDataList = new ArrayList<Habit>();
    GenericAdapter<Habit> todayRecyclerAdapter;

    String username = null;

    public HabitListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * declare that the app bar is going to be modified
     * Adding a switch that shows today event only
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tooltip_info_switch, menu);

        MenuItem todaySwitch = menu.findItem(R.id.toggle);
        Switch todayHabitSwitch = (Switch) todaySwitch.getActionView();
        todayHabitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    habitList.setAdapter(todayRecyclerAdapter);
                    todayRecyclerAdapter.notifyDataSetChanged();
                    //System.out.println(todayHabitDataList);
                }else {
                    habitList.setAdapter(habitAdapter);
                    habitAdapter.notifyDataSetChanged();
                }
            }
        });

        MenuItem todayText = menu.findItem(R.id.today);
        TextView todayTextView = (TextView) todayText.getActionView();
        todayTextView.setText("Today ");
        todayTextView.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Filling out other app bar's function, which is a tooltip
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tooltip_info_button:
                Toast.makeText(getContext(), "Swipe Right to Delete\nShort Tap to View Details\nTap Progress to View Events\nLong Press to Create a New Event", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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

        // inflate the recyclerView for habit with generic adapter
        habitDataList = new ArrayList<>();
        habitList = (RecyclerView) rootView.findViewById(R.id.habit_list);
        habitAdapter = new GenericAdapter<Habit>(getActivity(), habitDataList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                return new TextProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_row, parent, false));
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Habit val) {
                ((TextProgressViewHolder) holder).getTextView().setText(val.getName());
                ((TextProgressViewHolder) holder).getProgressButton().setText(Math.round(val.getProgress()) + "%");
                ((TextProgressViewHolder) holder).getProgressBar().setProgress(Math.round(val.getProgress()));
                ((TextProgressViewHolder) holder).getProgressButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putParcelable("Habit", val);

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_habitListFragment_to_eventListFragment, bundle);
                    }
                });
                ((TextProgressViewHolder) holder).getProgressButton().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("habitID", val.getHabitID());
                        bundle.putString("from_habitListFragment", "From habit list!");

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_habitListFragment_to_eventAddFragment, bundle);
                        return false;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putParcelable("Habit", val);

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_habitListFragment_to_habitDetailFragment, bundle);
                    }
                });
            }
        };

        habitList.setAdapter(habitAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // today recyclerAdapter is for the habit that happens today
        todayRecyclerAdapter = new GenericAdapter<Habit>(getActivity(), todayHabitDataList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                return new TextProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_row, parent, false));
            }
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Habit val) {
                ((TextProgressViewHolder) holder).getTextView().setText(val.getName());
                ((TextProgressViewHolder) holder).getProgressButton().setText(Math.round(val.getProgress()) + "%");
                ((TextProgressViewHolder) holder).getProgressBar().setProgress(Math.round(val.getProgress()));
                ((TextProgressViewHolder) holder).getProgressButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putParcelable("Habit", val);

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_habitListFragment_to_habitDetailFragment, bundle);
                    }
                });
                ((TextProgressViewHolder) holder).getProgressButton().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putParcelable("Habit", val);
                        bundle.putString("habitID", val.getHabitID());

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_habitListFragment_to_eventListFragment, bundle);
                        return false;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putParcelable("Habit", val);

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_habitListFragment_to_habitDetailFragment, bundle);
                    }
                });
            }
        };

        habitList.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

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

        // retrieve the info from db and give to the class instances, to create an array list
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
                habitAdapter.notifyDataSetChanged();

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

        //Navigate to friend page if the friend button is clicked
        // go to friend list
        getView().findViewById(R.id.friend_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Query Realname
                DocumentReference usersRef = db.collection("Users").document(username);
                Log.d("INSIDE HABITLIST", username);
                usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                String real = documentSnapshot.getString("realname");
                                Log.d("INSIDE HABITLIST", real);
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                bundle.putString("realname", real);

                                NavController controller = Navigation.findNavController(view);
                                controller.navigate(R.id.action_habitListFragment_to_friendListFragment, bundle);
                            }
                        }
                        else {
                            Log.d("----- MAINPAGE", "FAILED");
                        }
                    }
                });
            }
        });
    }

    // swipe to delete & drag to reorder the list
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

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

        /**
         * swipe right to delete, a dialog will pop up to ask the user to confirm
         * @param viewHolder
         * @param direction
         */
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
                            String selectedHabitID = habitDataList.get(position).getHabitID();
                            //delete habit events of this habit
                            collectionReferenceEvent = db.collection("habit").document(selectedHabitID).collection("EventList");
                            collectionReferenceEvent
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Log.d("Success", "To get " + document.toObject(String.class));
                                                    collectionReferenceEvent.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d("TAG", "Deleted " + document.getId() + " => " + document.getData());
                                                        }
                                                    });

                                                }
                                            } else {
                                                Log.d("TAG", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                            // Delete the habit
                            collectionReference.document(selectedHabitID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            habitAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
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