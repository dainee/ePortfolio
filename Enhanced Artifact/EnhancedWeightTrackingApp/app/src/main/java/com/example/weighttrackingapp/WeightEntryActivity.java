package com.example.weighttrackingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
// import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeightEntryActivity extends AppCompatActivity {

    private EditText weightInput;
    private Button submitWeightButton;
    private UserDataManager userDataManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_entry);

        weightInput = findViewById(R.id.weight_input);
        submitWeightButton = findViewById(R.id.submit_weight_button);
        userDataManager = new UserDataManager(this);

        // Get userId from intent
        userId = getIntent().getIntExtra("userId", -1);

        submitWeightButton.setOnClickListener(v -> {

            String weightText = weightInput.getText().toString();
            if (!weightText.isEmpty()) {
                try {
                    float weight = Float.parseFloat(weightText);
                    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

                    userDataManager.saveWeightEntry(userId, weight, date);
                    Toast.makeText(WeightEntryActivity.this, "Weight entry added", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(WeightEntryActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            } else {
                weightInput.setError("Please enter your weight");
            }
        });
    }
}
