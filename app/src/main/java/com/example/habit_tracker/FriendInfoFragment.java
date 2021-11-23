package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.habit_tracker.adapters.HabitListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendInfoFragment extends Fragment {

    RecyclerView habitList;
    HabitListAdapter habitListAdapter;
    ArrayList<Habit> habitDataList;
    Friend friend;
    TextView tvUsername;
    Button unfollowBtn;
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

        // TODO expecting (current user username, friend type object) from previous fragment
        Bundle bundle  = this.getArguments();
        currentUser = bundle.getString("username");
        friend = bundle.getParcelable("friend");

        // setting the friend username and realname
        tvUsername = rootView.findViewById(R.id.textView_username);
        tvUsername.setText(friend.getUserName().toString());

        habitDataList = new ArrayList<>();
        habitList = (RecyclerView) rootView.findViewById(R.id.recyclerView_friendInfo);
        habitListAdapter = new HabitListAdapter(getActivity(), habitDataList);
        habitList.setAdapter(habitListAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // unfollowBtn.findViewById(R.id.unfollow_button);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set onClickListener for unfollowBtn
        unfollowBtn = getView().findViewById(R.id.unfollow_button);

        unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove them from the follow list
                firebaseUtils.removeFriend(currentUser, friend);

                // give them undo option
                Snackbar.make(habitList, "Unfollowed " + friend.getUserName(), Snackbar.LENGTH_INDEFINITE).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseUtils.addFriend(currentUser, friend);
                    }
                }).show();

                // return to friendlist
                Bundle bundle = new Bundle();
                bundle.putString("username", currentUser);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_friendInfoFragment_to_friendListFragment, bundle);
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


                    // TODO: change isPrivate field of a habit to be a boolean. in the meantime use this:
                    String habitIsPrivate = (String) doc.getData().get("isPrivate");
                    if (habitIsPrivate.equals("false")) {
                        habitDataList.add(new Habit(friend.getUserName(), habitName, habitID, habitDateOfStarting, habitReason, habitRepeat, false, habitOrder));
                    }

//                    if (!habitIsPrivate) {
//                        habitDataList.add(new Habit(friend.getUserName(), habitName, habitID, habitDateOfStarting, habitReason, habitRepeat, false, habitOrder));
//                    }
                }
                habitListAdapter.notifyDataSetChanged();
            }
        });

    }
}