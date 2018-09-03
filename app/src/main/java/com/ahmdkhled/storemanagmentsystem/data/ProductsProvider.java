package com.ahmdkhled.storemanagmentsystem.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ProductsProvider extends ContentProvider {

    static UriMatcher uriMatcher;
    Dbhelper dbhelper;
    public static final int PRODUCTS=1;
    public static final int ORDERS=1;
    public static final int ORDER_ITEMS=1;


    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ProductsContract.AUTHORITY,ProductsContract.PRODUCTS_PATH,PRODUCTS);
        uriMatcher.addURI(ProductsContract.AUTHORITY,ProductsContract.ORDERS_PATH,ORDERS);
        uriMatcher.addURI(ProductsContract.AUTHORITY,ProductsContract.ORDERITEMS_PATH,ORDER_ITEMS);
    }

    @Override
    public boolean onCreate() {
        dbhelper=new Dbhelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        if (uriMatcher.match(uri)==PRODUCTS){
            long id=database.insert(ProductsContract.PRODUCTS,null,contentValues);
            Log.d("INSERT","id "+id);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
