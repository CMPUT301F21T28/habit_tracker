package com.example.habit_tracker.viewholders;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.R;

public class TextProgressViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final ProgressBar progressBar;

    public TextProgressViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.habit_name_row);
        progressBar = (ProgressBar) itemView.findViewById(R.id.habit_progress_row);
    }

    public TextView getTextView() {
        return textView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
