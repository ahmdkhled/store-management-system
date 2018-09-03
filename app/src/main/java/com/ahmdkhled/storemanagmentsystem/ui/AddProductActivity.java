package com.ahmdkhled.storemanagmentsystem.ui;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.data.ProductsContract;
import com.ahmdkhled.storemanagmentsystem.data.ProductsProvider;

public class AddProductActivity extends AppCompatActivity {

    EditText name,price,quantity,desc;
    TextView id;
    Button addProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        name=findViewById(R.id.product_name_add);
        price=findViewById(R.id.product_price_add);
        quantity=findViewById(R.id.product_quantity_add);
        desc=findViewById(R.id.product_desc_add);
        id=findViewById(R.id.barcode_value);
        addProduct=findViewById(R.id.addProduct);


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()){
                    int id=0;
                    String Name=name.getText().toString();
                    double Price=Double.valueOf(price.getText().toString());
                    int Quantity=Integer.valueOf(quantity.getText().toString());
                    String Desc=desc.getText().toString();
                    addProduct(id,Name,Price,Quantity,Desc);
                    clearFields();
                }else {
                    Toast.makeText(getApplicationContext(),"please complete all required fields"
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void addProduct(int id,String name,Double price,int quantity,String desc){
        ContentValues contentValues=new ContentValues();
        contentValues.put(ProductsContract.PRODUCT_ID,id);
        contentValues.put(ProductsContract.NAME,name);
        contentValues.put(ProductsContract.PRICE,price);
        contentValues.put(ProductsContract.QUANTITY,quantity);
        contentValues.put(ProductsContract.DESCRIPTION,desc);
        getContentResolver().insert(ProductsContract.productsUri,contentValues);
    }

    boolean validateInput(){
        return (!TextUtils.isEmpty(name.getText().toString())
                &&!TextUtils.isEmpty(price.getText().toString())
                &&!TextUtils.isEmpty(quantity.getText().toString()));

    }
    void clearFields(){
        name.setText("");
        price.setText("0");
        quantity.setText("0");
        desc.setText("");
    }
}
