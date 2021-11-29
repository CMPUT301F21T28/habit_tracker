package com.example.habit_tracker.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.R;

/**
 *  TextSearchViewHolder creates a viewHolder to display a single recyclerView row
 *  It provides a user's name, a button for requesting others to follow
 */

public class TextSearchViewHolder extends RecyclerView.ViewHolder {
    private final TextView userName;
    private final Button request;

    public TextSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.friend_name_search);
        request = (Button) itemView.findViewById(R.id.button_request);
    }

    public TextView getUserName() {
        return userName;
    }

    public Button getRequest() {
        return request;
    }
}
