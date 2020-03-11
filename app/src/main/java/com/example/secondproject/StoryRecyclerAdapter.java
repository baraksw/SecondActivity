package com.example.secondproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.StoryViewHolder> {

    private Context context;
    private ArrayList<Hum> storyHums;
    private boolean answerWindowVisible = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public StoryRecyclerAdapter(Context context, ArrayList<Hum> story_hums) {
        this.context = context;
        this.storyHums = story_hums;
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
        String friend_row_full_name = storyHums.get(position).getOwner();
        int humRowLen = storyHums.get(position).getHum_len();
        holder.humLengthTextView.setText("0:0" + String.valueOf(humRowLen));
        holder.ownerNameTextView.setText(friend_row_full_name);
        holder.confirmAnswer.setVisibility(View.GONE);
        holder.answerEditText.setVisibility(View.GONE);

        holder.storyHumPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyHums.get(position).playHum();
            }
        });

        holder.answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answerWindowVisible) {
                    holder.confirmAnswer.setVisibility(View.VISIBLE);
                    holder.answerEditText.setVisibility(View.VISIBLE);
                    holder.spaceStory.setVisibility(View.GONE);
                    answerWindowVisible = true;
                } else if (answerWindowVisible) {
                    holder.confirmAnswer.setVisibility(View.GONE);
                    holder.answerEditText.setVisibility(View.GONE);
                    holder.spaceStory.setVisibility(View.VISIBLE);
                    answerWindowVisible = false;
                }
            }
        });

        holder.confirmAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = holder.answerEditText.getText().toString();
                storyHums.get(position).AddAnswerToDB(answer, context);

                holder.confirmAnswer.setVisibility(View.GONE);
                holder.answerEditText.setVisibility(View.GONE);
                holder.spaceStory.setVisibility(View.VISIBLE);
                closeKeyboard();
            }

            private void closeKeyboard() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.confirmAnswer.getWindowToken(), 0);
            }
        });
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {

        ImageButton storyHumPlay;
        ImageButton answerButton;
        ImageButton confirmAnswer;
        EditText answerEditText;
        TextView ownerNameTextView;
        TextView humLengthTextView;
        Space spaceStory;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyHumPlay = itemView.findViewById(R.id.play_story_hum_imageButton);
            answerButton = itemView.findViewById(R.id.answer_imageButton);
            answerEditText = itemView.findViewById(R.id.answer_editText);
            confirmAnswer = itemView.findViewById(R.id.confirm_answer_imageButton);
            spaceStory = itemView.findViewById(R.id.story_space);
            ownerNameTextView = itemView.findViewById(R.id.friend_row_full_name_textView);
            humLengthTextView = itemView.findViewById(R.id.hum_length_textView);
        }
    }

    @Override
    public int getItemCount() {
        return storyHums.size();
    }
}

