package com.ahmdkhled.storemanagmentsystem.ui;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = EditProductActivity.class.getSimpleName();

    @BindView(R.id.product_barcode_value)
    TextView mProductBarcode;

    @BindView(R.id.edit_product_name)
    EditText mProductNameTxt;

    @BindView(R.id.edit_product_price)
    EditText mProductPriceTxt;

    @BindView(R.id.decrease_quantity_btn)
    ImageButton mdecreaseQuantityBtn;

    @BindView(R.id.edit_product_quantity)
    EditText mProductQuantityTxt;

    @BindView(R.id.increase_quantity_btn)
    ImageButton mIncreaseQuantityBtn;

    @BindView(R.id.product_category_value)
    TextView mCategoryTxt;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private ProgressDialog mProgressDialog;
    private String productId;
    private int mQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_edit_product);
        // bind views
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.edit_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // show dialog until getting data
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("waiting...");
        mProgressDialog.show();



        // get barcode value from intent
        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("detail_extra") != null){
            productId = intent.getStringExtra("detail_extra");
            Log.d(TAG,"barcode value is "+ productId);
            getSupportLoaderManager().initLoader(1,null,this);
        }else Log.d(TAG,"barcode value is null");


        // increase & decrease quantity of product
        mIncreaseQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity++;
                mProductQuantityTxt.setText(mQuantity+"");
            }
        });

       


    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = (ProductsContract.productsUri);
        return new CursorLoader(this, uri,null,ProductsContract.PRODUCT_ID+"=?",
                new String[]{productId},null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mProgressDialog.dismiss();
        updateViews(cursor);


    }

    private void updateViews(Cursor cursor) {
        // first get product details
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(ProductsContract.NAME));
            String price = cursor.getString(cursor.getColumnIndex(ProductsContract.PRICE));
            String desc = cursor.getString(cursor.getColumnIndex(ProductsContract.DESCRIPTION));
            String id = cursor.getString(cursor.getColumnIndex(ProductsContract.PRODUCT_ID));
            mQuantity = cursor.getInt(cursor.getColumnIndex(ProductsContract.QUANTITY));
            String category = cursor.getString(cursor.getColumnIndex(ProductsContract.CATEGORY_NAME));

            Log.d(TAG,"name is "+name);
            Log.d(TAG,"price is "+price);
            Log.d(TAG,"desc is "+desc);

            // then update views
            mProductBarcode.setText(id);
            mProductNameTxt.setText(name);
            mProductPriceTxt.setText(price);
            mProductQuantityTxt.setText(mQuantity+"");
            mCategoryTxt.setText(category);


        }else Log.d(TAG,"cursor is null");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_product_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.done){
            updateProductDetails();
            return true;
        }else return super.onOptionsItemSelected(item);
    }

    private void updateProductDetails() {
        ContentValues contentValues=new ContentValues();
        contentValues.put(ProductsContract.NAME,mProductNameTxt.getText().toString());
        contentValues.put(ProductsContract.PRICE,mProductPriceTxt.getText().toString());
        contentValues.put(ProductsContract.QUANTITY,mProductQuantityTxt.getText().toString());
        contentValues.put(ProductsContract.CATEGORY_NAME,mCategoryTxt.getText().toString());
        Uri uri = (ProductsContract.productsUri);
        getContentResolver().update(uri,contentValues,ProductsContract.PRODUCT_ID+"=?",
                new String[]{productId});
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
