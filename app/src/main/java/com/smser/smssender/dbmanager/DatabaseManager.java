package com.smser.smssender.dbmanager;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smser.smssender.BuildConfig;
import com.smser.smssender.definer.MainApp;

public class DatabaseManager {

    private static final String DB_NAME = BuildConfig.DB_NAME;

    private static DatabaseManager instance;

    private static SQLiteOpenHelper mDatabaseHelper;

    private Integer mOpenCounter = 0;

    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();


            mDatabaseHelper = helper;
        }
    }

    public static synchronized boolean deleteDB() {

        boolean d = MainApp.getContext().deleteDatabase(DB_NAME);

        if (d)
            instance = null;

        return d;

    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            MainApp.initializeApp(MainApp.getContext());
        }

        return instance;
    }

    public synchronized SQLiteDatabase openReadableDatabase() {
        mOpenCounter += 1;
        if (mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getReadableDatabase();
        }
        return mDatabase;
    }

    public synchronized SQLiteDatabase openWritableDatabase() {
        mOpenCounter += 1;
        if (mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter -= 1;
        if (mOpenCounter == 0 &&
                mDatabase != null) {
            // Closing database
            mDatabase.close();
        }

    }
}
