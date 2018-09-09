package com.ahmdkhled.storemanagmentsystem.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.model.OrderItem;

import java.util.ArrayList;

public  class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemHolder> {

    private ArrayList<OrderItem> orderItems;

    public OrderItemsAdapter(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_row,parent,false);
        return new OrderItemHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        holder.populateOrderItem();
    }

    @Override
    public int getItemCount() {
        if (orderItems==null)
            return 0;
        return orderItems.size();
    }

    class OrderItemHolder extends RecyclerView.ViewHolder{
        TextView name,quantity,price,total;
        OrderItemHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.orderItemName);
            quantity=itemView.findViewById(R.id.orderItemQuantity);
            price=itemView.findViewById(R.id.orderItemPrice);
            total=itemView.findViewById(R.id.orderItemTotal);
        }

        void populateOrderItem(){
            OrderItem orderItem=orderItems.get(getAdapterPosition());
            Log.d("NULLLL",orderItem.getProduct().getPrice()+"--");
             name.setText(orderItem.getProduct().getName());
            quantity.setText(String.valueOf(orderItem.getQuantity()));
            price.setText(String.valueOf(orderItem.getProduct().getPrice()));
            double price=orderItem.getProduct().getPrice();
            int quantity=orderItem.getQuantity();
            double totalPrice=price*quantity;
            total.setText(String.valueOf(totalPrice));



        }
    }
}
