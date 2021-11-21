package com.example.habit_tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.Friend;
import com.example.habit_tracker.R;

import java.util.ArrayList;

public class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.ViewHolder> {

    ArrayList<Friend> friends;
    Context context;

    public FriendSearchAdapter(Context ctx, ArrayList<Friend> friends) {
        context = ctx;
        this.friends = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.friendName.setText(friend.getActualName());

        // TODO set button onClickListener here

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        Button requestButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_name_search);
            requestButton = itemView.findViewById(R.id.button_request);
        }
    }
}
