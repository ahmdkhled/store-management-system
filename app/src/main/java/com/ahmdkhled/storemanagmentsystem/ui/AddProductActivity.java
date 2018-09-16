package com.ahmdkhled.storemanagmentsystem.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.Category;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = AddProductActivity.class.getSimpleName();
    private static final int CATEGORY_LOADER = 14;


    @BindView(R.id.product_name_add)
    EditText mProductNameTxt;
    @BindView(R.id.product_price_add)
    EditText mProductPriceTxt;
    @BindView(R.id.product_desc_add)
    EditText mProductDescTxt;
    @BindView(R.id.product_quantity_add)
    EditText mProductQuantityTxt;
    @BindView(R.id.barcode_value)
    TextView mBarcodeValueTxt;
    @BindView(R.id.btn)
    Button mSaveBtn;
    @BindView(R.id.barcode_logo)
    ImageView barcode_logo;
    @BindView(R.id.category_spinner)
    Spinner mCategotySpinner;
    MediaPlayer mediaPlayer;

    String id;

    private String barcodeValue;
    private List<String> categoryList = new ArrayList<>();
    private String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ButterKnife.bind(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        mSaveBtn.setText(R.string.save_btn);

        openCaptureActivity();

        // get barcode value from intent
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("add_extra") != null) {
            barcodeValue = intent.getStringExtra("add_extra");
            mBarcodeValueTxt.setText(barcodeValue);
            Log.d(TAG, "barcode value is " + barcodeValue);
        } else Log.d(TAG, "barcode value is null)");


        getSupportLoaderManager().initLoader(CATEGORY_LOADER,null,this);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    String Name = mProductNameTxt.getText().toString();
                    double Price = Double.valueOf(mProductPriceTxt.getText().toString());
                    int Quantity = Integer.valueOf(mProductQuantityTxt.getText().toString());
                    String Desc = mProductDescTxt.getText().toString();
                    addProduct(id, Name, Price, Quantity, Desc);
                    clearFields();
                } else {
                    Toast.makeText(getApplicationContext(), "please complete all required fields"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        barcode_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCaptureActivity();
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodeDetected(BarcodeDetectedEvent event) {
        mBarcodeValueTxt.setText(event.barcodeValue);
        id=event.barcodeValue;
        mediaPlayer.start();
        Log.d("onBarcodeDetected", "onBarcodeDetected :)");
    };



    void addProduct(String id, String name, Double price, int quantity, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductsContract.PRODUCT_ID, id);
        contentValues.put(ProductsContract.NAME, name);
        contentValues.put(ProductsContract.PRICE, price);
        contentValues.put(ProductsContract.QUANTITY, quantity);
        contentValues.put(ProductsContract.DESCRIPTION, desc);
        getContentResolver().insert(ProductsContract.productsUri, contentValues);
    }

    boolean validateInput() {
        return (!TextUtils.isEmpty(mProductNameTxt.getText().toString())
                && !TextUtils.isEmpty(mProductPriceTxt.getText().toString())
                && !TextUtils.isEmpty(mProductQuantityTxt.getText().toString()));

    }

    void clearFields() {
        mProductNameTxt.setText("");
        mProductPriceTxt.setText("0");
        mProductQuantityTxt.setText("0");
        mProductDescTxt.setText("");
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    void openCaptureActivity(){
        Intent capureIntent=new Intent(this,CaptureActivity.class);
        capureIntent.putExtra("source","AddProductActivity");
        startActivity(capureIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = (ProductsContract.categoryUri);
        return new CursorLoader(this, uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            categoryList.clear();
            data.moveToFirst();
            do{
                Category category = new Category();
                String name = data.getString(data.getColumnIndex(ProductsContract.CATEGORY_NAME));
                category.setName(name);
                categoryList.add(category.getName());
                Log.d(TAG,"cat name "+name);
            }while (data.moveToNext());

            // setup spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categoryList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCategotySpinner.setAdapter(dataAdapter);

            mCategotySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mCategory = adapterView.getItemAtPosition(i).toString();
                    Log.d(TAG,"selected category is "+mCategory);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public static class BarcodeDetectedEvent {
        String barcodeValue ;
        public BarcodeDetectedEvent(String barcodeValue) {
            this.barcodeValue=barcodeValue;
        }
    }

}



