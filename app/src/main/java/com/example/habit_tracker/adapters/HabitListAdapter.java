package com.example.habit_tracker.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.Habit;
import com.example.habit_tracker.R;

import java.util.ArrayList;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.ViewHolder> {

    private static final String TAG = "MyActivity";

    ArrayList<Habit> habits;
    Context context;

    /**
     * HabitListAdapter is a customized list for storing DB-retrieved data
     * @param ctx
     * @param habits is the self-defined Habit class instance
     */
    public HabitListAdapter(Context ctx, ArrayList<Habit> habits) {
        context = ctx;
        this.habits = habits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.habit_list_row, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Create the view of the recyclerView
     * Short-Click: cause a fragment change by navigation, to see the habit details
     * Long-Click: cause a fragment change by navigation, to see the habit events
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitName.setText(habit.getName());
        holder.habitProgress.setText(Math.round(habit.getProgress()) + "%");

        //TODO progress bar undone

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", habit.getUsername());
                bundle.putParcelable("Habit", habit);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_habitDetailFragment, bundle);
            }
        });

        holder.habitProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", habit.getUsername());
                bundle.putParcelable("Habit", habit);
                Log.d(TAG, "onLongClick: habit id " +habit.getHabitID());

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_eventListFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView habitName;
        Button habitProgress;

        /**
         * Create the view for a single row of the recyclerView
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habit_name_row);
            habitProgress = itemView.findViewById(R.id.habit_progress_row);
        }
    }
}
