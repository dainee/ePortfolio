package com.example.weighttrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserDataManager {

    private static final String TAG = "UserDataManager";
    private static final String SALT = "your-salt"; // Use securely generated salt
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public UserDataManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Hash password using PBKDF2
    private String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashedPassword = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    // Save user with hashed password
    public void saveUser(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USERNAME, username);
            values.put(DatabaseHelper.COLUMN_PASSWORD, hashedPassword);

            long result = database.insert(DatabaseHelper.TABLE_USERS, null, values);
            if (result == -1) {
                Log.e(TAG, "Error saving user.");
            } else {
                Log.d(TAG, "User " + username + " created successfully.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saving user: " + e.getMessage());
        }
    }

    // Validate user credentials by comparing hashed passwords
    public boolean validateCredentials(String inputUsername, String inputPassword) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_PASSWORD},
                DatabaseHelper.COLUMN_USERNAME + " =?",
                new String[]{inputUsername}, null, null, null);

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
            try {
                return hashPassword(inputPassword).equals(storedPassword);
            } catch (Exception e) {
                Log.e(TAG, "Error validating user: " + e.getMessage());
            }
        }
        cursor.close();
        return false;
    }

    // Get user ID by username
    public int getUserId(String username) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID},
                DatabaseHelper.COLUMN_USERNAME + " =?",
                new String[]{username},
                null, null, null);

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
        }
        cursor.close();
        return userId;
    }

    // Save a new weight entry
    public void saveWeightEntry(int userId, float weight, String date) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WEIGHT, weight);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);

        long result = database.insert(DatabaseHelper.TABLE_WEIGHT_ENTRIES, null, values);
        if (result == -1) {
            Log.e(TAG, "Error saving weight entry.");
        } else {
            Log.d(TAG, "Weight entry added: " + weight + " lbs on " + date);
        }
    }

    // Get all weight entries for a user
    public Cursor getWeightEntries(int userId) {
        return database.query(DatabaseHelper.TABLE_WEIGHT_ENTRIES,
                new String[]{DatabaseHelper.COLUMN_ENTRY_ID, DatabaseHelper.COLUMN_WEIGHT, DatabaseHelper.COLUMN_DATE},
                DatabaseHelper.COLUMN_USER_ID_FK + " =?",
                new String[]{String.valueOf(userId)},
                null, null, DatabaseHelper.COLUMN_DATE + " DESC");
    }

    // Save goal weight for a user
    public void saveGoalWeight(int userId, float goalWeight) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_GOAL_WEIGHT, goalWeight);

        // Update the user's goal weight in the database
        int result = database.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_USER_ID + " =?", new String[]{String.valueOf(userId)});

        if (result > 0) {
            Log.d(TAG, "Goal weight for user ID " + userId + " updated to " + goalWeight + " lbs.");
        } else {
            Log.e(TAG, "Error updating goal weight for user ID " + userId);
        }
    }

    // Get goal weight for a user
    public float getGoalWeight(int userId) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_GOAL_WEIGHT},
                DatabaseHelper.COLUMN_USER_ID + " =?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        float goalWeight = 0;
        if (cursor.moveToFirst()) {
            goalWeight = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOAL_WEIGHT));
        }
        cursor.close();
        return goalWeight;
    }

    // Delete a weight entry
    public void deleteWeightEntry(int entryId) {
        int result = database.delete(DatabaseHelper.TABLE_WEIGHT_ENTRIES,
                DatabaseHelper.COLUMN_ENTRY_ID + " =?",
                new String[]{String.valueOf(entryId)});
        if (result > 0) {
            Log.d(TAG, "Weight entry with ID " + entryId + " deleted successfully.");
        } else {
            Log.e(TAG, "Error deleting weight entry with ID " + entryId);
        }
    }
}
