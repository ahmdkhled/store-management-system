package com.ahmdkhled.storemanagmentsystem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {

    public static final String DBNAME = "store.db";
    public static final int DBVERSION = 1;

    public Dbhelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE `items` (" +
                "`itemId`INTEGER," +
                "`itemName`TEXT NOT NULL," +
                "`itemPrice`REAL NOT NULL," +
                "`itemDescription`TEXT DEFAULT 'No description available yet'," +
                "`otemQuantity`INTEGER," +
                "PRIMARY KEY(`itemId`)" +
                ");";

        String CREATE_ORDERS_TABLE = "CREATE TABLE `orders` ( `oriderId` INTEGER, `orderDate` TEXT DEFAULT 'getDate()', PRIMARY KEY(`oriderId`) )";
        String CREATE_ORDER_ITEMS_TABLE ="CREATE TABLE orderItem ( `orderId` INTEGER, `itemId` INTEGER, `quantity` INTEGER )";

        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_ITEMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
