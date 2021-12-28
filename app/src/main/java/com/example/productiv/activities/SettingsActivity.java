package com.example.productiv.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.productiv.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout layoutLogout;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        layoutLogout = findViewById(R.id.layoutLogout);
        ivBack = findViewById(R.id.ivBack);

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                goLoginActivity();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });
    }

    public void goMainActivity() {
        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void goLoginActivity() {
        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(i);
    }
}