package com.example.hercules.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "Cart.db";
    private static final int DB_VER = 1;
    String TAG = "Database";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        Log.d(TAG, "getCarts: getting database");
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductID", "Image", "ProductName", "Quantity", "Price", "Multiplier"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductID")),
                        (c.getInt(c.getColumnIndex("Image"))),
                        (c.getString(c.getColumnIndex("ProductName"))),
                        (c.getInt(c.getColumnIndex("Quantity"))),
                        (c.getInt(c.getColumnIndex("Price"))),
                        (c.getInt(c.getColumnIndex("Multiplier")))
                ));
            } while (c.moveToNext());
        }
        db.close();
        return result;
    }

    public void addToCart(Order order) {
        Log.d(TAG, "addToCart: adding product to cart");
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail (ProductID, Image, ProductName, Quantity, Price, Multiplier) VALUES('%s','%d','%s','%d','%d','%d');",
                order.getProductID(),
                order.getImageResource(),
                order.getProductName().replace("'", "''"),
                order.getQuantity(),
                order.getPrice(),
                order.getMultiplier());
        db.execSQL(query);
        db.close();

    }

    public void cleanCart() {
        Log.d(TAG, "cleanCart: cleaning cart");
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM OrderDetail";
        db.execSQL(query);
        db.close();


    }

    public boolean hasObject(String id) {
        Log.d(TAG, "hasObject: checking whether an product is in Database or not");
        Log.d(TAG, "hasObject: checking whether " + id +" exists in database or not");
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM OrderDetail" + " WHERE ProductID" + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[]{id});

        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            //here, count is records found
            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    public void deleteFromDatabase(String id) {
        Log.d(TAG, "deleteFromDatabase: delete the product from database");
        Log.d(TAG, "deleteFromDatabase: deleting " + id + " from database");
        SQLiteDatabase db = getWritableDatabase();
        db.delete("OrderDetail", "ProductID=?", new String[]{id});
        db.close();
    }

    public void updateQuantity(String id, int quantity) {
        Log.d(TAG, "updateQuantity: Updating Quantity");
        ContentValues data = new ContentValues();
        data.put("Quantity", quantity);
        SQLiteDatabase db = getWritableDatabase();
        Log.d(TAG, "updateQuantity: "+quantity);
        db.update("OrderDetail", data, "ProductID = ?", new String[]{id});
        db.close();
    }

    public int getQuantity(String id) {
        Log.d(TAG, "getQuantity: getting quantity from database for product id " + id);
        String selectString = "SELECT Quantity FROM OrderDetail" + " WHERE ProductID" + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectString, new String[]{id});
        int quantity = 0;
        if (c != null) {
            c.moveToFirst();
            quantity = c.getInt(c.getColumnIndex("Quantity"));//
            c.close();
        }
        db.close();
        return quantity;
    }

    public long count() {
        Log.d(TAG, "count: counting the no of items in database");
        String selectString = "SELECT * FROM OrderDetail";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectString, null);
        long count = 0;
        if (c != null && !tableContainsAnything()) {
            count = DatabaseUtils.queryNumEntries(db, "OrderDetail");
            db.close();
            c.close();
        }
        db.close();
        return count;
    }
    public boolean tableContainsAnything() {
        Log.d(TAG, "tableContainsAnything: checking whether database contains anything or not");
        boolean empty = true;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM OrderDetail", null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
            cur.close();
        }
        return empty;
    }
}
