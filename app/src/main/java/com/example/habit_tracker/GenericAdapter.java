package com.example.habit_tracker;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class GenericAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<T> items;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val);

    public GenericAdapter(Context context, ArrayList<T> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = setViewHolder(parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindData(holder,items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
