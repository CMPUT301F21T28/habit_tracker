package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habit_tracker.adapters.FriendSearchAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FriendSearchFragment extends Fragment {

    RecyclerView searchList;
    FriendSearchAdapter searchAdapter;
    ArrayList<Friend> searchDataList;

    // TODO initialize the following
    FirebaseFirestore db;
    CollectionReference collectionReference;

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
        searchList = (RecyclerView) rootView.findViewById(R.id.search_recyclerView);
        searchAdapter = new FriendSearchAdapter(getActivity(), searchDataList);
        searchList.setAdapter(searchAdapter);
        searchList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO use 'addSnapshotListener' to pull data from db, use for loop to add data to habitDataList (an ArrayList<Habit>),
        //  can look up how I implement in HabitListFragment.java
    }
}