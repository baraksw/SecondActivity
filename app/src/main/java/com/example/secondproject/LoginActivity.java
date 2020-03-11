package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private ImageButton mLoginBtn;
    private SignInButton mGoogleBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final String UserNameString = "deafultUserName";
    private String userName = "defaultUserName";
    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    UsersMap users_map;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = (EditText) findViewById(R.id.email_editText);
        mPasswordField = (EditText) findViewById(R.id.password_editText);
        mLoginBtn = (ImageButton) findViewById(R.id.loginBtn);
        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);

        onChangeAuthState();

        // Configure Google Sign In
        googleSignInProccess();

        //regular sign in proccess (username + passowrd)
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegularSignIn();
            }
        });

    }

    private void googleSignInProccess() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    private void onChangeAuthState() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser() != null) {
                Intent SignInSuccessfulIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                SignInSuccessfulIntent.putExtra(UserNameString, userName);
                startActivity(SignInSuccessfulIntent);
            }
        }
    };


    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, R.string.google_sign_success_msg, Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, R.string.google_sign_fail_msg, Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, R.string.google_sign_success_msg, Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateGoogleUserDetails(user);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.google_sign_fail_msg, Toast.LENGTH_SHORT).show();
                    updateGoogleUserDetails(null);
                }

            }
        });
    }

    private void updateGoogleUserDetails(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String full_name = account.getGivenName() + ' ' + account.getFamilyName();
            String user_name = account.getDisplayName();
            String email = account.getEmail();

            User new_user = new User();
            new_user.setFull_name(full_name);
            new_user.setUser_name(user_name);
            addGoogleUserToDB(new_user);

        }
    }

    public void addGoogleUserToDB(final User new_user) {
        mDataBase.child("DB").child("users_db").child(new_user.getFull_name()).setValue(new_user);
        Toast.makeText(LoginActivity.this, R.string.google_sign_success_msg, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startRegularSignIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty((password))) {
            Toast.makeText(LoginActivity.this, R.string.empty_login_fields_msg, Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.sign_in_fail_msg, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    public void launchSignUpActivity(View view) {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    public void launchHomePageActivity(View view) {
        Intent homePageIntent = new Intent(this, HomePageActivity.class);
        startActivity(homePageIntent);
    }

}
