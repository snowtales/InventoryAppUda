package com.example.android.inventoryappuda;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.inventoryappuda.database.BooksContract;
import com.example.android.inventoryappuda.database.Booksdb;

public class MainActivity extends AppCompatActivity {

    private static final String STRINGTAG = "MainActivity.class";
    private Booksdb mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new Booksdb(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        insertBook();
        displayDatabaseInfo();
    }

    public void insertBook(){
        Booksdb mDbHelper = new Booksdb(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.BOOKNAME, "War and Peace");
        values.put(BooksContract.BooksEntry.QUANTITY, 7);
        values.put(BooksContract.BooksEntry.PRICE, 29);
        values.put(BooksContract.BooksEntry.SUPPLIER, "everywhere");
        values.put(BooksContract.BooksEntry.PHONESUP, 123456789);
       db.insert(BooksContract.BooksEntry.TABLE_NAME, null, values);
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {
                BooksContract.BooksEntry._ID,
                BooksContract.BooksEntry.BOOKNAME,
                BooksContract.BooksEntry.PRICE,
                BooksContract.BooksEntry.QUANTITY,
                BooksContract.BooksEntry.SUPPLIER,
                BooksContract.BooksEntry.PHONESUP
        };
        Cursor cursor = db.query(BooksContract.BooksEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        try {
            Log.i(STRINGTAG,"The books table contains " + cursor.getCount() + " books.\n\n");
            Log.i(STRINGTAG, BooksContract.BooksEntry._ID + " - " +
                    BooksContract.BooksEntry.BOOKNAME +
                    " - " + BooksContract.BooksEntry.PRICE +
                    " - " + BooksContract.BooksEntry.QUANTITY +
                    " - " + BooksContract.BooksEntry.SUPPLIER +
                    " - " + BooksContract.BooksEntry.PHONESUP + "\n");

            int idColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.BOOKNAME);
            int quantIndex = cursor.getColumnIndex(BooksContract.BooksEntry.QUANTITY);
            int priceIndex = cursor.getColumnIndex(BooksContract.BooksEntry.PRICE);
            int supIndex = cursor.getColumnIndex(BooksContract.BooksEntry.SUPPLIER);
            int phonesupINdex = cursor.getColumnIndex(BooksContract.BooksEntry.PHONESUP);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentQuantity = cursor.getInt(quantIndex);
                int currentPrice = cursor.getInt(priceIndex);
                String currentSup = cursor.getString(supIndex);
                int currentPhoneSup = cursor.getInt(phonesupINdex);
                String message = "\n" + currentID + " - " +
                        currentName + " - " + currentQuantity + " - " + currentPrice + " - " + currentSup + " - " + currentPhoneSup;
                Log.i(STRINGTAG, message);
            }
        } finally {
            cursor.close();
        }
    }
}
