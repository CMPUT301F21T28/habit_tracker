package com.example.habit_tracker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.Friend;
import com.example.habit_tracker.R;
import com.example.habit_tracker.Utility;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    ArrayList<Friend> friends;
    Context context;
    String currentUserUsername;
    String currentUserRealname;
    Utility firebaseUtils = new Utility();


    public FriendRequestAdapter(Context ctx, ArrayList<Friend> friends, String username, String realname) {
        context = ctx;
        this.friends = friends;
        this.currentUserUsername = username;
        this.currentUserRealname = realname;
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
        holder.friendName.setText(friend.getActualName().concat("(").concat(friend.getUserName()).concat(")"));

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEBUG", friend.getActualName().toString());
                Log.d("DEBUG", friend.getUserName().toString());
                Log.d("DEBUG", currentUserUsername.toString());
                Log.d("DEBUG", currentUserRealname.toString());
                firebaseUtils.addFriend(currentUserUsername, friend);
                firebaseUtils.removeRequest(currentUserUsername, friend);
                friends.remove(friend);
                FriendRequestAdapter.this.notifyDataSetChanged();

                //Update the sender's friend list after accepting as well after accepting
            }
        });

        holder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUtils.removeRequest(currentUserUsername, friend);
                friends.remove(friend);
                FriendRequestAdapter.this.notifyDataSetChanged();
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
