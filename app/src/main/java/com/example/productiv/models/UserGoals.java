package com.example.productiv.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserGoals {
    private String goalName;
    private String repeat;
    private int dailyGoal;
    private int currentTime;

    public UserGoals() { }

    public UserGoals(String goalName, String repeat, int dailyGoal, int currentTime) {
        this.goalName = goalName;
        this.repeat = repeat;
        this.dailyGoal = dailyGoal;
    }


    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getRepeat() {
        return repeat;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public int getCurrentTime() {
        return currentTime;
    }
}
