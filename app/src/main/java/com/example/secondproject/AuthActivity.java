package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


public class AuthActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final String UserNameString = "UserName";
    private String userName = "John";

    private SignInButton mGoogleBtn;
    GoogleSignInClient mGoogleSignInClient;
    //private String Tag = "AuthActivity";


    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent SignInIntent = new Intent(AuthActivity.this, AccountActivity.class);
                    SignInIntent.putExtra(UserNameString, userName);
                    startActivity(SignInIntent);

                }
            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });


    }

    private void SignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(AuthActivity.this,"Google Sign In Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }

        catch (ApiException e){
            Toast.makeText(AuthActivity.this,"Google Sign In Unsuccessfull", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this,"Successfull", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else
                {
                    Toast.makeText(AuthActivity.this,"Unsuccessfull", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }
        });
    }

    private void updateUI(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null)
        {
            String full_name = account.getDisplayName();
            String user_name = account.getGivenName() + ' ' + account.getFamilyName();
            String email = account.getEmail();
            Toast.makeText(AuthActivity.this,user_name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty((password))) {
            Toast.makeText(AuthActivity.this, "Fields are empty.", Toast.LENGTH_LONG).show();
        }

        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(AuthActivity.this, "Sign In Problem", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    public void SignUpPage(View view) {
        Intent SignUpIntent = new Intent(this, SignUp.class);
        startActivity(SignUpIntent);
    }


}
