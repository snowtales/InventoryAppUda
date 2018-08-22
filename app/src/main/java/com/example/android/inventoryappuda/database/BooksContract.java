package com.example.android.inventoryappuda.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Анастасия on 27.07.2018.
 */

public final class BooksContract {

    private  BooksContract(){}
    public static final String CONTENT_AUTHORITY="com.example.android.inventoryappuda";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "books";


    public static abstract class BooksEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static final String TABLE_NAME = "books";
        /**        public static final String _ID = BaseColumns._ID;
         *
         */
        public static final String BOOKNAME = "name";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String SUPPLIER = "supplier";
        public static final String PHONESUP = "phone";

    }
}
