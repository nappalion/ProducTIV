package com.example.productiv.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.productiv.R;

public class ComposeActivity extends AppCompatActivity {

    EditText etGoalName;
    EditText etDailyGoal;
    EditText etRepeat;
    ImageView btnCompose;

    String goalName;
    String dailyGoal;
    String repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

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
                    // Create new UserGoal
                }
            }
        });
    }
}