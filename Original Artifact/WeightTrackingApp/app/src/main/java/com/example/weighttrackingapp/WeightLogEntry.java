package com.example.weighttrackingapp;

public class WeightLogEntry {
    private float weight;
    private String date;
    private boolean isGoalWeight;

    public WeightLogEntry(float weight, String date) {
        this.weight = weight;
        this.date = date;
        this.isGoalWeight = false;
    }

    public WeightLogEntry(float weight, String date, boolean isGoalWeight) {
        this.weight = weight;
        this.date = date;
        this.isGoalWeight = isGoalWeight;
    }

    public float getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public boolean isGoalWeight() {
        return isGoalWeight;
    }
}
