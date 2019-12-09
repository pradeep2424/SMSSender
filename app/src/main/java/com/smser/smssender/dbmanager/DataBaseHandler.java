package com.smser.smssender.dbmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smser.smssender.BuildConfig;
import com.smser.smssender.dbmanager.dbidentifier.MasterData;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static String DB_NAME = BuildConfig.DB_NAME;
    private static int DB_VERSION = BuildConfig.DB_VER;

    public DataBaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MasterData.createBlockList());
        db.execSQL(MasterData.createSentList());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
