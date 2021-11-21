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

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    ArrayList<Friend> friends;
    Context context;

    public FriendRequestAdapter(Context ctx, ArrayList<Friend> friends) {
        context = ctx;
        this.friends = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.request_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.ViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.friendName.setText(friend.getActualName());
        //TODO implement accept button and deny button, try to implement onClickListener here

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        Button acceptButton;
        Button denyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friendName = itemView.findViewById(R.id.friend_name_request);
            acceptButton = itemView.findViewById(R.id.request_accept_button);
            denyButton = itemView.findViewById(R.id.request_deny_button);
        }
    }
}
