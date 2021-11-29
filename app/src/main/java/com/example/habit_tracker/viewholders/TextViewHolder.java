package com.example.habit_tracker.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.R;

public class TextViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.name_row);
    }

    public TextView getTextView(){
        return textView;
    }
}
