package com.ahmdkhled.storemanagmentsystem.ui;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.View;
import android.widget.ProgressBar;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.adapters.OrderAdapter;
import com.ahmdkhled.storemanagmentsystem.adapters.OrderItemsAdapter;
import com.ahmdkhled.storemanagmentsystem.data.Dbhelper;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.Order;
import com.ahmdkhled.storemanagmentsystem.model.OrderItem;
import com.ahmdkhled.storemanagmentsystem.model.Product;
import com.google.android.gms.common.stats.ConnectionTracker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetails extends AppCompatActivity {

    private static final int VIEW_ORDERS_DETAILS_LOADER = 3;
    private static final String TAG = OrderDetails.class.getSimpleName();
    @BindView(R.id.orderItemsRecycler)
    RecyclerView mRecyclerView;


    private OrderItemsAdapter mAdapter;
    private ArrayList<OrderItem> mOrderItems = new ArrayList<>();
    private int orderId;
    private Dbhelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        dbHelper = new Dbhelper(this);

        // get order id
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getIntExtra("order_id", 0);
            Log.d(TAG, "order id is " + orderId);
            Cursor cursor = dbHelper.showOrderItem(String.valueOf(orderId));
            if (cursor != null && cursor.getCount() != 0) {
                mOrderItems = getOrderItemDetails(cursor);
                mAdapter = new OrderItemsAdapter(mOrderItems);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(mAdapter);

            } else Log.d(TAG, "cursor is null");
        }

    }


    private ArrayList<OrderItem> getOrderItemDetails(Cursor data) {
        mOrderItems.clear();
        Log.d(TAG, "getOrderItemDetails");
        if (data != null && data.getCount() > 0) {
            Log.d(TAG, "cursor not null");
            data.moveToFirst();
            do {
                String productName = data.getString(data.getColumnIndex(ProductsContract.NAME));
                double productPrice = data.getDouble(data.getColumnIndex(ProductsContract.PRICE));
                int quantity = data.getInt(data.getColumnIndex(ProductsContract.ORDER_ITEM_QUANTITY));

                Product product = new Product();
                product.setName(productName);
                product.setPrice(productPrice);
                OrderItem orderItem = new OrderItem(quantity,product);
                mOrderItems.add(orderItem);

            } while (data.moveToNext());

            return mOrderItems;
        }
        Log.d(TAG, "cursor  null");
        return null;
    }
}



