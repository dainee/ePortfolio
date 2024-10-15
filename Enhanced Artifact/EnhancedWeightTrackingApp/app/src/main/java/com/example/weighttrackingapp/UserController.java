package com.example.weighttrackingapp;

import android.content.Context;

public class UserController {
    private UserDataManager userDataManager;

    public UserController(Context context) {
        userDataManager = new UserDataManager(context);
    }

    public boolean createUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false; // Input validation
        }
        userDataManager.saveUser(username, password);
        return true;
    }

    public boolean validateUser(String username, String password) {
        return userDataManager.validateCredentials(username, password);
    }

    public void saveGoalWeight(int userId, float goalWeight) {
        userDataManager.saveGoalWeight(userId, goalWeight);
    }
}
