package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HabitDetailFragment extends Fragment {

    TextView habitTitle;
    TextView habitReason;
    TextView dateOfStarting;
    TextView repeat;
    ProgressBar visualIndicator;
    TextView percentage;
    Button edit;

    private static final String TAG = "MyActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_habit_detail, container, false);
        /*
        habitTitle = rootView.findViewById(R.id.textView_habitTitle);
        habitReason = rootView.findViewById(R.id.textView_habitReason);
        dateOfStarting = rootView.findViewById(R.id.textView_dateOfStarting);
        repeat = rootView.findViewById(R.id.textView_repeat);
        visualIndicator = rootView.findViewById(R.id.progressBar_visualIndicator);
        percentage = rootView.findViewById(R.id.textView_percentage);
        edit = rootView.findViewById(R.id.button);


        */
        Log.d(TAG, "onCreateView: runable");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit = getView().findViewById(R.id.button);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitDetailFragment_to_habitEditFragment);
            }
        });



    }
}