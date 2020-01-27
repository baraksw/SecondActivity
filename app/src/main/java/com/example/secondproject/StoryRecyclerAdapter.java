package com.example.secondproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.StoryViewHolder> {
    private Context context;
    private ArrayList<Hum> story_hums;
    private boolean answerWindowVisible = false;

    public StoryRecyclerAdapter(Context context, ArrayList<Hum> story_hums) {
        this.context = context;
        this.story_hums = story_hums;

    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_row_layout, parent, false);
        StoryViewHolder storyViewHolder = new StoryViewHolder(view);
        return storyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        String friend_row_full_name = story_hums.get(position).getOwner();
        int hum_row_len = story_hums.get(position).getHum_len();

        holder.humLengthTextView.setText("0:" + String.valueOf(hum_row_len));
        holder.storyHumPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story_hums.get(position).playHum();
            }
        });
        holder.ownerNameTextView.setText(friend_row_full_name);
        holder.confirmAnswer.setVisibility(View.GONE);
        holder.answerEditText.setVisibility(View.GONE);
        holder.answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answerWindowVisible){
                    holder.confirmAnswer.setVisibility(View.VISIBLE);
                    holder.answerEditText.setVisibility(View.VISIBLE);
                    answerWindowVisible = true;
                }else if (answerWindowVisible){
                    holder.confirmAnswer.setVisibility(View.GONE);
                    holder.answerEditText.setVisibility(View.GONE);
                    answerWindowVisible = false;
                }
            }
        });

        holder.confirmAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                story_hums.get(position).answereHum(holder.answerEditText.getText().toString());
                holder.answerEditText.setVisibility(View.GONE);
                Toast.makeText( v.getContext(), "Thanks for your answer!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() { return story_hums.size(); }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {

        TextView friendRowFullName;
        ImageButton storyHumPlay;
        ImageButton answerButton;
        ImageButton confirmAnswer;
        EditText answerEditText;
        TextView ownerNameTextView;
        TextView humLengthTextView;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            friendRowFullName = itemView.findViewById(R.id.friend_row_full_name_textView);
            storyHumPlay = itemView.findViewById(R.id.play_story_hum_imageButton);
            answerButton = itemView.findViewById(R.id.answer_imageButton);
            answerEditText = itemView.findViewById(R.id.answer_editText);
            confirmAnswer = itemView.findViewById(R.id.confirm_answer_imageButton);
            ownerNameTextView = itemView.findViewById(R.id.friend_row_full_name_textView);
            humLengthTextView = itemView.findViewById(R.id.hum_length_textView);
        }
    }
}
