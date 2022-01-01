package com.example.productiv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productiv.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerifyActivity extends AppCompatActivity {

    TextView tvEmail;
    Button btnResendEmail;
    Button btnContact;
    public static final String TAG = "EmailVerifyActivity";
    private FirebaseAuth mAuth;
    boolean isSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        tvEmail = findViewById(R.id.tvEmail);
        btnResendEmail = findViewById(R.id.btnResendEmail);
        btnContact = findViewById(R.id.btnContact);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();

        tvEmail.setText(user.getEmail());

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailVerifyActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            isSent = true;
                        } else {
                            Log.i(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(EmailVerifyActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        while(isSent) {
            if (user.isEmailVerified()) {
                Log.i(TAG, "Email is verified!");
                goMainActivity();
            }
        }
    }

    public void goMainActivity() {
        Log.i(TAG, "Went to MainActivity from EmailVerifyActivity");
        Intent i = new Intent(EmailVerifyActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}