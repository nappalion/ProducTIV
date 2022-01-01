package com.example.productiv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.productiv.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    EditText etVerifyPassword;
    Button btnSignup;
    Button btnLogin;
    ImageView ivBack;
    private FirebaseAuth mAuth;
    public static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etVerifyPassword = findViewById(R.id.etVerifyPassword);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        ivBack = findViewById(R.id.ivBack);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        etUsername.setText(getIntent().getStringExtra("username"));


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString() != null
                        && etPassword.getText().toString() != null
                        && !etEmail.getText().toString().equals("")
                        && !etPassword.getText().toString().equals("")) {
                    if (isValid(etEmail.getText().toString())
                            && etPassword.getText().toString().equals(etVerifyPassword.getText().toString())) {
                        //  Create account
                        createAccount();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginActivity();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginActivity();
            }
        });
    }

    public void createAccount() {
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goEmailVerificationActivity();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    // Check if email is valid using Matcher and Pattern
    private boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) return false;
        return pat.matcher(email).matches();
    }

    public void goLoginActivity() {
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        i.putExtra("username", etUsername.getText().toString());
        startActivity(i);
        finish();
    }

    public void goMainActivity() {
        Log.i(TAG, "Went to MainActivity from SignupActivity");
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void goEmailVerificationActivity() {
        Intent i = new Intent(SignupActivity.this, EmailVerifyActivity.class);
        startActivity(i);
        finish();
    }
}