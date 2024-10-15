package com.example.weighttrackingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSNotificationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SEND_SMS = 1;
    private TextView smsStatusTextView;
    private Button requestSMSPermissionButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_notification);

        smsStatusTextView = findViewById(R.id.sms_status_text);
        requestSMSPermissionButton = findViewById(R.id.request_sms_permission_button);
        backButton = findViewById(R.id.back_button);

        requestSMSPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestSMSPermission();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkAndRequestSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_CODE_SEND_SMS);
        } else {
            sendSMSNotification();
        }
    }

    private void sendSMSNotification() {
        String phoneNumber = "1234567890"; // Replace with a valid phone number
        String message = "Congratulations! You have reached your goal weight.";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            smsStatusTextView.setText("SMS sent successfully");
            Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            smsStatusTextView.setText("Failed to send SMS");
            Toast.makeText(this, "SMS failed to send", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSNotification();
            } else {
                smsStatusTextView.setText("SMS permission denied");
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
