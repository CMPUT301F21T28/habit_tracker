package com.example.habit_tracker.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.Friend;
import com.example.habit_tracker.R;
import com.example.habit_tracker.firebaseUtils.utils;

import java.util.ArrayList;

public class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.ViewHolder> {

    ArrayList<Friend> friends;
    Context context;
    Friend currentUser;

    public FriendSearchAdapter(Context ctx, ArrayList<Friend> friends, Friend currentUser) {
        context = ctx;
        this.friends = friends;
        this.currentUser = currentUser;
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
        //Log.d("tag", "Value: " + friend.getActualName());
        holder.friendName.setText(friend.getActualName() + " ("+friend.getUserName()+")");
        utils newUtil = new utils();

        holder.requestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (holder.requestButton.getText().toString().equals("Request")){
                    Log.d("tag", "Value: " + holder.requestButton.getText().toString());
                    newUtil.addRequest(friend.getUserName(),currentUser);
                    holder.requestButton.setText("Cancel");
                }
                else if (holder.requestButton.getText().toString().equals("Cancel")){
                    newUtil.removeRequest(friend.getUserName(),currentUser);
                    holder.requestButton.setText("Request");
                }


            }
        });


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
