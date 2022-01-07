package com.example.productiv.models;

public class GoalHistory {

    private long currentTime;
    private int coinsEarned;
    private boolean isComplete;

    public GoalHistory (long currentTime, int coinsEarned, boolean isComplete) {
        this.currentTime = currentTime;
        this.coinsEarned = coinsEarned;
        this.isComplete = isComplete;
    }

    public void setCoinsEarned(int coinsEarned) {
        this.coinsEarned = coinsEarned;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public boolean getIsComplete() {
        return isComplete;
    }
}
