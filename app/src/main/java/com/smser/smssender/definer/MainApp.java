package com.smser.smssender.definer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.smser.smssender.R;
import com.smser.smssender.dbmanager.DataBaseHandler;
import com.smser.smssender.dbmanager.DatabaseManager;

public class MainApp extends Application {
    private static Context context;
    private static DataBaseHandler dbHandler;
    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeApp(getApplicationContext());
    }

    public static void initializeApp(Context cxt) {
        context = cxt;

        dbHandler = new DataBaseHandler(context);
        dbHandler.getWritableDatabase();
        preferences = cxt.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        DatabaseManager.initializeInstance(dbHandler);
        Log.e("MainApp", "executed");
    }

    public static Context getContext() {
        return context;
    }

    public static DataBaseHandler getDbHandler() {
        if (dbHandler == null) {
            return dbHandler = new DataBaseHandler(getContext());
        } else {
            return dbHandler;
        }
    }

    public static void storeValue(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public static void removeValue(String key) {
        preferences.edit().remove(key).apply();
    }

    public static String getValue(String key) {
        return preferences.getString(key, "");
    }
}
