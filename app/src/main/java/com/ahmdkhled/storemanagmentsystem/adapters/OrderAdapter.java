package com.ahmdkhled.storemanagmentsystem.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private Context mContext;
    private List<Order> mOrders;

    public OrderAdapter(Context mContext, List<Order> mOrders) {
        this.mContext = mContext;
        this.mOrders = mOrders;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(mContext)
        .inflate(R.layout.order_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        String date = mOrders.get(position).getDate();
        holder.mOrderDateTxt.setText(date);
    }

    @Override
    public int getItemCount() {
        if(mOrders != null) return mOrders.size();
        else return 0;
    }

    public class OrderHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.order_date)
        TextView mOrderDateTxt;

        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
