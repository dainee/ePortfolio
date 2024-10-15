package com.example.weighttrackingapp;

public class WeightLogEntry {
    private int entryId;
    private float weight;
    private String date;

    public WeightLogEntry(int entryId, float weight, String date) {
        this.entryId = entryId;
        this.weight = weight;
        this.date = date;
    }

    public int getEntryId() {
        return entryId;
    }

    public float getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }
}
