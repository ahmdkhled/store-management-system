package com.ahmdkhled.storemanagmentsystem.ui;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_category_txt)
    EditText mCategoryTxt;
    @BindView(R.id.add)
    Button mAddCAtegoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ButterKnife.bind(this);


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
}
