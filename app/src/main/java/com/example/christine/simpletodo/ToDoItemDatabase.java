package com.example.christine.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Christine on 6/16/2016.
 */
public class ToDoItemDatabase extends SQLiteOpenHelper {
    private static final String TAG = "ToDoItemDatabase";

    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITEMS = "items";

    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_PRIORITY = "priority";

    private static ToDoItemDatabase sInstance;

    private ToDoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton pattern - only one databse to prevent memory leaks and unnecessary reallocations
    /* synchronized:
     * 1. Not possible for 2 invocations of of synchronized methods to interleave,
     * 2. Changes to the state of the object are visible to all threads
     */
    public static synchronized ToDoItemDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ToDoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the tables
        String ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" + KEY_ITEM_NAME + " TEXT PRIMARY KEY, " +
                      KEY_DUE_DATE + "TEXT, " +
                      KEY_PRIORITY + "TEXT);";

        db.execSQL(ITEMS_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // SQL for upgrading the tables
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }

    }

    public void addItem(Item item){
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, item.name);
            values.put(KEY_DUE_DATE, item.dueDate);
            values.put(KEY_PRIORITY, item.priority);

            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add item to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Item> getAllItems(){
        ArrayList<Item> items = new ArrayList<Item>();

        String itemName;
        String dueDate;
        String priority;

        String GET_ITEMS_QUERY = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        //Provides random read-write access to the result set returned by db query
        Cursor cursor = db.rawQuery(GET_ITEMS_QUERY, null);

        try{
            if (cursor.moveToFirst()){
                do{
                    itemName = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME));
                    dueDate = cursor.getString(cursor.getColumnIndex(KEY_DUE_DATE));
                    priority = cursor.getString(cursor.getColumnIndex(KEY_PRIORITY));
                    Item item = new Item(itemName, dueDate, priority);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                //Closes the Cursor, releasing all of its resources and making it completely invalid
                cursor.close();
            }

            db.endTransaction();
        }
        return items;
    }

    public void updateItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, item.name);
            values.put(KEY_DUE_DATE, item.dueDate);
            values.put(KEY_PRIORITY, item.priority);

            // Don't allow duplicate items with the same name
            db.update(TABLE_ITEMS, values, KEY_ITEM_NAME + "= ?", new String[]{item.name});

            // Insert if item didn't already exist
            /*
            if (rows != 1){
                db.insertOrThrow(TABLE_ITEMS, null, values);
            }
            */
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update items");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{
            db.delete(TABLE_ITEMS, KEY_ITEM_NAME + "= ?", new String[]{item.name});
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Error while trying to get delete item");
        } finally {
            db.endTransaction();
        }
    }
}