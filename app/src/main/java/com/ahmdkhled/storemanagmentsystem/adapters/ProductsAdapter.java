package com.ahmdkhled.storemanagmentsystem.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.Product;
import com.ahmdkhled.storemanagmentsystem.ui.EditProductActivity;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder>
{

    private Context context;
    private ArrayList<Product> productsList;

    public ProductsAdapter(Context context, ArrayList<Product> productsList) {
        this.context = context;
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


    class ProductHolder extends RecyclerView.ViewHolder {
        TextView name,price,quantity;
        ImageView options;
        ProductHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            price=itemView.findViewById(R.id.productPrice);
            quantity=itemView.findViewById(R.id.productQuantity);
            options=itemView.findViewById(R.id.options);
        }

        void populateProduct(){
            name.setText(productsList.get(getAdapterPosition()).getName());
            price.setText(String.valueOf(productsList.get(getAdapterPosition()).getPrice())+"$");
            quantity.setText(String.valueOf(productsList.get(getAdapterPosition()).getQuantity()));
            final PopupMenu popupMenu=new PopupMenu(context,options);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId()==R.id.delete_item){
                        deleteProduct(getAdapterPosition());
                    }else if (menuItem.getItemId()==R.id.edit_item){
                        Intent intent=new Intent(context, EditProductActivity.class);
                        String id=productsList.get(getAdapterPosition()).getId();
                        intent.putExtra("detail_extra",id);
                        context.startActivity(intent);
                    }
                    return true;
                }
            });
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu.show();
                }
            });

        }


    }

    private void deleteProduct(int i){
        Log.d("fromProductAdapter","onDeleteProduct");
        String id=productsList.get(i).getId();
        Log.d("fromProductAdapter","id is "+id);
        context.getContentResolver().delete(ProductsContract.productsUri,
                ProductsContract.PRODUCT_ID+"=?",new String[]{id});

        // decrease number of products under this category
        String mCategory = productsList.get(i).getCategory();
        Cursor cursor = context.getContentResolver().query(ProductsContract.categoryUri,new String[]{ProductsContract.CATEGORY_QUANTITY},
                ProductsContract.CATEGORY_NAME+"=?",new String[]{mCategory},null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            int currentQuantity = cursor.getInt(cursor.getColumnIndex(ProductsContract.CATEGORY_QUANTITY));
            Log.d("fromProductAdapter","current value is "+currentQuantity);

            // then update this value
            ContentValues cv = new ContentValues();
            cv.put(ProductsContract.CATEGORY_QUANTITY,currentQuantity-1);
            context.getContentResolver().update(ProductsContract.categoryUri,cv,
                    ProductsContract.CATEGORY_NAME+"=?",new String[]{mCategory});
        }




    }


}
