package com.example.secondproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.StoryViewHolder> {

    private Context context;
    private ArrayList<Hum> story_hums;
    private boolean is_answer_window_visible = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        int humRowLen = story_hums.get(position).getHum_len();
        holder.hum_length_TextView.setText("0:0" + String.valueOf(humRowLen));
        holder.hum_owner_TextView.setText(friend_row_full_name);
        holder.confirm_answer_Button.setVisibility(View.GONE);
        holder.answer_EditText.setVisibility(View.GONE);

        holder.play_hum_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story_hums.get(position).playHum();
            }
        });

        holder.answer_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_answer_window_visible) {
                    holder.confirm_answer_Button.setVisibility(View.VISIBLE);
                    holder.answer_EditText.setVisibility(View.VISIBLE);
                    holder.space_between_hums.setVisibility(View.GONE);
                    is_answer_window_visible = true;
                } else if (is_answer_window_visible) {
                    holder.confirm_answer_Button.setVisibility(View.GONE);
                    holder.answer_EditText.setVisibility(View.GONE);
                    holder.space_between_hums.setVisibility(View.VISIBLE);
                    is_answer_window_visible = false;
                }
            }
        });

        holder.confirm_answer_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = holder.answer_EditText.getText().toString();
                story_hums.get(position).AddAnswerToDB(answer, context);

                holder.confirm_answer_Button.setVisibility(View.GONE);
                holder.answer_EditText.setVisibility(View.GONE);
                holder.space_between_hums.setVisibility(View.VISIBLE);
                closeKeyboard();
            }

            private void closeKeyboard() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.confirm_answer_Button.getWindowToken(), 0);
            }
        });
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {

        ImageButton play_hum_Button;
        ImageButton answer_Button;
        ImageButton confirm_answer_Button;
        EditText answer_EditText;
        TextView hum_owner_TextView;
        TextView hum_length_TextView;
        Space space_between_hums;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            play_hum_Button = itemView.findViewById(R.id.play_story_hum_imageButton);
            answer_Button = itemView.findViewById(R.id.answer_imageButton);
            answer_EditText = itemView.findViewById(R.id.answer_editText);
            confirm_answer_Button = itemView.findViewById(R.id.confirm_answer_imageButton);
            space_between_hums = itemView.findViewById(R.id.story_space);
            hum_owner_TextView = itemView.findViewById(R.id.friend_row_full_name_textView);
            hum_length_TextView = itemView.findViewById(R.id.hum_length_textView);
        }
    }

    @Override
    public int getItemCount() {
        return story_hums.size();
    }
}

