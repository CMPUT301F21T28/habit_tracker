package com.example.habit_tracker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.ViewHolder> {

    ArrayList<Habit> habits;
    String habit_name_list[];
    Integer habit_progress_list[];
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
        holder.habitName.setText(habit.getHabitName());

        //TODO progress bar undone

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", habit.getUserName());
                bundle.putParcelable("Habit", habit);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitListFragment_to_habitDetailFragment, bundle);
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
        }
    }
}
