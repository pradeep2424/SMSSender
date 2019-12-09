package com.smser.smssender.dbmanager.dbidentifier;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.smser.smssender.comman.Utilities;
import com.smser.smssender.dbmanager.DatabaseManager;
import com.smser.smssender.model.BlockData;

import java.util.ArrayList;

import static com.smser.smssender.dbmanager.dbidentifier.MasterData.BLOCK_NAME;
import static com.smser.smssender.dbmanager.dbidentifier.MasterData.BLOCK_NUMBER;
import static com.smser.smssender.dbmanager.dbidentifier.MasterData.SENT_COUNT;
import static com.smser.smssender.dbmanager.dbidentifier.MasterData.SENT_NUMBER;
import static com.smser.smssender.dbmanager.dbidentifier.MasterData.TABLE_BLOCK_LIST;
import static com.smser.smssender.dbmanager.dbidentifier.MasterData.TABLE_SENT_LIST;

public class MasterCaller {

    private static Cursor cursor;

    public static ArrayList<BlockData> getBlockList() {
        ArrayList<BlockData> blockList = null;

        SQLiteDatabase database = DatabaseManager.getInstance().openReadableDatabase();
        try {
            database.beginTransaction();

            cursor = database.rawQuery("SELECT * FROM " + TABLE_BLOCK_LIST, null);

            if (cursor != null && cursor.getCount() > 0) {
                blockList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    BlockData blockData = new BlockData();
                    blockData.setBlockNumber(cursor.getString(cursor.getColumnIndex(BLOCK_NUMBER)));
                    blockData.setBlockName(cursor.getString(cursor.getColumnIndex(BLOCK_NAME)));

                    blockList.add(blockData);
                }
            }
        } catch (Exception e) {
            Log.e("getBlockList", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return blockList;
    }

//    public static ArrayList<SentData> getSentList() {
//        ArrayList<SentData> sentList = null;
//
//        SQLiteDatabase database = DatabaseManager.getInstance().openReadableDatabase();
//        try {
//            database.beginTransaction();
//
//            cursor = database.rawQuery("SELECT * FROM " + TABLE_SENT_LIST, null);
//
//            if (cursor != null && cursor.getCount() > 0) {
//                sentList = new ArrayList<>();
//
//                while (cursor.moveToNext()) {
//                    SentData sentData = new SentData();
//                    sentData.setSentNumber(cursor.getString(cursor.getColumnIndex(SENT_NUMBER)));
//                    sentData.setSentCount(cursor.getString(cursor.getColumnIndex(SENT_COUNT)));
//
//                    sentList.add(sentData);
//                }
//            }
//        } catch (Exception e) {
//            Log.e("getSentList", e.toString());
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            database.endTransaction();
//            DatabaseManager.getInstance().closeDatabase();
//        }
//
//        return sentList;
//    }

    public static BlockData getBlockData(String number) {
        BlockData blockData = null;

        SQLiteDatabase database = DatabaseManager.getInstance().openReadableDatabase();
        try {
            database.beginTransaction();

            cursor = database.rawQuery("SELECT * FROM " + TABLE_BLOCK_LIST + " WHERE " +
                    BLOCK_NUMBER + " = ?", new String[]{number});

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    blockData = new BlockData();
                    blockData.setBlockNumber(cursor.getString(cursor.getColumnIndex(BLOCK_NUMBER)));
                    blockData.setBlockName(cursor.getString(cursor.getColumnIndex(BLOCK_NAME)));
                }
            }
        } catch (Exception e) {
            Log.e("getBlockData", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return blockData;
    }

    public static int getSentData(String number) {
        int count = 0;

        SQLiteDatabase database = DatabaseManager.getInstance().openReadableDatabase();
        try {
            database.beginTransaction();

            cursor = database.rawQuery("SELECT * FROM " + TABLE_SENT_LIST + " WHERE " +
                    SENT_NUMBER + " = ?", new String[]{number});

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    count = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SENT_COUNT)));
//                    sentData = new SentData();
//                    sentData.setSentNumber(cursor.getString(cursor.getColumnIndex(SENT_NUMBER)));
//                    sentData.setSentName(cursor.getString(cursor.getColumnIndex(SENT_NAME)));
//                    sentData.setSentCount(cursor.getString(cursor.getColumnIndex(SENT_COUNT)));
                }
            }
        } catch (Exception e) {
            Log.e("getSentData", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return count;
    }

//    public static void insertBlockData(BlockData blockData) {
//        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
//        try {
//            database.beginTransaction();
//
//            SQLiteStatement statement = database.compileStatement(MasterData.insertBlockList());
//            statement.clearBindings();
//
//            statement.bindString(1, blockData.getBlockNumber());
//            statement.bindString(2, blockData.getBlockName());
//
//            statement.executeInsert();
//
//            statement.close();
//            database.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("insertBlockData", e.toString());
//        } finally {
//            database.endTransaction();
//            DatabaseManager.getInstance().closeDatabase();
//        }
//    }

    public static void insertBlockList(ArrayList<BlockData> blockList) {
        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
        try {
            database.beginTransaction();

            SQLiteStatement statement = database.compileStatement(MasterData.insertBlockList());
            for (BlockData data : blockList) {

                statement.bindString(1, data.getBlockNumber());
                statement.bindString(2, data.getBlockName());

                statement.executeInsert();
            }

            statement.close();
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("insertBlockList", e.toString());
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public static void insertUpdateSentData(String number) {
        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
        try {
            database.beginTransaction();

            cursor = database.rawQuery("SELECT * FROM " + TABLE_SENT_LIST + " WHERE " +
                    SENT_NUMBER + " = ?", new String[]{number});

            if (cursor != null && cursor.getCount() > 0) {

                cursor.moveToFirst();
//                String value = cursor.getString(cursor.getColumnIndex(SENT_COUNT));

                int count = Utilities.numberConverter(cursor.getString(cursor.getColumnIndex(SENT_COUNT)));

                count = count + 1;

//                if (value.equals("")) {
//                    count = 1;
//                } else {
//                    count = Integer.parseInt(value) + 1;
//                }

                String query = "UPDATE " + TABLE_SENT_LIST + " SET " + SET() + " WHERE " + SENT_NUMBER + " = ?;";

                SQLiteStatement statement = database.compileStatement(query);
                statement.clearBindings();

                statement.bindString(1, "" + count);
                statement.bindString(2, number);

                statement.execute();
                statement.close();

                Log.e("insertUpdateSentData", number + " updated " + count);
            } else {

                SQLiteStatement statement = database.compileStatement(MasterData.insertSentList());
                statement.clearBindings();

                statement.bindString(1, number);
                statement.bindString(2, "" + 1);

                statement.executeInsert();

                statement.close();

                Log.e("insertUpdateSentData", number + " inserted " + 1);
            }

            database.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("insertUpdateSentData", e.toString());
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public static void clearBlockList() {
        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
        try {

            database.execSQL("DELETE FROM " + TABLE_BLOCK_LIST);

            Log.e("clearBlockList", "BlockList cleared");

        } catch (Exception e) {
            Log.e("clearBlockList", e.toString());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public static void clearSentList() {
        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
        try {

            database.execSQL("DELETE FROM " + TABLE_SENT_LIST);

            Log.e("clearSentList", "SentList cleared");

        } catch (Exception e) {
            Log.e("clearSentList", e.toString());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

//    public static void removeBlockData(String number) {
//        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
//        try {
//            database.beginTransaction();
//
//            database.execSQL("DELETE FROM " + TABLE_BLOCK_LIST + " WHERE " + BLOCK_NUMBER + " = '" + number + "'");
//
//            Log.e("removeBlockData", "BlockData removed");
//
//        } catch (Exception e) {
//            Log.e("removeBlockData", e.toString());
//        } finally {
//            database.endTransaction();
//            DatabaseManager.getInstance().closeDatabase();
//        }
//    }

//    public static void removeSentData(String number) {
//        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
//        try {
//            database.beginTransaction();
//
//            database.execSQL("DELETE FROM " + TABLE_SENT_LIST + " WHERE " + SENT_NUMBER + " = '" + number + "'");
//
//            Log.e("removeSentData", "SentData removed");
//
//        } catch (Exception e) {
//            Log.e("removeSentData", e.toString());
//        } finally {
//            database.endTransaction();
//            DatabaseManager.getInstance().closeDatabase();
//        }
//    }

    public static void updateSentData(String number) {
        SQLiteDatabase database = DatabaseManager.getInstance().openWritableDatabase();
        try {
            database.beginTransaction();

            cursor = database.rawQuery("SELECT * FROM " + TABLE_SENT_LIST + " WHERE " + SENT_NUMBER + " = ?",
                    new String[]{number});

            if (cursor != null && cursor.getCount() > 0) {
                String query = "UPDATE " + TABLE_SENT_LIST + " SET " + SET() + " WHERE " + SENT_NUMBER + " = ?;";

                SQLiteStatement statement = database.compileStatement(query);
                statement.clearBindings();

                statement.bindString(1, "" + 0);
                statement.bindString(2, number);

                statement.execute();
                statement.close();
            }

            database.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("updateSentData", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private static String SET() {
        return MasterData.SENT_COUNT + " = " + "?" + "";
    }
}
