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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.habit_tracker.adapters.FriendListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendListFragment extends Fragment {

    RecyclerView friendList;
    FriendListAdapter friendRecyclerAdapter;
    ArrayList<Friend> friendDataList;

    RecyclerView requestList;
    FriendListAdapter requestRecyclerAdapter;
    ArrayList<Friend> requestDataList;

    FloatingActionButton add_friend;

    Friend deletedFriend;
    String username;

    FirebaseFirestore db;
    CollectionReference collectionReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_friend_list, container, false);

        Bundle bundle = this.getArguments();
        username = bundle.getString(username);

        // TESTING
        addFriend("hongwei22", new Friend("testFriend", "Actual Test Name"));

        friendDataList = new ArrayList<>();
        friendList = (RecyclerView) rootView.findViewById(R.id.recyclerView_friend);
        friendRecyclerAdapter = new FriendListAdapter(getActivity(), friendDataList);
        friendList.setAdapter(friendRecyclerAdapter);
        friendList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // ItemTouchHelper helps to define the swipe function
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        ItemTouchHelper.attachToRecyclerView(friendList);

        requestDataList = new ArrayList<>();
        requestList = (RecyclerView) rootView.findViewById(R.id.recyclerView_request);
        requestRecyclerAdapter = new FriendListAdapter(getActivity(), requestDataList);
        requestList.setAdapter(requestRecyclerAdapter);
        requestList.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateFriendList(username);

        add_friend = (FloatingActionButton) rootView.findViewById(R.id.add_friend_button);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                    // TODO a delete query (next line is wrong)
                    // more information on array operations in firestore
                    // https://firebase.googleblog.com/2018/08/better-arrays-in-cloud-firestore.html
                    removeFriend(username, deletedFriend);

                    // TODO have a undo deletion step
                    Snackbar.make(friendList, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addFriend(username, deletedFriend);
                        }
//                        @Override
//                        public void onClick(View view) {
//                            HashMap<String, String> data = new HashMap<>();
//                            data.put("event name", deletedEvent.getName());
//                            data.put("event comment", deletedEvent.getComment());
//                            collectionReference.document(deletedEvent.getEventID()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(getActivity(), "Restored", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
                    }).show();
                    break;
            }
        }
    };

    public void removeFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayRemove(targetFriend));
    }

    public void addFriend(String username, Friend targetFriend) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.update("friends", FieldValue.arrayUnion(targetFriend));
    }

    // get friends and friend requests from the user that is passed in
    public void updateFriendList(String username) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        friendDataList = (ArrayList<Friend>) document.get("friends");
                        friendRecyclerAdapter.notifyDataSetChanged();

                        requestDataList = (ArrayList<Friend>) document.get("requests");
                        requestRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}