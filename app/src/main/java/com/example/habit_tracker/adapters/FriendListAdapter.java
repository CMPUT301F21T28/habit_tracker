package com.example.habit_tracker.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.Friend;
import com.example.habit_tracker.R;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    ArrayList<Friend> friends;
    Context context;
    String currentUsername;
    private static final String TAG = "MyActivity";

    public FriendListAdapter(Context ctx, ArrayList<Friend> friends, String currentUsername) {
        this.context = ctx;
        this.friends = friends;
        this.currentUsername = currentUsername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.general_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.ViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.friendName.setText((friend.getActualName()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", currentUsername);
                bundle.putParcelable("friend", friend);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                NavController controller = Navigation.findNavController(view);

                controller.navigate(R.id.action_friendListFragment_to_friendInfoFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView friendName;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            friendName = itemView.findViewById(R.id.name_row);
        }
    }
}
