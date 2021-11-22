package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habit_tracker.adapters.FriendSearchAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class FriendSearchFragment extends Fragment {

    RecyclerView searchList;
    FriendSearchAdapter searchAdapter;
    ArrayList<Friend> searchDataList;
    ArrayList<String> friendDataList;

    // TODO initialize the following
    FirebaseFirestore db;
    CollectionReference collectionReference;
    private Button searchButton;
    private EditText editTextSearchUsername;

    public FriendSearchFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_friend_search, container, false);

        // TODO implement a bundle
        searchDataList = new ArrayList<>();
        friendDataList = new ArrayList<>();
        searchList = (RecyclerView) rootView.findViewById(R.id.search_recyclerView);
        Bundle bundle = getArguments();
        Friend currentUser = new Friend(bundle.getString("username"), bundle.getString("realname"));
        searchAdapter = new FriendSearchAdapter(getActivity(), searchDataList, currentUser);
        searchList.setAdapter(searchAdapter);
        searchList.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO use 'addSnapshotListener' to pull data from db, use for loop to add data to habitDataList (an ArrayList<Habit>),
        //  can look up how I implement in HabitListFragment.java

        searchButton = (Button) getView().findViewById(R.id.button_search);
        editTextSearchUsername = (EditText) getView().findViewById(R.id.editTextTextPersonName);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextSearchUsername.getText().toString();
                if (username.isEmpty()) {
                    //Log.d("Fail", "empty field");
                    editTextSearchUsername.setError("Username is required!");
                    editTextSearchUsername.requestFocus();
                    return;
                }
                Bundle bundle = getArguments();
                if (bundle.getString("username").equals(username)) {
                    editTextSearchUsername.setError("Cannot follow yourself!");
                    editTextSearchUsername.requestFocus();
                    return;
                }

                // Get friends of current user
                getFriendList(bundle.getString("username"));
                // Add the found user to the list
                updateSearchList(username);
            }
        });


    }

    public void updateSearchList(String username) {
        final CollectionReference usernameRef = db.collection("Users");
        Query query = usernameRef.whereEqualTo("username", username);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    searchDataList.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String friendUsername = documentSnapshot.getString("username");
                        String friendActualName = documentSnapshot.getString("realname");
                        // If the user is not a friend of current user
                        if (!friendDataList.contains(friendUsername)){
                            searchDataList.add(new Friend(friendUsername, friendActualName));
                        } else {
                            // If the requested user is already a friend
                            editTextSearchUsername.setError("Already friends! Please try another one.");
                            return;
                        }

                    }
                    searchAdapter.notifyDataSetChanged();
                }
                // If no user is found
                if (task.getResult().size() == 0) {
                    Log.d("Failure", "User not Exists");
                    editTextSearchUsername.setError("User does not exist. Please try another one.");

                }

            }
        });

    }

    public void getFriendList(String username) {
        DocumentReference usersRef = db.collection("Users").document(username);
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        friendDataList.clear();
                        // Getting the array of friends
                        ArrayList<Map> friends = (ArrayList<Map>) document.get("friends");
                        if (friends != null) {
                            for (Map<String, String> map : friends) {
                                String username = map.get("userName");
                                friendDataList.add(username);
                            }
                        }
                    }
                }
            }
        });
    }
}