package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habit_tracker.viewholders.TextSearchViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * FriendSearchFragment creates a fragment for searching a friend in the db
 */

public class FriendSearchFragment extends Fragment {

    RecyclerView searchList;
    GenericAdapter<Friend> searchAdapter;
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

        // handle the bundle
        Bundle bundle = getArguments();
        Friend currentUser = new Friend(bundle.getString("username"), bundle.getString("realname"));

        db = FirebaseFirestore.getInstance();

        // inflate the recyclerView with generic adapter
        searchDataList = new ArrayList<>();
        friendDataList = new ArrayList<>();
        searchList = (RecyclerView) rootView.findViewById(R.id.search_recyclerView);
        searchAdapter = new GenericAdapter<Friend>(getActivity(), searchDataList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                return new TextSearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_row, parent, false));
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Friend val) {
                ((TextSearchViewHolder) holder).getUserName().setText(val.getActualName() + " ("+val.getUserName()+")");
                Button requestButton = ((TextSearchViewHolder) holder).getRequest();
                requestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (requestButton.getText().toString().equals("Request")){
                            Log.d("tag", "Value: " + requestButton.getText().toString());
                            Utility.addRequest(val.getUserName(),currentUser);
                            requestButton.setText("Cancel");
                        }
                        else if (requestButton.getText().toString().equals("Cancel")){
                            Utility.removeRequest(val.getUserName(),currentUser);
                            requestButton.setText("Request");
                        }
                    }
                });
            }
        };
        searchList.setAdapter(searchAdapter);
        searchList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchList.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchButton = (Button) getView().findViewById(R.id.button_search);
        editTextSearchUsername = (EditText) getView().findViewById(R.id.editTextTextPersonName);

        // Handle the search button
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

    /**
     * update the list if the user is searched successfully
     * @param username
     * @return
     */
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

    /**
     * Get the friend list of the user
     * @param username
     * @return
     */
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