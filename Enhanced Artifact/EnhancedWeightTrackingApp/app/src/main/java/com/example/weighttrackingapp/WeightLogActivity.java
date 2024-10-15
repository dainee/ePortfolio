package com.example.weighttrackingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WeightLogActivity extends AppCompatActivity {

    private ListView weightLogList;
    private Button addWeightButton, setGoalWeightButton;
    private TextView goalWeightTextView;
    private WeightLogAdapter adapter;
    private List<WeightLogEntry> weightLogEntries;
    private UserDataManager userDataManager;
    private int userId;  // Get the user ID of the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);

        weightLogList = findViewById(R.id.weight_log_list);
        addWeightButton = findViewById(R.id.add_weight_button);
        setGoalWeightButton = findViewById(R.id.set_goal_weight_button);
        goalWeightTextView = findViewById(R.id.goal_weight_text);
        userDataManager = new UserDataManager(this);

        // Get the userId passed from MainActivity
        userId = getIntent().getIntExtra("userId", -1);

        // Check if userId is valid
        if (userId == -1) {
            Toast.makeText(this, "Invalid user. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize the list and adapter
        weightLogEntries = new ArrayList<>();
        adapter = new WeightLogAdapter(this, weightLogEntries, userDataManager);
        weightLogList.setAdapter(adapter);

        addWeightButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeightLogActivity.this, WeightEntryActivity.class);
            intent.putExtra("userId", userId);  // Pass userId to WeightEntryActivity
            startActivityForResult(intent, 100);
        });

        setGoalWeightButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeightLogActivity.this, GoalWeightActivity.class);
            intent.putExtra("userId", userId); // Pass userId to GoalWeightActivity
            startActivityForResult(intent, 101);
        });

        // Load weight entries and goal weight from database
        loadGoalWeight();
        loadWeightEntries();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                loadWeightEntries();  // Reload weight entries after adding
            } else if (requestCode == 101) {
                loadGoalWeight();  // Reload goal weight after setting
            }
        }
    }

    private void loadGoalWeight() {
        float goalWeight = userDataManager.getGoalWeight(userId);
        if (goalWeight > 0) {
            goalWeightTextView.setText(String.format("Goal Weight: %.1f lbs", goalWeight));
        } else {
            goalWeightTextView.setText("Goal Weight: Not set");
        }
    }

    private void loadWeightEntries() {
        weightLogEntries.clear();

        Cursor cursor = userDataManager.getWeightEntries(userId);
        if (cursor.moveToFirst()) {
            do {
                int entryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ENTRY_ID));
                float weight = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEIGHT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                weightLogEntries.add(new WeightLogEntry(entryId, weight, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void displayWeightTrend() {
        WeightTrendPredictor predictor = new WeightTrendPredictor();
        List<Float> weightValues = getWeightValuesFromEntries(weightLogEntries); // Extract weight values
        float trend = predictor.calculateMovingAverage(weightValues, 7); // Example: 7-day trend
        if (trend != -1) {
            goalWeightTextView.setText(String.format("7-Day Trend: %.1f lbs", trend));
        } else {
            goalWeightTextView.setText("Not enough data for trend");
        }
    }

    // Helper method to extract weight values from weight entries
    private List<Float> getWeightValuesFromEntries(List<WeightLogEntry> entries) {
        List<Float> weights = new ArrayList<>();
        for (WeightLogEntry entry : entries) {
            weights.add(entry.getWeight());
        }
        return weights;
    }
}
