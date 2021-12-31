package com.example.productiv.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.productiv.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerFragment extends Fragment {

    private EditText etTimer;
    private ToggleButton btnPlay;
    public static final String TAG = "TimerFragment";

    // Initialize timer duration
    long setTime = TimeUnit.SECONDS.toMillis(10);
    long startTime;
    boolean isPaused;
    CountDownTimer countDownTimer;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getActivity().getSharedPreferences("Timer", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Called onResume()");

        // SharedPreferences remembers startTime and isPaused after app is killed
        startTime = sharedPreferences.getLong("startTime", setTime);
        isPaused = sharedPreferences.getBoolean("isPaused", isPaused);
        // Log.i(TAG, "Getting startTime: " + sharedPreferences.getLong("startTime", setTime));

        // Deletes countDownTimer if doesn't exist
        if (countDownTimer != null) countDownTimer.cancel();

        startContinueTimer();

        // If paused before exiting activity, app remembers and pauses the timer
        if (isPaused) {
            timerPause();
            btnPlay.setChecked(true);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTimer = getView().findViewById(R.id.etTimer);
        btnPlay = getView().findViewById(R.id.btnPlay);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPlay.isChecked()) {
                    timerPause();
                    editor.putBoolean("isPaused", true);
                }
                else {
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

                // Set converted string on text view
                etTimer.setText(calculateDuration(millisUntilFinished));
                startTime = millisUntilFinished;

                editor.putLong("startTime", startTime);
                editor.apply();

                Log.i(TAG, String.valueOf(startTime));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getContext(), "Countdown timer has ended", Toast.LENGTH_SHORT).show();
                startTime = setTime;
                etTimer.setText(calculateDuration(startTime));
                // Log.i(TAG, "New startTime: " + startTime + " Taken from setTime: " + setTime);
                countDownTimer.cancel();
                btnPlay.setChecked(true);
            }
        }.start();
    }

    public String calculateDuration(long time) {
        // Convert millisecond to minute and second
        int seconds = (int) (time / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String timerDuration = String.format("%02d:%02d", minutes, seconds);
        Log.i(TAG, timerDuration);
        return timerDuration;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        // Log.i(TAG, "Called onCreateView");

        return rootView;
    }


}