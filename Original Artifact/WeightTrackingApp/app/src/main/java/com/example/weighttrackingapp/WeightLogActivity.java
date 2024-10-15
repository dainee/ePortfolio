package com.example.weighttrackingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WeightLogActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_WEIGHT = 1;
    private static final int REQUEST_CODE_SET_GOAL = 2;
    private static final String PREFS_NAME = "WeightTrackingAppPrefs";
    private static final String KEY_GOAL_WEIGHT = "goal_weight";

    private ListView weightLogList;
    private Button addWeightButton;
    private Button setGoalWeightButton;
    private Button smsNotificationButton;
    private TextView goalWeightTextView;
    private WeightLogAdapter adapter;
    private List<WeightLogEntry> weightLogEntries;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);

        weightLogList = findViewById(R.id.weight_log_list);
        addWeightButton = findViewById(R.id.add_weight_button);
        setGoalWeightButton = findViewById(R.id.set_goal_weight_button);
        smsNotificationButton = findViewById(R.id.sms_notification_button);
        goalWeightTextView = findViewById(R.id.goal_weight_text);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize weight log entries (this should be fetched from your database)
        weightLogEntries = new ArrayList<>();
        adapter = new WeightLogAdapter(this, weightLogEntries);
        weightLogList.setAdapter(adapter);

        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightLogActivity.this, WeightEntryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_WEIGHT);
            }
        });

        setGoalWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightLogActivity.this, GoalWeightActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SET_GOAL);
            }
        });

        smsNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightLogActivity.this, SMSNotificationActivity.class);
                startActivity(intent);
            }
        });

        // Load and display the goal weight
        loadGoalWeight();

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_ADD_WEIGHT) {
                float weight = data.getFloatExtra("weight", 0);
                String date = data.getStringExtra("date");
                addWeightEntry(new WeightLogEntry(weight, date));
                checkGoalAchievement(weight);
            } else if (requestCode == REQUEST_CODE_SET_GOAL) {
                float goalWeight = data.getFloatExtra("goalWeight", 0);
                saveGoalWeight(goalWeight);
                displayGoalWeight(goalWeight);
            }
        }
    }

    private void addWeightEntry(WeightLogEntry entry) {
        weightLogEntries.add(entry);
        adapter.notifyDataSetChanged();
    }

    private void checkGoalAchievement(float currentWeight) {
        float goalWeight = getGoalWeightFromDatabase();
        if (currentWeight <= goalWeight && goalWeight > 0) {
            NotificationHelper.sendGoalAchievementNotification(this);
        }
    }

    private float getGoalWeightFromDatabase() {
        return sharedPreferences.getFloat(KEY_GOAL_WEIGHT, 0);
    }

    private void saveGoalWeight(float goalWeight) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_GOAL_WEIGHT, goalWeight);
        editor.apply();
    }

    private void loadGoalWeight() {
        float goalWeight = sharedPreferences.getFloat(KEY_GOAL_WEIGHT, 0);
        displayGoalWeight(goalWeight);
    }

    private void displayGoalWeight(float goalWeight) {
        goalWeightTextView.setText(String.format("Goal Weight: %.1f lbs", goalWeight));
    }
}
