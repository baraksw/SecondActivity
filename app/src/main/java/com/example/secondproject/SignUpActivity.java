package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import com.firebase.client.Firebase;
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
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText fullNameEditText;
    private ImageButton registerBtn;
    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;

    public User current_user;
    public UsersMap users_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        usernameEditText = findViewById(R.id.user_nameField);
        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        registerBtn = findViewById(R.id.register_btn);
        fullNameEditText = findViewById(R.id.full_nameField);
        current_user = new User();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    public void launchLoginActivity(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

        private void startRegister(){
            final String fullName = fullNameEditText.getText().toString().trim();
            final String userName = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if(!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userName)) {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            current_user.setFull_name(fullName);
                            current_user.setUser_name(userName);
                            addRegularUserToDB(current_user);
                            addNameToUser(current_user.getFull_name());

                            Intent AuthIntent = new Intent(SignUpActivity.this, HomePageActivity.class);
                            AuthIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(AuthIntent);
                        }
                    }
                });}
        }

    public void addRegularUserToDB(final User new_user) {
        mDataBase.child("DB").child("users_db").child(new_user.getFull_name()).setValue(new_user);

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

    public void add_friend(String name){
        mDataBase.child("DB").child("users_db").child(current_user.getFull_name()).child("friends").child(String.valueOf(count_friends)).setValue(name);
        count_friends++;
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

