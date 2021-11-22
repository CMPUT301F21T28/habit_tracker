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

import com.example.habit_tracker.adapters.HabitListAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FriendInfoFragment extends Fragment {

    RecyclerView habitList;
    HabitListAdapter habitListAdapter;
    ArrayList<Habit> habitDataList;

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

        // TODO handle the bundle

        habitDataList = new ArrayList<>();
        habitList = (RecyclerView) rootView.findViewById(R.id.recyclerView_friendInfo);
        habitListAdapter = new HabitListAdapter(getActivity(), habitDataList);
        habitList.setAdapter(habitListAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // TODO use 'addSnapshotListener' to pull data from db, use for loop to add data to habitDataList (an ArrayList<Habit>),
        //  can look up how I implement in HabitListFragment.java


    }
}