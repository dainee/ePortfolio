package com.example.weighttrackingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GoalWeightActivity extends AppCompatActivity {

    private EditText goalWeightInput;
    private Button submitGoalWeightButton;
    private UserDataManager userDataManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_weight);

        goalWeightInput = findViewById(R.id.goal_weight_input);
        submitGoalWeightButton = findViewById(R.id.submit_goal_weight_button);
        userDataManager = new UserDataManager(this);

        // Get userId from intent
        userId = getIntent().getIntExtra("userId", -1);

        submitGoalWeightButton.setOnClickListener(v -> {
            String goalWeightText = goalWeightInput.getText().toString();
            if (!goalWeightText.isEmpty()) {
                try {
                    float goalWeight = Float.parseFloat(goalWeightText);
                    userDataManager.saveGoalWeight(userId, goalWeight);
                    Toast.makeText(GoalWeightActivity.this, "Goal weight set", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(GoalWeightActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            } else {
                goalWeightInput.setError("Please enter your goal weight");
            }
        });
    }
}
