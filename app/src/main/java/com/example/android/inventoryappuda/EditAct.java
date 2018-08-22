package com.example.android.inventoryappuda;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryappuda.database.BooksContract;

public class EditAct extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURRENT_LOADER = 0;
    private Uri currentUri;
    private EditText titleEdit;
    private EditText priceEdit;
    private EditText quantEdit;
    private EditText suppEdit;
    private EditText phoneEdit;
    private Button lessbtn;
    private Button morebtn;
    private Button callbtn;
    private boolean dataWasChanged = false;
    private View.OnTouchListener fieldTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dataWasChanged = true;
            return false;
        }
    };
    private int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        Intent intent = getIntent();
        currentUri = intent.getData();

        if (currentUri == null) {
            setTitle(getString(R.string.new_book));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_book));
            getLoaderManager().initLoader(CURRENT_LOADER, null, this);
        }

        titleEdit = findViewById(R.id.edit_title);
        priceEdit = findViewById(R.id.edit_price);
        quantEdit = findViewById(R.id.edit_quantity);
        quantEdit.setText(String.valueOf(quantity));
        suppEdit = findViewById(R.id.edit_supp);
        phoneEdit = findViewById(R.id.edit_phone);
        lessbtn = findViewById(R.id.less_button);
        morebtn = findViewById(R.id.more_button);
        callbtn = findViewById(R.id.call_button);

        titleEdit.setOnTouchListener(fieldTouchListener);
        priceEdit.setOnTouchListener(fieldTouchListener);
        quantEdit.setOnTouchListener(fieldTouchListener);
        suppEdit.setOnTouchListener(fieldTouchListener);
        phoneEdit.setOnTouchListener(fieldTouchListener);

        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(String.valueOf(quantEdit.getText()));
                quantity++;
                quantEdit.setText(String.valueOf(quantity));
            }
        });
        lessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    quantity = Integer.parseInt(String.valueOf(quantEdit.getText()));
                    quantity--;
                    quantEdit.setText(String.valueOf(quantity));
                }
            }
        });
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEdit.getText().toString().trim();
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BooksContract.BooksEntry._ID,
                BooksContract.BooksEntry.BOOKNAME,
                BooksContract.BooksEntry.PRICE,
                BooksContract.BooksEntry.QUANTITY,
                BooksContract.BooksEntry.SUPPLIER,
                BooksContract.BooksEntry.PHONESUP};

        return new CursorLoader(this,
                currentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() < 1) {
            return;
        }
        if (data.moveToFirst()) {
            int titleColumnIndex = data.getColumnIndex(BooksContract.BooksEntry.BOOKNAME);
            int priceColumnIndex = data.getColumnIndex(BooksContract.BooksEntry.PRICE);
            int quantColumnIndex = data.getColumnIndex(BooksContract.BooksEntry.QUANTITY);
            int suppColumnIndex = data.getColumnIndex(BooksContract.BooksEntry.SUPPLIER);
            int phoneColumnInd = data.getColumnIndex(BooksContract.BooksEntry.PHONESUP);

            String title = data.getString(titleColumnIndex);
            int price = data.getInt(priceColumnIndex);
            quantity = data.getInt(quantColumnIndex);
            String supplier = data.getString(suppColumnIndex);
            int phoneSup = data.getInt(phoneColumnInd);

            titleEdit.setText(title);
            priceEdit.setText(Integer.toString(price));
            quantEdit.setText(Integer.toString(quantity));
            suppEdit.setText(supplier);
            phoneEdit.setText(Integer.toString(phoneSup));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        titleEdit.setText("");
        priceEdit.setText("");
        quantEdit.setText("");
        suppEdit.setText("");
        phoneEdit.setText("");
    }

    private void saveBook() {
        String titleString = titleEdit.getText().toString().trim();
        String priceString = priceEdit.getText().toString().trim();
        String quantString = quantEdit.getText().toString().trim();
        String suppString = suppEdit.getText().toString().trim();
        String phoneString = phoneEdit.getText().toString().trim();

        if (currentUri == null &&
                TextUtils.isEmpty(titleString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantString) && TextUtils.isEmpty(suppString) && TextUtils.isEmpty(phoneString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BooksContract.BooksEntry.BOOKNAME, titleString);
        values.put(BooksContract.BooksEntry.PRICE, priceString);
        values.put(BooksContract.BooksEntry.QUANTITY, quantString);
        values.put(BooksContract.BooksEntry.SUPPLIER, suppString);
        values.put(BooksContract.BooksEntry.PHONESUP, phoneString);
        int quantity = 0;
        if (!TextUtils.isEmpty(quantString)) {
            quantity = Integer.parseInt(quantString);
        }
        values.put(BooksContract.BooksEntry.QUANTITY, quantity);

        if (currentUri == null) {
            Uri newUri = getContentResolver().insert(BooksContract.BooksEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(currentUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_successfull),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_option_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.edit_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_save:
                String titleString = titleEdit.getText().toString();
                String priceString = priceEdit.getText().toString();
                String quantString = quantEdit.getText().toString();
                if (TextUtils.isEmpty(titleString)) {
                    Toast.makeText(this, "Add title", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(priceString)) {
                    Toast.makeText(this, "Add price", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(quantString)) {
                    Toast.makeText(this, "Add quantity", Toast.LENGTH_SHORT).show();
                    break;
                }

                saveBook();
                finish();
                return true;
            case R.id.edit_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!dataWasChanged) {
                    NavUtils.navigateUpFromSameTask(EditAct.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditAct.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        if (currentUri != null) {
            int rowsDeleted = getContentResolver().delete(currentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!dataWasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

}