package com.ahmdkhled.storemanagmentsystem.ui;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ahmdkhled.storemanagmentsystem.R;

public class MainActivity extends AppCompatActivity {
    Button goToAddProduct;
    Button goToOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToAddProduct=findViewById(R.id.goToAddProduct);
        goToOrder=findViewById(R.id.goToOrder);

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

    }


}





