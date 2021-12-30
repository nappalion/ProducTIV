package com.example.productiv.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.productiv.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerFragment extends Fragment {

    private TextView tvTimer;
    private ToggleButton btnPlay;
    public static final String TAG = "TimerFragment";

    // Initialize timer duration
    long startTime;
    CountDownTimer countDownTimer;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTime = TimeUnit.MINUTES.toMillis(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countDownTimer != null) countDownTimer.cancel();

        startContinueTimer();
    }

    //
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTimer = getView().findViewById(R.id.tvTimer);
        btnPlay = getView().findViewById(R.id.btnPlay);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPlay.isChecked()) {

                }
                else {

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    private void startContinueTimer() {
        countDownTimer = new CountDownTimer(startTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // When tick
                // Convert millisecond to minute and second
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                String timerDuration = String.format("%02d:%02d", minutes, seconds);
                Log.i(TAG, timerDuration);
                // Set converted string on text view
                tvTimer.setText(timerDuration);
                startTime = millisUntilFinished;

                Log.i(TAG, String.valueOf(startTime));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getContext(), "Countdown timer has ended", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
}