package com.example.android.inventoryappuda;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryappuda.database.BooksContract;

/**
 * Created by Анастасия on 08.08.2018.
 */

public class BookCursorAdapter extends CursorAdapter {
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_viewer, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.title);
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView suppTextView = view.findViewById(R.id.shop_name);

        Button minusButton = view.findViewById(R.id.sale_button);

        int titleColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.BOOKNAME);
        int quantityColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.QUANTITY);
        int suppColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.SUPPLIER);

        String bookTitle = cursor.getString(titleColumnIndex);
        String quantityOfBook = cursor.getString(quantityColumnIndex);
        String supplierShop = cursor.getString(suppColumnIndex);

        nameTextView.setText(bookTitle);
        quantityTextView.setText(quantityOfBook);
        suppTextView.setText(supplierShop);
        final long id = cursor.getInt(cursor.getColumnIndexOrThrow(BooksContract.BooksEntry._ID));
        final int qty = Integer.parseInt(quantityOfBook);

        view.findViewById(R.id.sale_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.saleProduct(id, qty);
            }
        });
    }


}
