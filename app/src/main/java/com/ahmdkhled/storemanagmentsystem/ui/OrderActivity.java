package com.ahmdkhled.storemanagmentsystem.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.adapters.OrderItemsAdapter;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.OrderItem;
import com.ahmdkhled.storemanagmentsystem.model.Product;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {

    ArrayList<OrderItem> orderItems;
    HashMap<String,Integer> itemsMap;
    RecyclerView recyclerView;
    OrderItemsAdapter orderItemsAdapter;
    Button placeOrder;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderItems=new ArrayList<>();
        itemsMap=new HashMap<>();
        recyclerView=findViewById(R.id.orderItemsRecycler);
        placeOrder=findViewById(R.id.placeOrder);



        if (savedInstanceState==null){
            Log.d("onSaveInstanceState","i will open capture activity");
            openCaptureActivity();
        }else {
            Log.d("onSaveInstanceState","i will save  capture list ");
            orderItems=savedInstanceState.getParcelableArrayList("ORDER_ITEMS");
            orderItemsAdapter=new OrderItemsAdapter(orderItems);
            recyclerView.setAdapter(orderItemsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });
    }

    void showFake(){
        Product product=new Product("23233","iphone","",10,20.0);
        orderItems.add(new OrderItem(10,product));
        orderItems.add(new OrderItem(50,product));
        orderItems.add(new OrderItem(100,product));
        orderItems.add(new OrderItem(1000,product));
        OrderItemsAdapter orderItemsAdapter=new OrderItemsAdapter(orderItems);
        recyclerView.setAdapter(orderItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodeDetected(OrderActivity.BarcodeDetectionEvent event) {
        Log.d("onBarcodeDetected", "onBarcodeDetected :) ----" +event.barcodeValue);
        addProductItem(getOrderItem(event.barcodeValue));
    };

    void addProductItem(OrderItem orderItem){
        if (orderItem!=null){
            mediaPlayer = MediaPlayer.create(this, R.raw.beep);
            mediaPlayer.start();
            orderItems.add(orderItem);
            orderItemsAdapter=new OrderItemsAdapter(orderItems);
            recyclerView.setAdapter(orderItemsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    OrderItem getOrderItem(String id){
        Product product=new Product();
        product.setId(id);
        OrderItem orderItem=new OrderItem(product);
        int pos=orderItems.indexOf(orderItem);
        if (pos>-1){
            mediaPlayer = MediaPlayer.create(this, R.raw.beep);
            mediaPlayer.start();
            orderItems.get(pos).setQuantity(orderItems.get(pos).getQuantity()+1);
            Log.d("QUANTITY","pos :  "+pos);
            orderItemsAdapter.notifyDataSetChanged();
            return null;
        }

        OrderItem mOrderItem=null;
        Cursor cursor=getContentResolver().query(ProductsContract.productsUri
                ,null,ProductsContract.PRODUCT_ID+
                " =?",new String[]{id},null);
        Log.d("onBarcodeDetected", "get order item "+cursor.getCount());

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            Log.d("onBarcodeDetected", "cursor not null");
            String productName=cursor.getString(cursor.getColumnIndex(ProductsContract.NAME));
            Double productPrice=cursor.getDouble(cursor.getColumnIndex(ProductsContract.PRICE));
            Product mProduct=new Product();
            mProduct.setName(productName);
            mProduct.setPrice(productPrice);
            mProduct.setId(id);
            Log.d("onBarcodeDetected", "product name "+productName);

            mOrderItem=new OrderItem(1,mProduct);
        }else{
            mediaPlayer = MediaPlayer.create(this, R.raw.error);
            mediaPlayer.start();
        }

        return mOrderItem;

    }

    void placeOrder(){
        if (orderItems.size()==0){return;}

        // insert into orders
        ContentValues contentValues=new ContentValues();
        contentValues.put(ProductsContract.ORDER_DATE, DateFormat.getDateTimeInstance().format(new Date()));
        Uri uri=getContentResolver().insert(ProductsContract.ordersUri,contentValues);
        String id=uri.getLastPathSegment();
        Log.d("id is ", id+"");

        ContentValues itemsValues=new ContentValues();
        for(OrderItem orderItem:orderItems){
            itemsValues.put(ProductsContract.ORDERID,id);
            itemsValues.put(ProductsContract.PRODUCTID,orderItem.getProduct().getId());
            itemsValues.put(ProductsContract.ORDER_ITEM_QUANTITY,orderItem.getQuantity());
            getContentResolver().insert(ProductsContract.orderItemssUri,itemsValues);
        }

        orderItems.clear();
        orderItemsAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onSaveInstanceState","Order Activity orientation changed");
        outState.putParcelableArrayList("ORDER_ITEMS",orderItems);
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
        capureIntent.putExtra("source","OrderActivity");
        startActivity(capureIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    public static class BarcodeDetectionEvent {
        String barcodeValue ;
        public BarcodeDetectionEvent(String barcodeValue) {
            this.barcodeValue=barcodeValue;
        }
    }
}
