package com.ahmdkhled.storemanagmentsystem.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.adapters.OrderItemsAdapter;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.OrderItem;
import com.ahmdkhled.storemanagmentsystem.model.Product;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {

    ArrayList<OrderItem> orderItems;
    HashMap<String,Integer> itemsMap;
    RecyclerView recyclerView;
    OrderItemsAdapter orderItemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderItems=new ArrayList<>();
        itemsMap=new HashMap<>();
        recyclerView=findViewById(R.id.orderItemsRecycler);

        openCaptureActivity();
        //showFake();
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
        orderItems.add(orderItem);
        orderItemsAdapter=new OrderItemsAdapter(orderItems);
        recyclerView.setAdapter(orderItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    OrderItem getOrderItem(String id){

        if (itemsMap.containsKey(id)){
            itemsMap.put(id,itemsMap.get(id)+1);
            Product product=new Product();
            product.setId(id);
            OrderItem orderItem=new OrderItem(product);
            int pos=orderItems.indexOf(orderItem);
            orderItems.get(pos).setQuantity(orderItems.get(pos).getQuantity()+1);
            Log.d("QUANTITY","pos :  "+pos);
            orderItemsAdapter.notifyDataSetChanged();
            return null;
        }else{
            itemsMap.put(id,1);
        }

        OrderItem orderItem=null;
        Cursor cursor=getContentResolver().query(ProductsContract.productsUri
                ,null,ProductsContract.PRODUCT_ID+
                " =?",new String[]{id},null);
        Log.d("onBarcodeDetected", "get order item "+cursor.getCount());

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            Log.d("onBarcodeDetected", "cursor not null");
            String productName=cursor.getString(cursor.getColumnIndex(ProductsContract.NAME));
            Double productPrice=cursor.getDouble(cursor.getColumnIndex(ProductsContract.PRICE));
            Product product=new Product();
            product.setName(productName);
            product.setPrice(productPrice);
            product.setId(id);
            Log.d("onBarcodeDetected", "product name "+productName);

            orderItem=new OrderItem(itemsMap.get(id),product);
        }

        return orderItem;

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
