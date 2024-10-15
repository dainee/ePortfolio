package com.example.weighttrackingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        createAccountButton = findViewById(R.id.create_account_button);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
