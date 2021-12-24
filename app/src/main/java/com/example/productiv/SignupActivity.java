package com.example.productiv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignupActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    EditText etVerifyPassword;
    Button btnSignup;
    Button btnLogin;
    ImageView ivBack;

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

        etUsername.setText(getIntent().getStringExtra("username"));

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

    public void goLoginActivity() {
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        i.putExtra("username", etUsername.getText().toString());
        startActivity(i);
    }
}