package com.example.habit_tracker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class GenericAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<T> items;
    private OnRecyclerItemClicked onRecyclerItemClicked;
//    private OnRecyclerItemLongClicked onRecyclerItemLongClicked;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val);

    //public abstract OnRecyclerItemClicked onGetRecyclerItemClickListener();

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

    public void setOnRecyclerItemClicked(OnRecyclerItemClicked onRecyclerItemClicked){
        this.onRecyclerItemClicked = onRecyclerItemClicked;
    }

    public interface OnRecyclerItemClicked{
        void onItemClicked(View view, int position);
    }

//
//    /*public void setOnRecyclerItemLongClicked(OnRecyclerItemLongClicked onRecyclerItemLongClicked) {
//        this.onRecyclerItemLongClicked = onRecyclerItemLongClicked;
//    }
//
//     */
//
//    public interface OnRecyclerItemLongClicked{
//        void onItemLongClicked(View view, int position);
//    }


}
