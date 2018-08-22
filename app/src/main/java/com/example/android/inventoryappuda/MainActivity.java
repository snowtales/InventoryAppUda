package com.example.android.inventoryappuda;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.inventoryappuda.database.BooksContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;
    BookCursorAdapter bookCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView bookslist = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        bookslist.setEmptyView(emptyView);
        bookCursorAdapter = new BookCursorAdapter(this, null);
        bookslist.setAdapter(bookCursorAdapter);

        bookslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditAct.class);
                Uri currentPetUri = ContentUris.withAppendedId(BooksContract.BooksEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditAct.class);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertBook();
                return true;
            case R.id.action_delete_all_entries:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertBook() {
        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.BOOKNAME, "Poetry by Pushkin");
        values.put(BooksContract.BooksEntry.QUANTITY, 7);
        values.put(BooksContract.BooksEntry.PRICE, 29);
        values.put(BooksContract.BooksEntry.SUPPLIER, "everywhere");
        values.put(BooksContract.BooksEntry.PHONESUP, 123456789);
        getContentResolver().insert(BooksContract.BooksEntry.CONTENT_URI, values);
    }

    private void deleteAll() {
        getContentResolver().delete(BooksContract.BooksEntry.CONTENT_URI, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BooksContract.BooksEntry._ID,
                BooksContract.BooksEntry.BOOKNAME,
                BooksContract.BooksEntry.QUANTITY,
                BooksContract.BooksEntry.SUPPLIER};

        return new CursorLoader(this,
                BooksContract.BooksEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookCursorAdapter.swapCursor(null);
    }

    public void saleProduct(long productId, int quantity) {

        if (quantity >= 1) {
            quantity--;
            Uri updateUri = ContentUris.withAppendedId(BooksContract.BooksEntry.CONTENT_URI, productId);
            ContentValues values = new ContentValues();
            values.put(BooksContract.BooksEntry.QUANTITY, quantity);
            int rowsUpdated = getContentResolver().update(
                    updateUri,
                    values,
                    null,
                    null);
            if (rowsUpdated == 1) {
                Toast.makeText(this, R.string.sale_ok, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.sale_not, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, R.string.sale_out_stock, Toast.LENGTH_LONG).show();
        }
    }
}
