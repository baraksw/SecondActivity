package com.example.secondproject;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.secondproject.Hum;


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
    public void onBindViewHolder(@NonNull HumViewHolder holder, final int position) {

        int num_of_listener = hums[position].countNumOfListeners();

        String num_of_listeners_str = String.valueOf(num_of_listener);
        String hum_len_str = String.valueOf(hums[position].hum_rec_len);

        holder.numOfListeners.setText(num_of_listeners_str);
        holder.humRecLen.setText(hum_len_str);

        holder.humPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hums[position].PlayHum();
            }
        });
        holder.youtubeUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(hums[position].youtube_url);
                Intent WebView = new Intent(Intent.ACTION_VIEW, uriUrl);
                //startActivity(WebView);
            }
        });

        holder.humPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add a call to the function that play the record of the hum.
            }
        });
    }

    @Override
    public int getItemCount() {
        return hums.length;
    }

    public static class HumViewHolder extends RecyclerView.ViewHolder {

        TextView numOfListeners;
        TextView humRecLen;
        ImageButton humPlay;
        ImageButton youtubeUrl;

        public HumViewHolder(@NonNull View itemView) {
            super(itemView);
            numOfListeners = itemView.findViewById(R.id.num_of_listeners_textView);
            humRecLen = itemView.findViewById(R.id.hum_name_textView);
            humPlay = itemView.findViewById(R.id.play_record_button);
            youtubeUrl = itemView.findViewById(R.id.youtube_logo_view);
        }
    }
}
