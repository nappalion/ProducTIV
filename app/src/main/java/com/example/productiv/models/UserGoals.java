package com.example.productiv.models;


public class UserGoals {
    private String goalName;
    private String repeat;
    private long dailyGoal;
    private long currentTime;

    public UserGoals() { }

    public UserGoals(String goalName, String repeat, long dailyGoal, long currentTime) {
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

    public void setDailyGoal(long dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getRepeat() {
        return repeat;
    }

    public long getDailyGoal() {
        return dailyGoal;
    }

    public long getCurrentTime() {
        return currentTime;
    }
}
