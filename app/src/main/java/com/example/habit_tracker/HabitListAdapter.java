package com.example.habit_tracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.ViewHolder> {

    private static final String TAG = "MyActivity";

    ArrayList<Habit> habits;
    Context context;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitName.setText(habit.getName());

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // TODO change to a button implementation in the future update
                Bundle bundle = new Bundle();
                bundle.putString("username", habit.getUsername());
                bundle.putParcelable("Habit", habit);
                Log.d(TAG, "onLongClick: habit id " +habit.getHabitID());

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_eventListFragment, bundle);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView habitName;
        ProgressBar habitProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habit_name_row);
            habitProgress = itemView.findViewById(R.id.habit_progress_row);
            // TODO make habit progress re-visible after find solution for progress
            habitProgress.setVisibility(View.INVISIBLE);
        }
    }
}
