package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    private int count_friends = 0;
    private EditText username_EditText;
    private EditText email_EditText;
    private EditText password_EditText;
    private EditText full_name_EditText;
    private ImageButton register_Button;
    private DatabaseReference db_Reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    public User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        username_EditText = findViewById(R.id.user_nameField);
        email_EditText = findViewById(R.id.email_editText);
        password_EditText = findViewById(R.id.password_editText);
        register_Button = findViewById(R.id.register_btn);
        full_name_EditText = findViewById(R.id.full_nameField);

        current_user = new User();

        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

        private void startRegister(){
            final String full_name = full_name_EditText.getText().toString().trim();
            final String username = username_EditText.getText().toString().trim();
            String email = email_EditText.getText().toString().trim();
            String password = password_EditText.getText().toString().trim();

            if(!TextUtils.isEmpty(full_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            current_user.setFull_name(full_name);
                            current_user.setUser_name(username);
                            addRegularUserToDB(current_user);
                            addNameToUser(current_user.getFull_name()); //allows us to address the current user using his username value

                            Intent AuthIntent = new Intent(SignUpActivity.this, HomePageActivity.class);
                            AuthIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(AuthIntent);
                        }
                    }
                });}
        }

    public void addRegularUserToDB(final User new_user) {
        db_Reference.child("DB").child("users_db").child(new_user.getFull_name()).setValue(new_user);

    }

    private void addNameToUser(String full_name){
        FirebaseUser fb_user = mAuth.getCurrentUser();
        if(fb_user!=null)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(full_name).build();
            fb_user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                    }
                }
            });

        }


    }

    public void launchLoginActivity(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }


    /*

    public void Restart_DB(View view) {
        users_map = new UsersMap();
        current_user.setFull_name("NULL_FULL");
        current_user.setUser_name("NULL_USER");
        users_map.add_user(current_user);
        mDataBase.child("DB").setValue(users_map);
        Toast.makeText(SignUpActivity.this, "DB Restarted", Toast.LENGTH_SHORT).show();
    }

     */

}

