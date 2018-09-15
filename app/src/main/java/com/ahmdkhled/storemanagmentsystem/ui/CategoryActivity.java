package com.ahmdkhled.storemanagmentsystem.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.adapters.CategoryAdapter;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.model.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CATEGORY_LOADER = 13;
    private static final String TAG = CategoryActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_category_txt)
    EditText mCategoryTxt;
    @BindView(R.id.add)
    Button mAddCAtegoryBtn;
    @BindView(R.id.category_recycler_view)
    RecyclerView mRecyclerView;

    private CategoryAdapter adapter;
    private List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // bind views
        ButterKnife.bind(this);

        // setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.category_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup recycler view
        adapter = new CategoryAdapter(categoryList,this);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(CATEGORY_LOADER,null,this);



        mAddCAtegoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mCategoryTxt.getText().toString())){
                    String category = mCategoryTxt.getText().toString();
                    ContentValues cv = new ContentValues();
                    cv.put(ProductsContract.categoryName,category);
                    getContentResolver().insert(ProductsContract.categoryUri,cv);

                }else
                    Toast.makeText(CategoryActivity.this, "invalid category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = (ProductsContract.categoryUri);
        return new CursorLoader(this, uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            categoryList.clear();
            data.moveToFirst();
            do{
                Category category = new Category();
                String name = data.getString(data.getColumnIndex(ProductsContract.categoryName));
                category.setName(name);
                categoryList.add(category);
                Log.d(TAG,"cat name "+name);
            }while (data.moveToNext());

            adapter.notifyAdapter(categoryList);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
