package com.example.productiv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.productiv.R;
import com.example.productiv.fragments.CalendarFragment;
import com.example.productiv.fragments.GoalsFragment;
import com.example.productiv.fragments.ShopFragment;
import com.example.productiv.fragments.TimerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageView ivSettings;
    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private int menuToChoose = R.menu.menu_bottom_navigation;
    public static final String TAG = "MainActivity";

    // SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        ivSettings = findViewById(R.id.ivSettings);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Define fragments
        final Fragment goalsFragment = new GoalsFragment();
        final Fragment timerFragment = new TimerFragment();
        final Fragment calendarFragment = new CalendarFragment();
        final Fragment shopFragment = new ShopFragment();

        // Handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()) {
                    case R.id.action_goals:
                        fragment = goalsFragment;
                        break;
                    case R.id.action_timer:
                        fragment = timerFragment;
                        break;
                    case R.id.action_calendar:
                        fragment = calendarFragment;
                        break;
                    case R.id.action_shop:
                        fragment = shopFragment;
                        break;
                    default:
                        fragment = timerFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.rlContainer, fragment).commit();
                return true;
            }
        });

        // Set default
        bottomNavigationView.setSelectedItemId(R.id.action_timer);

        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSettingsActivity();
            }
        });
    }

    public void goSettingsActivity() {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }
}