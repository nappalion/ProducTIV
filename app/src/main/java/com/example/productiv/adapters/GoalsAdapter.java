package com.example.productiv.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productiv.R;
import com.example.productiv.models.UserGoals;

import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    public static final String TAG = "GoalsAdapter";

    private List<UserGoals> userGoals;
    Context context;

    public GoalsAdapter(List<UserGoals> userGoals, Context context) {
        this.userGoals = userGoals;
        this.context = context;
    }


    // Inflates the layout from XML and returns the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View goalView = inflater.inflate(R.layout.item_goal, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(goalView);
        return viewHolder;
    }

    // Populates data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull GoalsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        UserGoals userGoal = userGoals.get(position);
        Log.i(TAG, "onBindViewHolder called for position: " + position);

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

        public ViewHolder(View itemView) {
            super(itemView);

            tvGoal = itemView.findViewById(R.id.tvGoal);
            tvDailyGoal = itemView.findViewById(R.id.tvDailyGoal);
            tvCurrentTime = itemView.findViewById(R.id.tvCurrentTime);
            tvGoalTime = itemView.findViewById(R.id.tvGoalTime);
        }

        public void bind(UserGoals userGoal) {
            // Set item views based on your views and data model
            Log.i(TAG, userGoal.getGoalName());
            tvGoal.setText(userGoal.getGoalName());
            tvDailyGoal.setText(Integer.toString(userGoal.getDailyGoal()));
            tvCurrentTime.setText(Integer.toString(userGoal.getCurrentTime()));
            tvGoalTime.setText(Integer.toString(userGoal.getDailyGoal()));
        }
    }
}
