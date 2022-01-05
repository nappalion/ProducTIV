package com.example.productiv.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.productiv.R;
import com.example.productiv.models.UserGoals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class ComposeActivity extends AppCompatActivity {

    EditText etGoalName;
    EditText etDailyGoal;
    EditText etRepeat;
    ImageView btnCompose;

    String goalName;
    String dailyGoal;
    String repeat;

    public static final String TAG = "ComposeActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserGoalsRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserGoalsRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        etGoalName = findViewById(R.id.etGoalName);
        etDailyGoal = findViewById(R.id.etDailyGoal);
        etRepeat = findViewById(R.id.etRepeat);
        btnCompose = findViewById(R.id.btnCompose);

        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalName = etGoalName.getText().toString();
                dailyGoal = etDailyGoal.getText().toString();
                repeat = etRepeat.getText().toString();

                if (!goalName.equals("") && !dailyGoal.equals("") && !repeat.equals("")) {
                    UserGoals userGoal = new UserGoals(goalName, repeat, TimeUnit.HOURS.toMillis(Long.valueOf(dailyGoal)), 0);
                    mUserGoalsRef.child("userGoals").child(currentUser.getUid()).child(goalName).setValue(userGoal);
                    Log.i(TAG, "Created new goal with " + currentUser.getUid() + " as the user.");
                    Toast.makeText(getApplicationContext(), "Goal created successfully.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Unable to create goal.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}