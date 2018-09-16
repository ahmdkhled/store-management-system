package com.ahmdkhled.storemanagmentsystem.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.model.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private List<Category> mCategoryList;
    private Context mContext;

    public CategoryAdapter(List<Category> mCategoryList, Context mContext) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(mContext).
                inflate(R.layout.categoty_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = mCategoryList.get(position);
        Log.d("fromCatAdpater","quantity is "+category.getQuantity());
        holder.mCategoryTxt.setText(category.getName());
        holder.mCategoryQuantityTxt.setText(category.getQuantity()+"");
    }

    @Override
    public int getItemCount() {
        if(mCategoryList != null) return mCategoryList.size();
        else return 0;
    }

    public void notifyAdapter(List<Category> mCategoryList){
        this.mCategoryList = mCategoryList;
        this.notifyDataSetChanged();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.category_text)
        TextView mCategoryTxt;
        @BindView(R.id.category_quantity_text)
        TextView mCategoryQuantityTxt;
        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
