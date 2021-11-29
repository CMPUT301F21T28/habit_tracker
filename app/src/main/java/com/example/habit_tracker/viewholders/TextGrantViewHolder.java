package com.example.habit_tracker.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.R;

/**
 *  TextGrantViewHolder creates a viewHolder to display a single recyclerView row
 *  It provides a user's name, an accept button and a deny button
 */

public class TextGrantViewHolder extends RecyclerView.ViewHolder{
    private final TextView friendName;
    private final Button accept;
    private final Button deny;

    public TextGrantViewHolder(@NonNull View itemView) {
        super(itemView);
        friendName = (TextView) itemView.findViewById(R.id.friend_name_grant);
        accept = (Button) itemView.findViewById(R.id.request_accept_button);
        deny = (Button) itemView.findViewById(R.id.request_deny_button);
    }

    public TextView getFriendName() {
        return friendName;
    }

    public Button getAccept() {
        return accept;
    }

    public Button getDeny() {
        return deny;
    }
}
