package com.example.weighttrackingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeightTrackingApp.db";
    private static final int DATABASE_VERSION = 2;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_WEIGHT_ENTRIES = "weight_entries";

    // Column names for USERS table
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_GOAL_WEIGHT = "goal_weight";

    // Column names for WEIGHT_ENTRIES table
    public static final String COLUMN_ENTRY_ID = "entry_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USER_ID_FK = "user_id_fk"; // Foreign key to USERS

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create USERS table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_GOAL_WEIGHT + " REAL" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create WEIGHT_ENTRIES table
        String CREATE_WEIGHT_ENTRIES_TABLE = "CREATE TABLE " + TABLE_WEIGHT_ENTRIES + "("
                + COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_USER_ID_FK + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" + ")";
        db.execSQL(CREATE_WEIGHT_ENTRIES_TABLE);

        // Add index to speed up username queries
        String CREATE_USER_INDEX = "CREATE INDEX idx_username ON " + TABLE_USERS + "(" + COLUMN_USERNAME + ")";
        db.execSQL(CREATE_USER_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_ENTRIES);
        onCreate(db);
    }
}
