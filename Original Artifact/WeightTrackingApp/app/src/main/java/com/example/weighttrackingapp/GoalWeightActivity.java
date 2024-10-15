package com.example.weighttrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class GoalWeightActivity extends AppCompatActivity {

    private EditText goalWeightInput;
    private Button submitGoalWeightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_weight);

        goalWeightInput = findViewById(R.id.goal_weight_input);
        submitGoalWeightButton = findViewById(R.id.submit_goal_weight_button);

        submitGoalWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float goalWeight = Float.parseFloat(goalWeightInput.getText().toString());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("goalWeight", goalWeight);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
