package com.ahmdkhled.storemanagmentsystem.ui;


import android.app.ProgressDialog;
import android.content.ContentUris;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = ProductDetail.class.getSimpleName();

    @BindView(R.id.barcode_value)
    TextView mBarcodeValueTxt;
    @BindView(R.id.product_name_add)
    EditText mProductNameTxt;
    @BindView(R.id.product_price_add)
    EditText mProductPriceTxt;
    @BindView(R.id.product_desc_add)
    EditText mProductDescTxt;
    @BindView(R.id.product_quantity_add)
    EditText mProductQuantityTxt;
    @BindView(R.id.btn)
    Button mUpdateBtn;

    private ProgressDialog mProgressDialog;

    private String barcodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        // bind views
        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("waiting...");
        mProgressDialog.show();



        // get barcode value from intent
        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("detail_extra") != null){
            barcodeValue = intent.getStringExtra("detail_extra");
            Log.d(TAG,"barcode value is "+barcodeValue);
            getSupportLoaderManager().initLoader(1,null,this);
        }else Log.d(TAG,"barcode value is null");


        mUpdateBtn.setText(R.string.update_btn);
        mUpdateBtn.setEnabled(false);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = ContentUris.withAppendedId(ProductsContract.productsUri, Long.parseLong(barcodeValue));
        return new CursorLoader(this, uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mProgressDialog.dismiss();
        updateViews(cursor);


    }

    private void updateViews(Cursor cursor) {
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            final String name = cursor.getString(cursor.getColumnIndex(ProductsContract.NAME));
            final String price = cursor.getString(cursor.getColumnIndex(ProductsContract.PRICE));
            final String desc = cursor.getString(cursor.getColumnIndex(ProductsContract.DESCRIPTION));
            final String id = cursor.getString(cursor.getColumnIndex(ProductsContract.PRODUCT_ID));
            final String quantity = cursor.getString(cursor.getColumnIndex(ProductsContract.QUANTITY));

            Log.d(TAG,"name is "+name);
            Log.d(TAG,"price is "+price);
            Log.d(TAG,"desc is "+desc);

            mBarcodeValueTxt.setText(id);
            mProductNameTxt.setText(name);
            mProductPriceTxt.setText(price);
            mProductDescTxt.setText(desc);
            mProductQuantityTxt.setText(quantity);

            mUpdateBtn.setEnabled(true);
            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(ProductsContract.PRODUCT_ID,id);
                    contentValues.put(ProductsContract.NAME,name);
                    contentValues.put(ProductsContract.PRICE,price);
                    contentValues.put(ProductsContract.QUANTITY,quantity);
                    contentValues.put(ProductsContract.DESCRIPTION,desc);
                    Uri uri = ContentUris.withAppendedId(ProductsContract.productsUri, Long.parseLong(id));
                    getContentResolver().update(uri,contentValues,null,null);
                }
            });

        }else Log.d(TAG,"cursor is null");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}
