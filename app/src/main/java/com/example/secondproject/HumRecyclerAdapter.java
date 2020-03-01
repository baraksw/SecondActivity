package com.example.secondproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HumRecyclerAdapter extends RecyclerView.Adapter<HumRecyclerAdapter.HumViewHolder> {
    private ArrayList<Hum> hums;
    android.content.Context context;

    public HumRecyclerAdapter(Context context, ArrayList<Hum> hums){
        this.hums = hums;
        this.context = context;
    }

    @NonNull
    @Override
    public HumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hum_row_layout, parent, false);
        HumViewHolder humViewHolder = new HumViewHolder(view);
        return humViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HumViewHolder holder, final int position) {

        int num_of_listeners = hums.get(position).countNumOfListeners();
        int hum_row_len = hums.get(position).getHum_len();
        int hum_visibility = hums.get(position).getAnswered();

        holder.numOfListeners.setText(num_of_listeners + " Listeners"); //TODO: Update this to the latest version of hum.
        holder.humRecLen.setText(String.valueOf(hum_row_len));
        holder.humPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hums.get(position).playHum();
            }
        });
        holder.humAnsweredImage.setVisibility(hum_visibility);
        holder.humAnsweredText.setVisibility(hum_visibility);
    }

    @Override
    public int getItemCount() {
        return hums.size();
    }

    public static class HumViewHolder extends RecyclerView.ViewHolder {

        TextView numOfListeners;
        TextView humRecLen;
        ImageButton humPlay;
        ImageView humAnsweredImage;
        TextView humAnsweredText;

        public HumViewHolder(@NonNull View itemView) {
            super(itemView);
            numOfListeners = itemView.findViewById(R.id.num_of_listeners_textView);
            humRecLen = itemView.findViewById(R.id.hum_length_textView);
            humPlay = itemView.findViewById(R.id.play_record_button);
            humAnsweredImage = itemView.findViewById(R.id.hum_answered_imageView);
            humAnsweredText = itemView.findViewById(R.id.hum_answered_textView);
        }
    }
}
