package com.example.android.inventoryappuda.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class ContentProviderBooks extends ContentProvider {

    public static final String LOG_TAG = ContentProviderBooks.class.getSimpleName();

    private static final int ALLTABLE = 42;

    private static final int SINGLE_ITEM = 43;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH, ALLTABLE);
        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH + "/#", SINGLE_ITEM);
    }
    private Booksdb booksBase;

    @Override
    public boolean onCreate() {
        booksBase = new Booksdb(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = booksBase.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case ALLTABLE:
                cursor = database.query(BooksContract.BooksEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SINGLE_ITEM:
                selection = BooksContract.BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BooksContract.BooksEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("We don't know this URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALLTABLE:
                return BooksContract.BooksEntry.CONTENT_LIST_TYPE;
            case SINGLE_ITEM:
                return BooksContract.BooksEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case ALLTABLE:
                String name = contentValues.getAsString(BooksContract.BooksEntry.BOOKNAME);
                if (name == null) {
                    throw new IllegalArgumentException("Give Title to Book");
                }

                Integer price = contentValues.getAsInteger(BooksContract.BooksEntry.PRICE);
                if (price == null) {
                    throw new IllegalArgumentException("Book requires price");
                }

                Integer quantity = contentValues.getAsInteger(BooksContract.BooksEntry.QUANTITY);
                if (quantity != null && quantity < 0) {
                    throw new IllegalArgumentException("Quantity can not be less than 0");
                }

                SQLiteDatabase database = booksBase.getWritableDatabase();

                long id = database.insert(BooksContract.BooksEntry.TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = booksBase.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case ALLTABLE:
                rowsDeleted = database.delete(BooksContract.BooksEntry.TABLE_NAME, s, strings);
                break;
            case SINGLE_ITEM:
                s = BooksContract.BooksEntry._ID + "=?";
                strings = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(BooksContract.BooksEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (sUriMatcher.match(uri)) {
            case ALLTABLE:
                return updateTable(uri, contentValues, s, strings);
            case SINGLE_ITEM:
                s = BooksContract.BooksEntry._ID + "=?";
                strings = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateTable(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateTable(Uri uri, ContentValues contentValues, String s, String[] strings){
        if (contentValues.containsKey(BooksContract.BooksEntry.BOOKNAME)) {
            String name = contentValues.getAsString(BooksContract.BooksEntry.BOOKNAME);
            if (name == null) {
                throw new IllegalArgumentException("Give Title to Book");
            }
        }
        if (contentValues.containsKey(BooksContract.BooksEntry.PRICE)) {
            Integer price = contentValues.getAsInteger(BooksContract.BooksEntry.PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Book requires price");
            }
        }
        if (contentValues.containsKey(BooksContract.BooksEntry.QUANTITY)) {
            Integer quantity = contentValues.getAsInteger(BooksContract.BooksEntry.QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Quantity can not be less than 0");
            }
        }
        if (contentValues.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = booksBase.getWritableDatabase();
        int rowsUpdated = database.update(BooksContract.BooksEntry.TABLE_NAME, contentValues, s, strings);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
    }
        return rowsUpdated;
}
}
