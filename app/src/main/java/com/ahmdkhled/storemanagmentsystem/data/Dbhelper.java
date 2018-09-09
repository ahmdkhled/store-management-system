package com.ahmdkhled.storemanagmentsystem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {

    public static final String DBNAME = "store.db";
    public static final int DBVERSION = 8;

    public Dbhelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " +ProductsContract.PRODUCTS + "(" +
                ProductsContract.PRODUCT_ID+" TEXT," +
                ProductsContract.NAME+" TEXT NOT NULL," +
                ProductsContract.PRICE+" REAL NOT NULL," +
                ProductsContract.DESCRIPTION+" TEXT DEFAULT 'No description available yet'," +
                ProductsContract.QUANTITY+" INTEGER," +
                "PRIMARY KEY("+ProductsContract.PRODUCT_ID+")" +
                ");";

        String CREATE_ORDERS_TABLE = "CREATE TABLE " +ProductsContract.ORDERS+
                "( "+ProductsContract.PRODUCT_ID+" INTEGER,"
                 +ProductsContract.ORDER_DATE+" TEXT ," +
                " PRIMARY KEY("+ProductsContract.PRODUCT_ID+ ") )";

        String CREATE_ORDER_ITEMS_TABLE ="CREATE TABLE "+ProductsContract.ORDER_ITEMS
                +" ( "+ProductsContract.ORDERID+" INTEGER," +
                 ProductsContract.PRODUCTID+" TEXT," +
                ProductsContract.QUANTITY+" INTEGER )";


        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_ITEMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsContract.PRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsContract.ORDERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsContract.ORDER_ITEMS);
        onCreate(sqLiteDatabase);
    }
}
