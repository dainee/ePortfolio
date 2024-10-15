package com.example.weighttrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeightEntryActivity extends AppCompatActivity {

    private EditText weightInput;
    private Button submitWeightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_entry);

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);

        weightInput = findViewById(R.id.weight_input);
        submitWeightButton = findViewById(R.id.submit_weight_button);

        submitWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(weightInput.getText().toString());
                String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("weight", weight);
                resultIntent.putExtra("date", date);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
