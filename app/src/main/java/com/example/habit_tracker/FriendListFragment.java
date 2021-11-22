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

import com.example.habit_tracker.adapters.FriendListAdapter;
import com.example.habit_tracker.adapters.FriendRequestAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendListFragment extends Fragment {

    RecyclerView friendList;
    FriendListAdapter friendRecyclerAdapter;
    ArrayList<Friend> friendDataList;

    RecyclerView requestList;
    FriendRequestAdapter requestRecyclerAdapter;
    ArrayList<Friend> requestDataList;

    FloatingActionButton add_friend;

    Friend deletedFriend;
    String username;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    private static final String TAG = "MyActivity";

    public FriendListFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_friend_list, container, false);

        db = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        friendDataList = new ArrayList<>();
        friendList = (RecyclerView) rootView.findViewById(R.id.recyclerView_friend);
        friendRecyclerAdapter = new FriendListAdapter(getActivity(), friendDataList);
        friendList.setAdapter(friendRecyclerAdapter);
        friendList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // ItemTouchHelper helps to define the swipe function
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(friendList);

        requestDataList = new ArrayList<>();
        requestList = (RecyclerView) rootView.findViewById(R.id.recyclerView_request);
        requestRecyclerAdapter = new FriendRequestAdapter(getActivity(), requestDataList, username);
        requestList.setAdapter(requestRecyclerAdapter);
        requestList.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFriendList(username);
            }
        });

        updateFriendList(username);

        add_friend = (FloatingActionButton) rootView.findViewById(R.id.add_friend_button);




        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO use 'addSnapshotListener' to pull data from db, use for loop to add data to habitDataList (an ArrayList<Habit>),
        //  can look up how I implement in HabitListFragment.java
        //  for both request and friend list !!! (TWO IN TOTAL, HAVE DIFFERENT ARRAY ADAPTER)

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);

                NavController controller = Navigation.findNavController(view);
                // TODO havent declare next fragment
                controller.navigate(R.id.action_eventListFragment_to_eventAddFragment,bundle);
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
                    deletedFriend = friendDataList.get(position);
                    // more information on array operations in firestore
                    // https://firebase.googleblog.com/2018/08/better-arrays-in-cloud-firestore.html
                    removeFriend(username, deletedFriend);

                    // Undo the deletion
                    Snackbar.make(friendList, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addFriend(username, deletedFriend);
                        }
                    }).show();
                    break;
            }
        }
    };

    public void removeRequest(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("requests", FieldValue.arrayRemove(targetFriend));
        updateFriendList(username);
    }

    public void addRequest(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("requests", FieldValue.arrayUnion(targetFriend));
        updateFriendList(username);
    }

    public void removeFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayRemove(targetFriend));
        updateFriendList(username);
    }

    public void addFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayUnion(targetFriend));
        updateFriendList(username);
    }

    // get friends and friend requests from the user that is passed in
    public void updateFriendList(String username) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    friendDataList.clear();
                    requestDataList.clear();
                    // Getting the array of friends
                    ArrayList<Map> friends = (ArrayList<Map>) documentSnapshot.get("friends");
                    for (Map<String, String> map : friends) {
                        Log.d("----- MAP VAL FRIENDS", map.toString());
                        String username = map.get("userName");
                        String realname = map.get("actualName");
                        friendDataList.add(new Friend(username, realname));
                        Log.d("Name", realname);
                        Log.d("Username", username);
                    }

                    // Getting the array of requests
                    ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("requests");
                    for (Map<String, String> map : requests) {
                        Log.d("------ MAP VAL REQUESTS", map.toString());
                        String username = map.get("userName");
                        String realname = map.get("actualName");
                        requestDataList.add(new Friend(username, realname));
                    }

                    // Update the adapaters
                    friendRecyclerAdapter.notifyDataSetChanged();
                    requestRecyclerAdapter.notifyDataSetChanged();
                }
        });
//        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        friendDataList.clear();
//                        requestDataList.clear();
//                        // Getting the array of friends
//                        ArrayList<Map> friends = (ArrayList<Map>) document.get("friends");
//                        for (Map<String, String> map : friends) {
//                            Log.d("----- MAP VAL FRIENDS", map.toString());
//                            String username = map.get("userName");
//                            String realname = map.get("actualName");
//                            friendDataList.add(new Friend(username, realname));
//                            Log.d("Name", realname);
//                            Log.d("Username", username);
//                        }
//
//                        // Getting the array of requests
//                        ArrayList<Map> requests = (ArrayList<Map>) document.get("requests");
//                        for (Map<String, String> map : requests) {
//                            Log.d("------ MAP VAL REQUESTS", map.toString());
//                            String username = map.get("userName");
//                            String realname = map.get("actualName");
//                            requestDataList.add(new Friend(username, realname));
//                        }
//
//                        // Update the adapaters
//                        friendRecyclerAdapter.notifyDataSetChanged();
//                        requestRecyclerAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
    }

}