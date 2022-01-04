package com.example.productiv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.productiv.R;
import com.example.productiv.adapters.GoalsAdapter;
import com.example.productiv.models.UserGoals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimerGoalActivity extends AppCompatActivity {

    RecyclerView rvGoals;

    // Firebase initialize
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    List<UserGoals> sampleGoals;

    public static final String TAG = "TimerGoalActivity";

    GoalsAdapter goalsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_goal);

        rvGoals = findViewById(R.id.rvGoals);

        // Firebase initialize
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("userGoals").child(currentUser.getUid());

        sampleGoals = new ArrayList<>();

        ValueEventListener goalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // If allGoal with date (current date), goalName, and userID exists use allGoals

                // Else use userGoals, initializing goal and creating new allGoal with current date to use on reload
                // Get Goal object and add to sampleGoals list
                sampleGoals.clear();
                for (DataSnapshot snapShot: dataSnapshot.getChildren()) {
                    sampleGoals.add(snapShot.getValue(UserGoals.class));
                    Log.i(TAG, "Added " + snapShot.getValue(UserGoals.class));
                }
                goalsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Goal failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        };
        mRef.addValueEventListener(goalListener);

        GoalsAdapter.OnLongClickListener onLongClickListener = new GoalsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Do when long clicked
            }
        };

        GoalsAdapter.OnClickListener onClickListener = new GoalsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {

            }
        };

        // Create adapter passing in the user data
        goalsAdapter = new GoalsAdapter(sampleGoals, getApplicationContext(), onLongClickListener, onClickListener);

        // Lookup the recyclerview in activity layout
        rvGoals = (RecyclerView) findViewById(R.id.rvGoals);

        // Attach the adapter to the recyclerview to populate items
        rvGoals.setAdapter(goalsAdapter);

        // Set Layout manager to position the items
        rvGoals.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Log.i(TAG, goalsAdapter.getItemCount() + "");
    }
}