package com.example.productiv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoalsFragment extends Fragment {

    RecyclerView rvGoals;
    List<UserGoals> sampleGoals;
    GoalsAdapter goalsAdapter;
    public static final String TAG = "GoalsFragment";

    // Firebase initialize
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserGoalsRef;
    private FirebaseAuth mAuth;

    String currentDate = formatDate(Calendar.getInstance().getTime());

    public GoalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase initialize
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserGoalsRef = mFirebaseDatabase.getReference("userGoals").child(currentUser.getUid());

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

        mUserGoalsRef.addValueEventListener(goalListener);

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

    public String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(date);
        return formattedDate;
    }
}