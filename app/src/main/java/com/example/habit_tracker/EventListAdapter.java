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

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    ArrayList<Event> events;
    Context context;
    private static final String TAG = "MyActivity";

    public EventListAdapter(Context ctx, ArrayList<Event> events) {
        this.context = ctx;
        this.events = events;
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
        Event event = events.get(position);
        holder.eventName.setText(event.getEventName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", event.getUsername());
                bundle.putString("habitID", event.getHabitID());
                bundle.putParcelable("Event", event);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_eventListFragment_to_eventDetailFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.habit_progress_row);
            progressBar.setVisibility(View.INVISIBLE);
            eventName = itemView.findViewById(R.id.habit_name_row);
        }
    }
}
