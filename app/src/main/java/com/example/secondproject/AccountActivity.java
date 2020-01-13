package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    String helloName = "Hey ";
    String name;
    private FirebaseAuth mAuth;
    private TextView mUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mUserName=(TextView) findViewById(R.id.helloTextView);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        name=intent.getStringExtra(AuthActivity.UserNameString);
        helloName=helloName+name;
        mUserName.setText(helloName);
    }

    public void GoToHomePage(View view) {
        Intent HomePageIntent = new Intent(AccountActivity.this,HomePageActivity.class);
        startActivity(HomePageIntent);
    }

    public void LogOut(View view){
       mAuth.signOut();
       Intent HomePageIntent = new Intent(AccountActivity.this,HomePageActivity.class);
       startActivity(HomePageIntent);
    }

}
