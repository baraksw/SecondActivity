package com.example.secondproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.HumViewHolder> {
    private Hum[] hums;

    public RecyclerAdapter(Hum[] hums){
        this.hums = hums;
    }

    @NonNull
    @Override
    public HumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hum_row_layout, parent, false);
        HumViewHolder humViewHolder = new HumViewHolder(view);

        return humViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HumViewHolder holder, int position) {
        int numOfListeners = hums[position].num_of_listeners;
        int humRecLen = hums[position].hum_rec_len;

        holder.numOfListeners.setText(String.valueOf(numOfListeners));
        holder.humRecLen.setText(String.valueOf(humRecLen));

    }

    @Override
    public int getItemCount() {
        return hums.length;
    }

    public static class HumViewHolder extends RecyclerView.ViewHolder {

        TextView numOfListeners;
        TextView humRecLen;

        public HumViewHolder(@NonNull View itemView) {
            super(itemView);
            numOfListeners = itemView.findViewById(R.id.num_of_listeners_textView);
            humRecLen = itemView.findViewById(R.id.hum_length_textView);
        }
    }
}
