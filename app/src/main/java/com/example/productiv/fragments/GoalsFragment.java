package com.example.productiv.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productiv.R;
import com.example.productiv.adapters.GoalsAdapter;
import com.example.productiv.models.UserGoals;

import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment {

    RecyclerView rvGoals;
    List<UserGoals> sampleGoals;
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

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lookup the recyclerview in activity layout
        rvGoals = (RecyclerView) getView().findViewById(R.id.rvGoals);

        // Create adapter passing in the user data
        GoalsAdapter adapter = new GoalsAdapter(sampleGoals, getContext());
        // Attach the adapter to the recyclerview to populate items
        rvGoals.setAdapter(adapter);

        // Set Layout manager to position the items
        rvGoals.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i(TAG, adapter.getItemCount() + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }
}