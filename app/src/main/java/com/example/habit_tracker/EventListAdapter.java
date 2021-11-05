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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    ArrayList<Event> events;
    Context context;
    private static final String TAG = "MyActivity";

    /**
     * EventListAdapter is a customized list. It stores data retrieved from firebase
     * @param ctx
     * @param events
     */
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

    /**
     * Create the view of recyclerView
     * Short Click； cause Fragment change to event detail Fragment.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventName.setText(event.getName());

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

        /**
         * Create a row's view of recyclerView
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.habit_progress_row);
            progressBar.setVisibility(View.INVISIBLE);
            eventName = itemView.findViewById(R.id.habit_name_row);
        }
    }
}
