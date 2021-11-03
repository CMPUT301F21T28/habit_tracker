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

import org.w3c.dom.Text;

public class HabitDetailFragment extends Fragment {

    Habit habit;
    TextView habitTitle;
    TextView habitReason;
    TextView dateOfStarting;
    TextView repeat;
    TextView isPrivate;
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

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("username")){
            habit = bundle.getParcelable("Habit");
        }


        habitTitle = rootView.findViewById(R.id.textView_habitTitle);
        habitReason = rootView.findViewById(R.id.textView_habitReason);
        dateOfStarting = rootView.findViewById(R.id.textView_dateOfStarting);
        repeat = rootView.findViewById(R.id.textView_repeat);
        visualIndicator = rootView.findViewById(R.id.progressBar_visualIndicator);
        percentage = rootView.findViewById(R.id.textView_percentage);
        isPrivate = rootView.findViewById(R.id.textView_private);
        edit = rootView.findViewById(R.id.button_edit);

        habitTitle.setText(habit.getHabitName());
        habitReason.setText(habit.getReason());
        dateOfStarting.setText(habit.getDateOfStarting());
        repeat.setText(habit.getRepeat());
        isPrivate.setText(Boolean.toString(habit.getIsPrivate()));
        //TODO visual indicator

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        edit = getView().findViewById(R.id.button_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putString("username", habit.getUserName());
                bundle.putParcelable("Habit", habit);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitDetailFragment_to_habitEditFragment, bundle);
            }
        });



    }
}