package com.example.habit_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    FloatingActionButton edit;
    TextView progress;

    private static final String TAG = "MyActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create view for HabitDetailFragment
     * Extract necessities (e.g. username, instance of Habit class) from bundle, set TextViews to their corresponding values
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_habit_detail, container, false);

        Bundle bundle = this.getArguments();
        habit = bundle.getParcelable("Habit");

        habitTitle = rootView.findViewById(R.id.textView_habitTitle);
        habitReason = rootView.findViewById(R.id.textView_habitReason);
        dateOfStarting = rootView.findViewById(R.id.textView_dateOfStarting);
        repeat = rootView.findViewById(R.id.textView_repeat);
        visualIndicator = rootView.findViewById(R.id.progressBar_visualIndicator);
        percentage = rootView.findViewById(R.id.buttonPercentage);
        isPrivate = rootView.findViewById(R.id.textView_private);
        edit = rootView.findViewById(R.id.button_edit);
        progress = rootView.findViewById(R.id.textView5);

        habitTitle.setText(habit.getName());
        if (habit.getComment().length() == 0) {
            habitReason.setText("Comment is not set");
        } else {
            habitReason.setText(habit.getComment());
        }
        dateOfStarting.setText("Starts on " + habit.getDateOfStarting());
        repeat.setText("Repeat on every" + habit.getRepeat());
        if (habit.getIsPrivate()) {
            isPrivate.setText("Followers cannot see this habit");
        } else {
            isPrivate.setText("Followers can see this habit");
        }
        visualIndicator.setProgress(Math.round(habit.getProgress()));
        percentage.setText(Math.round(habit.getProgress()) + "%");
        progress.setText("Complete " + habit.getFinish() + "/" + habit.getPlan() + " times");

        //TODO visual indicator

        return rootView;
    }

    /**
     * Initialize all other parts that could cause the fragment status change
     * Fragment change by navigation
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit = getView().findViewById(R.id.button_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putString("username", habit.getUsername());
                bundle.putParcelable("Habit", habit);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitDetailFragment_to_habitEditFragment, bundle);
            }
        });



    }
}