package com.example.weighttrackingapp;

import java.util.List;

public class WeightTrendPredictor {
    // Simple moving average to predict weight trend
    public float calculateMovingAverage(List<Float> weights, int period) {
        if (weights == null || weights.size() < period) {
            return -1; // Not enough data
        }

        float sum = 0;
        for (int i = weights.size() - period; i < weights.size(); i++) {
            sum += weights.get(i);
        }
        return sum / period;
    }
}
