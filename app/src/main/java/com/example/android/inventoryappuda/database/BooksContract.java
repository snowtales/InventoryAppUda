package com.example.android.inventoryappuda.database;

import android.provider.BaseColumns;

/**
 * Created by Анастасия on 27.07.2018.
 */

public class BooksContract {
    public static abstract class BooksEntry implements BaseColumns {
        public static final String TABLE_NAME = "books";
        public static final String _ID = "id";
        public static final String BOOKNAME = "name";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String SUPPLIER = "supplier";
        public static final String PHONESUP = "phone";
    }
}
