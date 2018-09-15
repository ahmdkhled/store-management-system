package com.ahmdkhled.storemanagmentsystem.data;

import android.content.ContentResolver;
import android.net.Uri;

public class ProductsContract {

    public static final String AUTHORITY="com.ahmdkhled.storemanagmentsystem.ProductsProvider";
    public static final String PRODUCTS_PATH="products";
    public static final String ORDERS_PATH="order";
    public static final String ORDERITEMS_PATH="order_items";
    public static final String CATEGORY_PATH="categorys";

    public static Uri productsUri=Uri.parse("content://"+AUTHORITY+"/"+PRODUCTS_PATH);
    public static Uri ordersUri=Uri.parse("content://"+AUTHORITY+"/"+ORDERS_PATH);
    public static Uri orderItemssUri=Uri.parse("content://"+AUTHORITY+"/"+ORDERITEMS_PATH);
    public static Uri categoryUri=Uri.parse("content://"+AUTHORITY+"/"+CATEGORY_PATH);

    public static final String PRODUCTS_MIME_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+AUTHORITY+"/"+PRODUCTS_PATH;

    public static final String PRODUCTS="products";

    public static final String PRODUCT_ID="id";
    public static final String NAME="name";
    public static final String PRICE="price";
    public static final String QUANTITY="quantity";
    public static final String DESCRIPTION="desription";


    public static final String ORDERS="orders";
    public static final String ORDER_ID="id";
    public static final String ORDER_DATE="date";

    public static final String ORDER_ITEMS="order_items";

    public static final String ORDERID="order_id";
    public static final String PRODUCTID="product_id";
    public static final String ORDER_ITEM_QUANTITY="quantity";

    // category table
    public static final String categoryTable = "categpry_table";
    public static final String categoryID = "categpry_id";
    public static final String categoryName = "categpry_name";

}
