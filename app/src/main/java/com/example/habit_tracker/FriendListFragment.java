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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habit_tracker.viewholders.TextGrantViewHolder;
import com.example.habit_tracker.viewholders.TextViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

public class FriendListFragment extends Fragment {

    RecyclerView friendList;
    GenericAdapter<Friend> friendAdapter;
    ArrayList<Friend> friendDataList;

    RecyclerView requestList;
    GenericAdapter<Friend> requestAdapter;
    ArrayList<Friend> requestDataList;

    FloatingActionButton add_friend;

    String username;
    String realname;

    Utility firebaseUtils = new Utility();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

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
        realname = bundle.getString("realname");

        friendDataList = new ArrayList<>();
        friendList = (RecyclerView) rootView.findViewById(R.id.recyclerView_friend);
        friendAdapter = new GenericAdapter<Friend>(getActivity(), friendDataList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.general_list_row, parent, false));
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Friend val) {
                ((TextViewHolder) holder).getTextView().setText(val.getActualName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putParcelable("friend", val);
                        bundle.putString("realname", realname);

                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_friendListFragment_to_friendInfoFragment, bundle);
                    }
                });
            }
        };
        friendList.setAdapter(friendAdapter);
        friendList.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestDataList = new ArrayList<>();
        requestList = (RecyclerView) rootView.findViewById(R.id.recyclerView_request);
        requestAdapter = new GenericAdapter<Friend>(getActivity(), requestDataList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                return new TextGrantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grant_list_row, parent, false));
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Friend val) {
                ((TextGrantViewHolder) holder).getFriendName().setText(val.getActualName());
                ((TextGrantViewHolder) holder).getAccept().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("DEBUG", val.getActualName().toString());
                        Log.d("DEBUG", val.getUserName().toString());
                        Log.d("DEBUG", username.toString());
                        Log.d("DEBUG", username.toString());
                        firebaseUtils.addFriend(username, val);
                        firebaseUtils.removeRequest(username, val);
                        friendDataList.remove(val);
                        friendAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        requestList.setAdapter(requestAdapter);
        requestList.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateFriendList(username);

        add_friend = (FloatingActionButton) rootView.findViewById(R.id.add_friend_button);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("realname", realname);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_friendListFragment_to_friendSearchFragment, bundle);
            }
        });
    }

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
                    try {
                        for (Map<String, String> map : friends) {
                            String username = map.get("userName");
                            String realname = map.get("actualName");
                            friendDataList.add(new Friend(username, realname));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Getting the array of requests
                    try {
                        ArrayList<Map> requests = (ArrayList<Map>) documentSnapshot.get("requests");
                        for (Map<String, String> map : requests) {
                            String username = map.get("userName");
                            String realname = map.get("actualName");
                            requestDataList.add(new Friend(username, realname));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // Update the adapaters
                    friendAdapter.notifyDataSetChanged();
                    requestAdapter.notifyDataSetChanged();
                }
        });
    }


}