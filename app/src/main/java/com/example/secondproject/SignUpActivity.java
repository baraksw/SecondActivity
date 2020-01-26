package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SignUpActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUser_NameField;
    private ImageButton mRegisterBtn;
    private Button mAuthBtn;
    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference friends_db = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private Firebase mRef;

    public User current_user;
    public UsersMap users_map;
    public DatabaseReference users_DB_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mUser_NameField = findViewById(R.id.user_nameField);
        mEmailField = findViewById(R.id.email_editText);
        mPasswordField = findViewById(R.id.password_editText);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mNameField = findViewById(R.id.full_nameField);
        current_user = new User();
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    public void launchLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

        private void startRegister(){
            final String name = mNameField.getText().toString().trim();
            final String user_name = mUser_NameField.getText().toString().trim();
            String email = mEmailField.getText().toString().trim();
            String password = mPasswordField.getText().toString().trim();

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(user_name)) {
                mProgress.setMessage("Signing Up...");
                mProgress.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            current_user.setFull_name(name);
                            current_user.setUser_name(user_name);
                            addFriends(current_user);
                            add_user_to_db(current_user);
                            userProfile(current_user.getFull_name());
                            mProgress.dismiss();

                            Intent AuthIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                            AuthIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(AuthIntent);
                        }
                    }
                });}

        }


    public void AuthPage(View view) {

        Intent AuthIntent = new Intent(this, LoginActivity.class);
        startActivity(AuthIntent);
    }

    public void add_user_to_db(final User new_user) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users_map = dataSnapshot.child("DB").getValue(UsersMap.class);
                users_map.add_user(new_user);
                mDataBase.child("DB").setValue(users_map);
                mDataBase.child("NEWFRIEND").setValue(new_user.get_friend(2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userProfile(String full_name){
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
    private void addFriends(User new_user) {

        friends_db = FirebaseDatabase.getInstance().getReference().child("DB").child("users_db");
        friends_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User friend = dataSnapshot1.getValue(User.class);
                    new_user.add_friend(friend.getFull_name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void Restart_DB(View view) {
        users_map = new UsersMap();
        current_user.setFull_name("NULL_FULL");
        current_user.setUser_name("NULL_USER");
        users_map.add_user(current_user);
        mDataBase.child("DB").setValue(users_map);
        Toast.makeText(SignUpActivity.this, "DB Restarted", Toast.LENGTH_SHORT).show();
    }
}

