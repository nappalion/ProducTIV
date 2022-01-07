package com.example.productiv.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.productiv.R;
import com.example.productiv.activities.MainActivity;
import com.example.productiv.activities.TimerGoalActivity;
import com.example.productiv.models.UserGoals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimerFragment extends Fragment {

    // GoalFragment
    private TextView tvGoal;
    String currentTimeValue;

    private EditText etTimer;
    private ToggleButton btnPlay;
    public static final String TAG = "TimerFragment";
    public static final int TIMER_MAX = 5999000;

    // Initialize timer duration
    private long setTime = TimeUnit.SECONDS.toMillis(10);
    private long startTime;
    private boolean isPaused;
    private static CountDownTimer countDownTimer;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Firebase initialize
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserGoalsRef;
    private DatabaseReference mUsersRef;
    private DatabaseReference mGoalHistoryRef;
    private FirebaseAuth mAuth;

    String currentDate = formatDate(Calendar.getInstance().getTime());

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase initialize
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserGoalsRef = mFirebaseDatabase.getReference("userGoals").child(currentUser.getUid());
        mUsersRef = mFirebaseDatabase.getReference("users").child(currentUser.getUid());
        mGoalHistoryRef = mFirebaseDatabase.getReference("goalHistory").child(currentUser.getUid()).child(currentDate);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null) tvGoal.setText(value);
                else tvGoal.setText("Click Me");

                btnPlay.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Goal failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        };

        mUsersRef.child("currentGoal").addValueEventListener(userListener);

        sharedPreferences = this.getActivity().getSharedPreferences("Timer", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        // Log.i(TAG, "Called onCreateView");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Initialize date
        currentDate = formatDate(Calendar.getInstance().getTime());

        Log.i(TAG, "Called onResume()");

        // SharedPreferences remembers startTime and isPaused after app is killed

        startTime = sharedPreferences.getLong("startTime", setTime);
        setTime = sharedPreferences.getLong("setTime", setTime);
        isPaused = sharedPreferences.getBoolean("isPaused", isPaused);

        // Log.i(TAG, "Getting startTime: " + sharedPreferences.getLong("startTime", setTime));

        // Deletes countDownTimer if doesn't exist
        if (countDownTimer != null) {
            Log.i(TAG, "Canceled timer");
            countDownTimer.cancel();
        }

        if (!tvGoal.getText().toString().equals("Click Me")) startContinueTimer();

        // Pause when resuming after selecting goal
        if (countDownTimer != null) timerPause();

        // If paused before exiting activity, app remembers and pauses the timer
        if (isPaused) {
            timerPause();
            btnPlay.setChecked(true);
        }
    }

    // Called after onViewCreated
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTimer = getView().findViewById(R.id.etTimer);
        btnPlay = getView().findViewById(R.id.btnPlay);
        tvGoal = getView().findViewById(R.id.tvGoal);

        tvGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTimerGoalActivity();
            }
        });

        // When user clicks confirm, set time and exit keyboard
        etTimer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                              @Override
                                              public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                  if (actionId == EditorInfo.IME_ACTION_DONE) {
                                                      String editTime = etTimer.getText().toString();

                                                      for (int i = editTime.length(); i <= 4; i++) {
                                                          editTime += 0;
                                                      }

                                                      if (calculateNewDuration(editTime) >= TIMER_MAX) editTime = "99:59";

                                                      setTime = calculateNewDuration(editTime);
                                                      startTime = calculateNewDuration(editTime);

                                                      editor.putLong("startTime", startTime);
                                                      editor.putLong("setTime", setTime);

                                                      hideKeyboard();

                                                  }
                                                  return false;
                                              }
                                          });

        // Pause if user tries to edit
        etTimer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnPlay.setChecked(true);
                timerPause();
                return false;
            }
        });

                btnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // If goal not chosen
                        if (tvGoal.getText().toString().equals("Click Me")) {
                            Toast.makeText(getActivity(), "Please choose a goal.", Toast.LENGTH_SHORT).show();
                            btnPlay.setChecked(true);
                            editor.putBoolean("isPaused", true);

                            return;
                        }

                        // If goal is chosen
                        if (btnPlay.isChecked()) {
                            timerPause();
                            editor.putBoolean("isPaused", true);
                        } else {
                            Log.i(TAG, "that one thing was called");
                            hideKeyboard();
                            timerResume();
                            editor.putBoolean("isPaused", false);
                        }
                        editor.apply();
                    }
                });
    }

    public void timerPause() {
        countDownTimer.cancel();
    }

    public void timerResume() {
        startContinueTimer();
    }

    private void startContinueTimer() {
        Log.i(TAG, "startContinueTimer called with startTime: " + startTime);
        // Initialize timer view
        etTimer.setText(calculateDuration(startTime));
        countDownTimer = new CountDownTimer(startTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // When tick
                if (!tvGoal.getText().toString().equals("Click Me")) {
                    mGoalHistoryRef.child(tvGoal.getText().toString()).child("currentTime").setValue(ServerValue.increment(1000));
                }

                // Set converted string on text view
                etTimer.setText(calculateDuration(millisUntilFinished));
                startTime = millisUntilFinished;


                editor.putLong("startTime", startTime);
                editor.apply();

                // Log.i(TAG, String.valueOf(startTime));
            }

            @Override
            public void onFinish() {
                startTime = setTime;
                editor.putLong("startTime", startTime);
                etTimer.setText(calculateDuration(startTime));
                // Log.i(TAG, "New startTime: " + startTime + " Taken from setTime: " + setTime);
                countDownTimer.cancel();
                editor.putBoolean("isPaused", true);
                editor.apply();
                btnPlay.setChecked(true);

            }
        }.start();
    }

    public long calculateNewDuration(String editTime) {
        int minutes = Integer.parseInt(editTime.substring(0,2));
        int seconds = Integer.parseInt(editTime.substring(3,5));

        long newTime = TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
        Log.i(TAG, "New Time: " + TimeUnit.MILLISECONDS.toMinutes(newTime) + " Using: " + minutes + "; minutes " + seconds + " seconds");

        return newTime;
    }

    public String calculateDuration(long time) {
        // Convert millisecond to minute and second
        int seconds = (int) (time / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String timerDuration = String.format("%02d%02d", minutes, seconds);
        Log.i(TAG, timerDuration);
        return timerDuration;
    }

    public void hideKeyboard() {
        // Code to hide the soft keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(etTimer.getApplicationWindowToken(), 0);
    }

    public void goTimerGoalActivity() {
        Intent i = new Intent(getActivity(), TimerGoalActivity.class);
        startActivity(i);
    }

    public String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(date);
        return formattedDate;
    }

    /* No longer need Activity Result Launcher
    // Create ActivityResultLauncher
    ActivityResultLauncher<Intent> timerGoalActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // There are no request codes
                        Intent data = result.getData();
                        tvGoal.setText(data.getStringExtra("GoalName"));
                    }
                }
            });

             // Launch activity to get result
        // timerGoalActivityResultLauncher.launch(i);
     */
}