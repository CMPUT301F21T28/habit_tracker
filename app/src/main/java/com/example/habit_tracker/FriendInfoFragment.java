package com.example.habit_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habit_tracker.viewholders.TextProgressViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * FriendInfo create a fragment to show details of a friend, including his/her public habit
 */
public class FriendInfoFragment extends Fragment {

    RecyclerView habitList;
    GenericAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    Friend friend;

    TextView tvUsername;
    Button unfollowBtn;
    String currentReal;
    String currentUser;
    Utility firebaseUtils = new Utility();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    // TODO initialize collectionReference

    public FriendInfoFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_friend_info, container, false);

        Bundle bundle  = this.getArguments();
        currentUser = bundle.getString("username");
        currentReal = bundle.getString("realname");
        friend = bundle.getParcelable("friend");

        // setting the friend username and realname
        tvUsername = rootView.findViewById(R.id.textView_username);
        tvUsername.setText(friend.getActualName() + " (" + friend.getUserName() + ")");

        // create the recyclerview for the habitDataList, with my generic adapter
        habitDataList = new ArrayList<>();
        habitList = (RecyclerView) rootView.findViewById(R.id.recyclerView_friendInfo);
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
            }
        };
        habitList.setAdapter(habitAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(getActivity()));

        habitList.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
        // unfollowBtn.findViewById(R.id.unfollow_button);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO Generic adapter: setOnClick to be disabled!!

        // set onClickListener for unfollowBtn
        unfollowBtn = getView().findViewById(R.id.unfollow_button);

        unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create window asking to confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm Unfriend");
                builder.setMessage("Are you sure that you want to unfriend " + friend.getUserName() + "?\nThey're going to miss you!");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // remove them from the follow list
                        firebaseUtils.removeFriend(currentUser, friend);

                        // give them undo option
                        Snackbar.make(habitList, "Unfriended " + friend.getUserName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                firebaseUtils.addFriend(currentUser, friend);
                            }
                        }).show();

                        // return to friendlist
                        Bundle bundle = new Bundle();
                        bundle.putString("username", currentUser);
                        bundle.putString("realname", currentReal);

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_friendInfoFragment_to_friendListFragment, bundle);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        //  use 'addSnapshotListener' to pull data from db, use for loop to add data to habitDataList (an ArrayList<Habit>),
        //  can look up how I implement in HabitListFragment.java
        collectionReference = db.collection("Users").document(friend.getUserName()).collection("HabitList");
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
                    Integer habitOrder = Integer.parseInt(String.valueOf(doc.getData().get("order")));
                    Boolean habitIsPrivate = (Boolean) doc.getData().get("isPrivate");
                    Integer habitPlan = Integer.parseInt(String.valueOf(doc.getData().get("plan")));
                    Integer habitFinish = Integer.parseInt(String.valueOf(doc.getData().get("finish")));
                    if (habitIsPrivate == false) {
                        habitDataList.add(new Habit(friend.getUserName(), habitName, habitID, habitDateOfStarting, habitReason, habitRepeat, false, habitOrder, habitPlan, habitFinish));
                    }
                }
                habitAdapter.notifyDataSetChanged();
            }
        });

    }
}