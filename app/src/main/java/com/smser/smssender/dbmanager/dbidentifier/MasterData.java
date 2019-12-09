package com.smser.smssender.dbmanager.dbidentifier;

import android.util.Log;

public class MasterData {

    public static final String TABLE_BLOCK_LIST = "Tbl_Block_List";
    public static final String BLOCK_NUMBER = "Block_Number";
    public static final String BLOCK_NAME = "Block_Name";

    public static final String TABLE_SENT_LIST = "Tbl_Sent_List";
    public static final String SENT_NUMBER = "Sent_Number";
//    public static final String SENT_NAME = "Sent_Name";
    public static final String SENT_COUNT = "Sent_Count";

    public static String createBlockList = null;
    public static String insertBlockList = null;
    public static String createSentList = null;
    public static String insertSentList = null;

    public static String createBlockList() {
        try {
            createBlockList = "CREATE TABLE IF NOT EXISTS " + TABLE_BLOCK_LIST + " ( "
                    + BLOCK_NUMBER + " TEXT, "
                    + BLOCK_NAME + " TEXT )";

        } catch (Exception e) {
            Log.e("createBlockList", e.toString());
        }

        return createBlockList;
    }

    public static String insertBlockList() {
        try {
            insertBlockList = "INSERT INTO " + TABLE_BLOCK_LIST + " ( "
                    + BLOCK_NUMBER + ", "
                    + BLOCK_NAME
                    + ") VALUES (?,?)";

        } catch (Exception e) {
            Log.e("insertBlockList", e.toString());
        }

        return insertBlockList;
    }

    public static String createSentList() {
        try {
            createSentList = "CREATE TABLE IF NOT EXISTS " + TABLE_SENT_LIST + " ( "
                    + SENT_NUMBER + " TEXT, "
//                    + SENT_NAME + " TEXT, "
                    + SENT_COUNT + " TEXT )";

        } catch (Exception e) {
            Log.e("createSentList", e.toString());
        }

        return createSentList;
    }

    public static String insertSentList() {
        try {
            insertSentList = "INSERT INTO " + TABLE_SENT_LIST + " ( "
                    + SENT_NUMBER + ", "
//                    + SENT_NAME + ", "
                    + SENT_COUNT
                    + ") VALUES (?,?)";

        } catch (Exception e) {
            Log.e("insertSentList", e.toString());
        }

        return insertSentList;
    }

}
