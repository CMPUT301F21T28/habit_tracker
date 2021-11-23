package com.example.habit_tracker.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class GenericAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<T> items;
    private OnRecyclerItemClicked onRecyclerItemClicked;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent, OnRecyclerItemClicked onRecyclerItemClicked);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val);

    public abstract OnRecyclerItemClicked onGetRecyclerItemClickListener();

    public GenericAdapter(Context context, ArrayList<T> items){
        this.context = context;
        this.items = items;
        onRecyclerItemClicked = onGetRecyclerItemClickListener();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = setViewHolder(parent,onRecyclerItemClicked);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindData(holder,items.get(position));

    }

    public T getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnRecyclerItemClicked{
        void onItemClicked(View view, int position);
    }
}

