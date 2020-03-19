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
        holder.humRecLen.setText("0:0" + String.valueOf(humRowLen));
        holder.humPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hums.get(position).playHum();
            }
        });

        if(!(hums.get(position).hum_answer.equals("NULL"))){
            holder.humNameText.setText(hums.get(position).hum_answer);
            holder.humAnsweredImage.setVisibility(View.VISIBLE);
            holder.humAnsweredText.setVisibility(View.VISIBLE);
        } else {
            holder.humNameText.setText(R.string.no_answer_text);
            holder.humAnsweredImage.setVisibility(View.INVISIBLE);
            holder.humAnsweredText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return hums.size();
    }

    public static class HumViewHolder extends RecyclerView.ViewHolder {

        //TextView numOfListeners;
        TextView humRecLen;
        ImageButton humPlay;
        ImageView humAnsweredImage;
        TextView humAnsweredText;
        TextView humNameText;

        public HumViewHolder(@NonNull View itemView) {
            super(itemView);
            //numOfListeners = itemView.findViewById(R.id.num_of_listeners_textView);
            humRecLen = itemView.findViewById(R.id.hum_length_textView);
            humPlay = itemView.findViewById(R.id.play_record_button);
            humAnsweredImage = itemView.findViewById(R.id.hum_answered_imageView);
            humAnsweredText = itemView.findViewById(R.id.hum_answered_textView);
            humNameText = itemView.findViewById(R.id.hum_name_textView);
        }
    }
}
