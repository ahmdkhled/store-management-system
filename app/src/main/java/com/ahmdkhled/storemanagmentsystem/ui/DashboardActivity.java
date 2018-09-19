package com.ahmdkhled.storemanagmentsystem.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.productsNum)
    TextView productsNum;
    @BindView(R.id.ordersNum)
    TextView ordersNum;
    int PRODUCTS_NUM_LOADER=16;
    int ORDERS_NUM_LOADER=17;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setElevation(0);
        ButterKnife.bind(this);
        getLoaderManager().initLoader(PRODUCTS_NUM_LOADER,null,this);
        getLoaderManager().initLoader(ORDERS_NUM_LOADER,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i==PRODUCTS_NUM_LOADER){
            return new CursorLoader(this, ProductsContract.productsUri,
                    new String[]{ProductsContract.PRODUCT_ID},null,null,null);
        }else if (i==ORDERS_NUM_LOADER){
            return new CursorLoader(this, ProductsContract.ordersUri,
                    new String[]{ProductsContract.ORDER_ID},null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId()==PRODUCTS_NUM_LOADER){
            productsNum.setText(String.valueOf(cursor.getCount()));
        }else if (loader.getId()==ORDERS_NUM_LOADER){
            Log.d("CCCIIIC","cc "+cursor.getCount());
            ordersNum.setText(String.valueOf(cursor.getCount()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
