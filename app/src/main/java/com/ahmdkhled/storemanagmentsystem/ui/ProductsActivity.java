package com.ahmdkhled.storemanagmentsystem.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.adapters.ProductsAdapter;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.Product;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.productsRecycler)
    RecyclerView productsRecycler;
    @BindView(R.id.ProductsSearchBox)
    EditText searchProducts;
    @BindView(R.id.scanProduct)
    ImageView scanProduct;

    ArrayList<Product> products;

    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ButterKnife.bind(this);
        loaderCallbacks=this;
        products = new ArrayList<>();
        populateProducts(products);
        getLoaderManager().initLoader(11,null,loaderCallbacks);


        searchProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TEXTWAD",""+charSequence);
                if (charSequence.length()==0){
                    getLoaderManager().restartLoader(11,null,loaderCallbacks);
                }else {
                    Bundle bundle=new Bundle();
                    bundle.putString("NAME",String.valueOf(charSequence));
                    getLoaderManager().restartLoader(11,bundle,loaderCallbacks);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        scanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CaptureActivity.class);
                intent.putExtra("source","ProductsActivity");
                startActivity(intent);
            }
        });

    }

    void populateProducts(ArrayList<Product> products){
        productsAdapter=new ProductsAdapter(this,products);
        productsRecycler.setAdapter(productsAdapter);
        productsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (bundle==null){
            return new CursorLoader(this, ProductsContract.productsUri,null,
                    null,null,null);
        }
        if (bundle.containsKey("NAME")){
            String name=bundle.getString("NAME");
            return new CursorLoader(this, ProductsContract.productsUri,null,
                    ProductsContract.NAME+" like ?",new String[]{"%"+name+"%"},null);
        }else if (bundle.containsKey("ID")){
            String id=bundle.getString("ID");
            return new CursorLoader(this, ProductsContract.productsUri,null,
                    ProductsContract.PRODUCT_ID+" =?",new String[]{id},null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        products.clear();
        if (cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                String id=cursor.getString(cursor.getColumnIndex(ProductsContract.PRODUCT_ID));
                String name=cursor.getString(cursor.getColumnIndex(ProductsContract.NAME));
                double price=cursor.getDouble(cursor.getColumnIndex(ProductsContract.PRICE));
                int quantity=cursor.getInt(cursor.getColumnIndex(ProductsContract.QUANTITY));
                String desc=cursor.getString(cursor.getColumnIndex(ProductsContract.DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndex(ProductsContract.CATEGORY_NAME));
                products.add(new Product(id,name,desc,quantity,price,category));
            }
            cursor.close();
        }

        productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodeDetected(OnProductDetected event) {
        Bundle bundle=new Bundle();
        bundle.putString("ID",event.id);
        getLoaderManager().restartLoader(11,bundle,loaderCallbacks);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    static class OnProductDetected{
        String id;
        public OnProductDetected(String id) {
            this.id = id;
        }
    }
}
