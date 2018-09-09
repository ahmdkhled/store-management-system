package com.ahmdkhled.storemanagmentsystem.ui;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.adapters.OrderAdapter;
import com.ahmdkhled.storemanagmentsystem.adapters.OrderItemsAdapter;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.Order;
import com.ahmdkhled.storemanagmentsystem.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewOrders extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int VIEW_ORDERS_LOADER = 2;
    @BindView(R.id.orders_recyclerview)
    RecyclerView mRecyclerView;

    private OrderAdapter mAdapter;
    private List<Order> mOrders = new ArrayList<>();
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(VIEW_ORDERS_LOADER,null,this);


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("vieworders","onCreateLoader");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("WAITING...");
        mProgressDialog.show();
        Uri uri = (ProductsContract.ordersUri);
        return new CursorLoader(this, uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("vieworders","onLoadFinished");
        mProgressDialog.dismiss();
        mOrders = getOrders(data);
        mAdapter = new OrderAdapter(this,mOrders);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    private List<Order> getOrders(Cursor data) {
        Log.d("vieworders","getOrders");
        if(data != null && data.getCount() > 0){
            Log.d("vieworders","cursor not null");
            data.moveToFirst();
            do{
                Order order = new Order();
                order.setId(data.getInt(data.getColumnIndex(ProductsContract.ORDER_ID)));
                order.setDate(data.getString(data.getColumnIndex(ProductsContract.ORDER_DATE)));
                mOrders.add(order);
            }while (data.moveToNext());

            return mOrders;
        }
        Log.d("vieworders","cursor  null");
        return null;
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
