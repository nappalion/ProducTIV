package com.example.productiv.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.productiv.R;
import com.example.productiv.adapters.GoalsAdapter;
import com.example.productiv.models.UserGoals;

import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment {

    RecyclerView rvGoals;
    List<UserGoals> sampleGoals;
    GoalsAdapter goalsAdapter;
    public static final String TAG = "GoalsFragment";

    public GoalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sampleGoals = new ArrayList<>();
        sampleGoals.add(new UserGoals("Exercise", "Daily", 4, 3));
        sampleGoals.add(new UserGoals("Study", "Weekly", 3, 2));
        sampleGoals.add(new UserGoals("Cook", "Daily", 1, 0));
        sampleGoals.add(new UserGoals("Drive", "Weekly", 6, 3));
        sampleGoals.add(new UserGoals("Guitar", "Weekly", 3, 3));
        sampleGoals.add(new UserGoals("Piano", "Weekly", 2, 0));

        GoalsAdapter.OnLongClickListener onLongClickListener = new GoalsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                sampleGoals.remove(position);
                // Notify the adapter
                goalsAdapter.notifyItemRemoved(position);
            }
        };

        GoalsAdapter.OnClickListener onClickListener = new GoalsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {

            }
        };

        // Create adapter passing in the user data
        goalsAdapter = new GoalsAdapter(sampleGoals, getContext(), onLongClickListener, onClickListener);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lookup the recyclerview in activity layout
        rvGoals = (RecyclerView) getView().findViewById(R.id.rvGoals);

        // Attach the adapter to the recyclerview to populate items
        rvGoals.setAdapter(goalsAdapter);

        // Set Layout manager to position the items
        rvGoals.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i(TAG, goalsAdapter.getItemCount() + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }
}