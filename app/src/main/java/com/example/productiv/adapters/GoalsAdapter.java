package com.example.productiv.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productiv.R;
import com.example.productiv.activities.ComposeActivity;
import com.example.productiv.activities.MainActivity;
import com.example.productiv.activities.TimerGoalActivity;
import com.example.productiv.models.UserGoals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }


    public static final String TAG = "GoalsAdapter";

    private List<UserGoals> userGoals;
    Context context;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    private static final long millisInHour = 3600000;
    String currentTimerGoal;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();;
    private DatabaseReference mUserGoalsRef = mFirebaseDatabase.getReference("userGoals").child(currentUser.getUid());
    private DatabaseReference mUsersRef = mFirebaseDatabase.getReference("users").child(currentUser.getUid());

    public GoalsAdapter(List<UserGoals> userGoals, Context context, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.userGoals = userGoals;
        this.context = context;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }


    // Inflates the layout from XML and returns the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View goalView;

        // Inflate the custom layout
        if (context.getClass().equals(MainActivity.class)) {
            goalView = inflater.inflate(R.layout.item_goal, parent, false);
        }
        else {
            goalView = inflater.inflate(R.layout.item_timer_goal, parent, false);
        }

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(goalView);
        return viewHolder;
    }

    // Populates data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull GoalsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        UserGoals userGoal = userGoals.get(position);
        // Log.i(TAG, "onBindViewHolder called for position: " + position);

        // Set item views based on views in data model
        holder.bind(userGoal);
    }

    @Override
    public int getItemCount() {
        return userGoals.size();
    }

    // Provide a direct reference to each of the views within a data item (fast access)
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGoal;
        public TextView tvDailyGoal; // Displays daily goal needed under heading
        public TextView tvCurrentTime; // Displays current time
        public TextView tvGoalTime; // Displays goal time as denominator in fraction
        public RelativeLayout goalContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            if (context.getClass().equals(MainActivity.class)) {
                tvGoal = itemView.findViewById(R.id.tvGoal);
                tvDailyGoal = itemView.findViewById(R.id.tvDailyGoal);
                tvCurrentTime = itemView.findViewById(R.id.tvCurrentTime);
                tvGoalTime = itemView.findViewById(R.id.tvGoalTime);
                goalContainer = itemView.findViewById(R.id.goalContainer);
            }
            else {
                tvGoal = itemView.findViewById(R.id.tvGoal);
                goalContainer = itemView.findViewById(R.id.goalContainer);
            }
        }

        public void bind(UserGoals userGoal) {
            // Set item views based on your views and data model
            // Log.i(TAG, userGoal.getGoalName());
            if (context.getClass().equals(MainActivity.class)) {
                tvGoal.setText(userGoal.getGoalName());
                tvDailyGoal.setText(convertMillisToHours(userGoal.getDailyGoal()));
                tvCurrentTime.setText(convertMillisToHours(userGoal.getCurrentTime()));
                tvGoalTime.setText(convertMillisToHours(userGoal.getDailyGoal()));
            }
            else {
                tvGoal.setText(userGoal.getGoalName());
            }

            goalContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the position that was pressed.
                    clickListener.onItemClicked(getAdapterPosition());
                    mUsersRef.child("currentGoal").setValue(userGoal.getGoalName());
                }
            });

            mUsersRef.child("currentGoal").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        currentTimerGoal = String.valueOf(task.getResult().getValue());
                        // Log.i(TAG, currentTimerGoal);
                    }
                    else {
                        Log.e(TAG, "Error getting data", task.getException());
                    }
                }
            });

            goalContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (context.getClass().equals(MainActivity.class)) {
                        // Notify the position that was long pressed.
                        longClickListener.onItemLongClicked(getAdapterPosition());
                        // Delete the item from database
                        mUserGoalsRef.child(userGoal.getGoalName()).removeValue();
                        if (currentTimerGoal.equals(userGoal.getGoalName())) {
                            Log.i(TAG, currentTimerGoal + " is equal to " + userGoal.getGoalName());
                            mUsersRef.child("currentGoal").setValue("Click Me");
                        }
                    }
                    return true;
                }
            });
        }
    }

    public String convertMillisToHours(long millis) {
        long hours = millis / millisInHour;
        return String.valueOf(hours);
    }
}
