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

        int humRowLen = hums.get(position).getHum_len();
        int hum_visibility = hums.get(position).getAnswered();

        //holder.numOfListeners.setText(num_of_listeners + " Listeners"); //TODO: Update this to the latest version of hum.
        holder.hum_length.setText("0:0" + String.valueOf(humRowLen));
        holder.play_hum_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hums.get(position).playHum();
            }
        });

        if(!(hums.get(position).hum_answer.equals("NULL"))){
            holder.hum_answer_TextView.setText(hums.get(position).hum_answer);
            holder.is_hum_answered_ImageButton.setVisibility(View.VISIBLE);
            holder.is_hum_answered_TextView.setVisibility(View.VISIBLE);
        } else {
            holder.hum_answer_TextView.setText(R.string.no_answer_text);
            holder.is_hum_answered_ImageButton.setVisibility(View.INVISIBLE);
            holder.is_hum_answered_TextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return hums.size();
    }

    public static class HumViewHolder extends RecyclerView.ViewHolder {

        //TextView numOfListeners;
        TextView hum_length;
        ImageButton play_hum_ImageButton;
        ImageView is_hum_answered_ImageButton;
        TextView is_hum_answered_TextView;
        TextView hum_answer_TextView;

        public HumViewHolder(@NonNull View itemView) {
            super(itemView);
            //numOfListeners = itemView.findViewById(R.id.num_of_listeners_textView);
            hum_length = itemView.findViewById(R.id.hum_length_textView);
            play_hum_ImageButton = itemView.findViewById(R.id.play_record_button);
            is_hum_answered_ImageButton = itemView.findViewById(R.id.hum_answered_imageView);
            is_hum_answered_TextView = itemView.findViewById(R.id.hum_answered_textView);
            hum_answer_TextView = itemView.findViewById(R.id.hum_name_textView);
        }
    }
}
