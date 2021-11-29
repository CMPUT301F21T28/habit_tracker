package com.example.habit_tracker.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_tracker.R;

/**
 *  TextProgressViewHolder creates a viewHolder to display a single recyclerView row
 *  It provides a habit's name, a progress bar and a progress button to see the progress of a habit
 */

public class TextProgressViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final Button progressButton;
    private final ProgressBar progressBar;

    public TextProgressViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.habit_name_row);
        progressButton = (Button) itemView.findViewById(R.id.habit_progress_row);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }

    public TextView getTextView() {
        return textView;
    }
    public Button getProgressButton() {
        return progressButton;
    }
    public ProgressBar getProgressBar() { return progressBar; }
}
