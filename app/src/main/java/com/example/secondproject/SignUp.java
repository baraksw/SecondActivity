package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUser_NameField;
    private Button mRegisterBtn;
    private Button mAuthBtn;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    public User first_user;
    public UsersMap users_map;
    public DatabaseReference users_DB_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        users_DB_ref = FirebaseDatabase.getInstance().getReference();
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth=FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        mUser_NameField = findViewById(R.id.user_nameField);
        mNameField = findViewById(R.id.nameField);
        mEmailField = findViewById(R.id.email_editText);
        mPasswordField = findViewById(R.id.password_editText);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mAuthBtn =findViewById(R.id.AuthBtn);
        first_user = new User();
        users_map = new UsersMap();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

    }
        private void startRegister(){
            final String name = mNameField.getText().toString().trim();
            final String user_name = mUser_NameField.getText().toString().trim();
            String email = mEmailField.getText().toString().trim();
            String password = mPasswordField.getText().toString().trim();


            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(user_name)) {
                mProgress.setMessage("Signing Up...");
                mProgress.show();
                first_user.setFull_name(name);
                first_user.setUser_name(user_name);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            users_map.add_user(first_user);
                            users_DB_ref.child("DB").setValue(users_map);
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDataBase.child(user_id);
                            current_user_db.child("USER").setValue(first_user);
                            mProgress.dismiss();
                            Intent AuthIntent = new Intent(SignUp.this,AuthActivity.class);
                            AuthIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(AuthIntent);
                        }
                    }
                });}

        }


    public void AuthPage(View view) {

        Intent AuthIntent = new Intent(this, AuthActivity.class);
        startActivity(AuthIntent);
    }
    }

