package com.ahmdkhled.storemanagmentsystem.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class ProductsProvider extends ContentProvider {

    static UriMatcher uriMatcher;
    Dbhelper dbhelper;
    public static final int PRODUCTS=101;
    public static final int ORDERS=201;
    public static final int ORDER_ITEMS=301;
    private static final int PRODUCT_WITH_ID =102 ;

    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ProductsContract.AUTHORITY,ProductsContract.PRODUCTS_PATH,PRODUCTS);
        uriMatcher.addURI(ProductsContract.AUTHORITY,ProductsContract.PRODUCTS_PATH+"/#",PRODUCT_WITH_ID);
        uriMatcher.addURI(ProductsContract.AUTHORITY,ProductsContract.ORDERS_PATH,ORDERS);
//        matcher.addURI(ProductsContract.AUTHORITY,ProductsContract.ORDERS_PATH+"/#",TASK_WITH_ID);
        uriMatcher.addURI(ProductsContract.AUTHORITY,ProductsContract.ORDERITEMS_PATH,ORDER_ITEMS);
//        matcher.addURI(ProductsContract.AUTHORITYProductsContract.ORDERITEMS_PATH+"/#",TASK_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        dbhelper=new Dbhelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selections
            , @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database=dbhelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        if (match==PRODUCTS){
            cursor= database.query(ProductsContract.PRODUCTS,columns,
                    selections,selectionArgs,null,null,null);
            cursor.setNotificationUri(getContext().getContentResolver(),ProductsContract.productsUri);
            return cursor;
        }
        else if(match==PRODUCT_WITH_ID){
            String id = uri.getPathSegments().get(1);
            String mSelection = ProductsContract.PRODUCT_ID+"=?";
            String[]mArgs = new String[]{id};
            cursor = database.query(ProductsContract.PRODUCTS,columns,mSelection,mArgs,null,null,sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(),ProductsContract.productsUri);
            return cursor;
        }


        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (uriMatcher.match(uri)==PRODUCTS){
            return ProductsContract.PRODUCTS_MIME_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        long id=-10;
        if (uriMatcher.match(uri)==PRODUCTS){
            id=database.insert(ProductsContract.PRODUCTS,null,contentValues);
            Log.d("INSERT","id "+id);
        }else if (uriMatcher.match(uri)==ORDERS){
            id=database.insert(ProductsContract.ORDERS,null,contentValues);
            Log.d("INSERT","id "+id);
        }else if (uriMatcher.match(uri)==ORDERS){
            id=database.insert(ProductsContract.ORDER_ITEMS,null,contentValues);
            Log.d("INSERT","id "+id);
        }
        if (id>0){
            getContext().getContentResolver().notifyChange(uri, null);
            Toast.makeText(getContext(), "successfully saved", Toast.LENGTH_SHORT).show();
            return ContentUris.withAppendedId(ProductsContract.productsUri,id);
        }else {
            Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
            return uri;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        if (uriMatcher.match(uri)==PRODUCTS){
            getContext().getContentResolver().notifyChange(uri,null);
            return database.delete(ProductsContract.PRODUCTS,where,whereArgs);
        }else if (uriMatcher.match(uri)==ORDERS){
            getContext().getContentResolver().notifyChange(uri,null);
           return database.delete(ProductsContract.ORDERS,where,whereArgs);
        }else if (uriMatcher.match(uri)==ORDER_ITEMS){
            getContext().getContentResolver().notifyChange(uri,null);
            return database.delete(ProductsContract.ORDER_ITEMS,where,whereArgs);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int mId;
        switch (match){
            case PRODUCT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                mId = database.update(ProductsContract.PRODUCTS,contentValues,ProductsContract.PRODUCT_ID+"=?",new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("invalid uri");
        }

        // if deleted record exist
        if(mId != 0){
            getContext().getContentResolver().notifyChange(uri,null);
            Toast.makeText(getContext(), "successfully updated", Toast.LENGTH_SHORT).show();
            return mId;
        }
        // if record not exist
        else {
            Toast.makeText(getContext(), "something went wrong ", Toast.LENGTH_SHORT).show();
            return 0;
        }

    }
}
