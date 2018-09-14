package com.ahmdkhled.storemanagmentsystem.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.model.Product;
import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder>{

    private ArrayList<Product> productsList;

    public ProductsAdapter(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row,parent,false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.populateProduct();
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        TextView name,price,quantity;
        ProductHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            price=itemView.findViewById(R.id.productPrice);
            quantity=itemView.findViewById(R.id.productQuantity);
        }

        void populateProduct(){
            name.setText(productsList.get(getAdapterPosition()).getName());
            price.setText(String.valueOf(productsList.get(getAdapterPosition()).getPrice()));
            quantity.setText(String.valueOf(productsList.get(getAdapterPosition()).getQuantity()));
        }
    }
}
