package com.example.android.inventoryappuda.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Анастасия on 27.07.2018.
 */

public class Booksdb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "bookstore.db";

    public Booksdb (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BooksContract.BooksEntry.TABLE_NAME + " (" +
                    BooksContract.BooksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BooksContract.BooksEntry.BOOKNAME + " TEXT NOT NULL," +
                    BooksContract.BooksEntry.PRICE + " INTEGER NOT NULL," +
                    BooksContract.BooksEntry.QUANTITY + " INTEGER NOT NULL," +
                    BooksContract.BooksEntry.SUPPLIER + " TEXT," +
                    BooksContract.BooksEntry.PHONESUP + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BooksContract.BooksEntry.TABLE_NAME;
}
