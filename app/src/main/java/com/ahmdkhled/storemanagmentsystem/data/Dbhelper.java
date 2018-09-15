package com.ahmdkhled.storemanagmentsystem.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.common.stats.ConnectionTracker;

public class Dbhelper extends SQLiteOpenHelper {

    public static final String DBNAME = "store.db";
    public static final int DBVERSION = 9;

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

        String CREATE_CATEGORY_TABLE = "CREATE TABLE "+ProductsContract.categoryTable
                +" ( "+ProductsContract.categoryID+" INTEGER PRIMARY KEY, "+
                ProductsContract.categoryName+" TEXT )";


        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsContract.PRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsContract.ORDERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsContract.ORDER_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsContract.categoryTable);
        onCreate(sqLiteDatabase);
    }

    public Cursor showOrderItem(String orderId){
        SQLiteDatabase db = getReadableDatabase();
        String MY_QUERY = "SELECT * FROM "+ProductsContract.PRODUCTS+
                " INNER JOIN "+ProductsContract.ORDER_ITEMS+" ON "+
                ProductsContract.PRODUCTS+"."+ProductsContract.PRODUCT_ID+"="+
                ProductsContract.ORDER_ITEMS+"."+ProductsContract.PRODUCTID+
                " WHERE "+ProductsContract.ORDER_ITEMS+"."+ProductsContract.ORDERID+"=?";

       return db.rawQuery(MY_QUERY,new String[]{orderId});
    }
}
