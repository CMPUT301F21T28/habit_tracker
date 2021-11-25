package com.example.habit_tracker;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SuperFragment extends Fragment {

    String username;
    Bundle inBundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit_add, container, false);
        // get the bundle passed from the previous fragment
        inBundle = this.getArguments();
        username = inBundle.getString("username");

        return rootView;
    }
}
