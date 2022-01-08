package com.example.hoteleats;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "HotelEats.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE = "user";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ACCESS = "access";

    private static final String PRODUCT_TABLE = "product";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_STOCK = "stock";

    private static final String HISTORY_TABLE = "history";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";
    private static final String COLUMN_PURCHASED_PRODUCT = "purchased_product";
    private static final String COLUMN_TOTAL_AMT = "total_amt";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        this.preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + USER_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ACCESS + " TEXT);";

        db.execSQL(query);

        query = "CREATE TABLE " + PRODUCT_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_STOCK + " TEXT);";

        db.execSQL(query);

        query = "CREATE TABLE " + HISTORY_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_NAME + " TEXT, " +
                COLUMN_PURCHASED_PRODUCT + " TEXT, " +
                COLUMN_TOTAL_AMT + " TEXT, " +
                COLUMN_TIMESTAMP + " TEXT);";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        onCreate(db);
    }

    void register(String name, String password, String access) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PASSWORD, password);
        cv.put(COLUMN_ACCESS, access);

        long result = db.insert(USER_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
        } else  {
            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();
        }

    }

    void addProduct(String name, String price, String stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_STOCK, stock);

        long result = db.insert(PRODUCT_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed to add product", Toast.LENGTH_SHORT).show();
        } else  {
            Toast.makeText(context, "Product add successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void generateHistory(History history) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUSTOMER_NAME, history.getName());
        cv.put(COLUMN_PURCHASED_PRODUCT, history.getProductList());
        cv.put(COLUMN_TOTAL_AMT, history.getTotalPrice());
        cv.put(COLUMN_TIMESTAMP, history.getTime());

        long result = db.insert(HISTORY_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed to purchase product", Toast.LENGTH_SHORT).show();
        } else  {
            Toast.makeText(context, "Product purchased successfully", Toast.LENGTH_SHORT).show();

            editor.remove("cart");
            editor.commit();

            Intent intent = new Intent(context, OrderSuccess.class);

            intent.putExtra("itemList", history.getProductList());
            intent.putExtra("totalPrice", history.getTotalPrice());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

    }

    void updateStock(String stock, String row_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STOCK, stock);

        long result = db.update(PRODUCT_TABLE, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update stock", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Stock updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor getAllHistory() {
        String query = "SELECT * FROM " + HISTORY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }


    Cursor getUserData(String username, String password) {
        String query = "SELECT * FROM " + USER_TABLE +
                " WHERE " + COLUMN_NAME + " = " + "'" + username + "'" +
                " AND " + COLUMN_PASSWORD + " = " + "'" + password + "';";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    Cursor getAllProducts() {
        String query = "SELECT * FROM " + PRODUCT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;

    }

    Cursor getAllProductsInStock() {
        String query = "SELECT * FROM " + PRODUCT_TABLE + " WHERE " + COLUMN_STOCK + " = 'Stock In';";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
