package com.ahmdkhled.storemanagmentsystem.ui;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ahmdkhled.storemanagmentsystem.R;

public class MainActivity extends AppCompatActivity {
    Button goToAddProduct,showProducts;
    Button goToOrder,mViewOrdersBtn,mAddCategoryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToAddProduct=findViewById(R.id.goToAddProduct);
        goToOrder=findViewById(R.id.goToOrder);
        mViewOrdersBtn=findViewById(R.id.view_orders);
        showProducts=findViewById(R.id.show_products);
        mAddCategoryBtn=findViewById(R.id.add_category);

        goToAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddProductActivity.class));
            }
        });

        goToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),OrderActivity.class));
            }
        });

        mViewOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ViewOrders.class));
            }
        });

        showProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ProductsActivity.class);
                startActivity(intent);
            }
        });

        mAddCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CategoryActivity.class);
                startActivity(intent);
            }
        });

    }


}





