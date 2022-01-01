package com.example.productiv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.productiv.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    CheckBox btnRemember;
    Button btnLogin;
    Button btnForgot;
    Button btnSignup;
    private FirebaseAuth mAuth;
    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRemember = findViewById(R.id.btnRemember);
        btnForgot = findViewById(R.id.btnForgot);
        btnSignup = findViewById(R.id.btnSignup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        etEmail.setText(getIntent().getStringExtra("username"));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString() != null
                        && etPassword.getText().toString() != null
                        && !etEmail.getText().toString().equals("")
                        && !etPassword.getText().toString().equals("")) {
                    loginUser();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignupActivity();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            // Go to the Main Activity with user data
            goMainActivity();
        }
    }

    public void loginUser() {
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                goMainActivity();
                            }
                            else {
                                goEmailVerifyActivity();
                            }
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    public void goMainActivity() {
        Log.i(TAG, "Went to MainActivity from LoginActivity");
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void goSignupActivity() {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        i.putExtra("email", etEmail.getText().toString());
        startActivity(i);
        finish();
    }

    public void goEmailVerifyActivity() {
        Intent i = new Intent(LoginActivity.this, EmailVerifyActivity.class);
        startActivity(i);
        finish();
    }
}