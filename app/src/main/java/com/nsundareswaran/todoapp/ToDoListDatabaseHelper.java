package com.nsundareswaran.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nsundareswaran on 7/5/16.
 */
public class ToDoListDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todoList";
    private static final int DB_VERSION = 1;
    public static final String COLUMN_ITEM_NAME = "TODO_ITEM_NAME";
    public static final String COLUMN_ID = "_id";
    public static final String DB_TABLE_NAME = "TODOS";
    private static ToDoListDatabaseHelper sInstance = null;
    private static SQLiteDatabase db = null;

    public ToDoListDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE "+DB_TABLE_NAME+" ("
                                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + COLUMN_ITEM_NAME + " TEXT);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static ToDoListDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            try {
                sInstance = new ToDoListDatabaseHelper(context);
                db = sInstance.getWritableDatabase();
            } catch (SQLiteException e) {
                System.out.println("Db initialization error");
                return null;
            }
        }
        return sInstance;
    }

    public Cursor getCursor() {
        Cursor cursor = null;
        try {
            cursor = db.query(DB_TABLE_NAME,
                    new String[] {COLUMN_ID, COLUMN_ITEM_NAME},
                    null,
                    null,
                    null,
                    null,
                    COLUMN_ITEM_NAME+" ASC");
//            cursor.moveToFirst();
//            if (cursor.moveToFirst()) {
//                do {
//                    String itemName = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME));
//                    tasks.add(itemName);
//                } while (cursor.moveToNext());
//            }

        } catch (SQLiteException e) {
            System.out.println("Db initialization error");
        }

        return cursor;
    }

    public void addDbEntry (String task) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ITEM_NAME, task);
            db.insertOrThrow(DB_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            System.out.println("Db insert error");
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }
    }

    public void deleteEntry (int id) {
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            int i = db.delete(DB_TABLE_NAME, COLUMN_ID+ " = ? ", new String[] {Integer.toString(id)});
//            System.out.println("i = "+i);
            if (i > 0) {
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            System.out.println("Db delete error");
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }
    }

    public void updateEntry (String oldTask, String newTask) {
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ITEM_NAME, newTask);
            db.update(DB_TABLE_NAME, values, COLUMN_ITEM_NAME+ " = ? ", new String[] {oldTask});
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            System.out.println("Db update error");
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }
    }
}
