package codeztalk.elbasha.delegate.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.activities.categoryProduct.CategoryProductModel;
import codeztalk.elbasha.delegate.activities.categoryProduct.Product;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.ProductModel;


public class ForsahDB extends SQLiteOpenHelper {

    private static final String DBName = "ba89.db";
    private static final int version = 1;

    private static final String product_Table_Create = "CREATE TABLE IF NOT EXISTS product (id INTEGER PRIMARY KEY AUTOINCREMENT,productId INTEGER,m$id TEXT,productName TEXT,productEN TEXT,productCode TEXT,productUnitPrice DOUBLE,productBoxPrice DOUBLE,boxUnit INTEGER,productQuantity INTEGER,type INTEGER,unitStockQty INTEGER,categoryName TEXT,categoryAR TEXT,OfficialUnitPrice DOUBLE)";
    private static final String category_Table_Create = "CREATE TABLE IF NOT EXISTS category (id INTEGER PRIMARY KEY AUTOINCREMENT,categoryName TEXT,categoryEN TEXT)";
    private static final String client_Table_Create = "CREATE TABLE IF NOT EXISTS client (id INTEGER PRIMARY KEY AUTOINCREMENT,clientId INTEGER,clientName TEXT)";

    private static final String product_Table_Drop = "drop table if exists product";
    private static final String category_Table_Drop = "drop table if exists category";
    private static final String client_Table_Drop = "drop table if exists client";


    private SQLiteDatabase database;
    private Context fContext = null;


    public ForsahDB(Context context) {
        super(context, DBName, null, version);
        fContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            database = db;
            db.execSQL(client_Table_Create);
            db.execSQL(product_Table_Create);
            db.execSQL(category_Table_Create);


        } catch (SQLException e) {

            Toast.makeText(null, "Reason :" + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

            db.execSQL(product_Table_Drop);
            db.execSQL(category_Table_Drop);
            db.execSQL(client_Table_Drop);


            onCreate(db);
        } catch (SQLException e) {
            Toast.makeText(null, "Reason :" + e, Toast.LENGTH_LONG).show();
        }
    }



    public void addToClient(List<ClientModel> inventoryList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (ClientModel productModel : inventoryList) {
            contentValues.put("clientId", productModel.getId());
            contentValues.put("clientName", productModel.getClientName());

            db.insert("client", null, contentValues);
        }


    }

    public ArrayList<ClientModel> getAllClients() {
        ArrayList<ClientModel> array_list = new ArrayList();
        database = this.getReadableDatabase();

        String Query = "select * from client";


        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int id = cursor.getInt(0);
            int employeeId = cursor.getInt(1);
            String employeeName = cursor.getString(2);

            cursor.moveToNext();
            array_list.add(new ClientModel(id, employeeId, employeeName));
        }
        return array_list;
    }

    public int getClientCount() {
        String countQuery = "select * from client";

        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteClientTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete  from client");
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addToCategory(List<CategoryProductModel> inventoryList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (CategoryProductModel productModel : inventoryList) {
            contentValues.put("categoryName", productModel.getCategoryNameAR());
            contentValues.put("categoryEN", productModel.getCategoryNameEN());


            db.insert("category", null, contentValues);
        }


    }

    public ArrayList<CategoryProductModel> getAllCategory() {
        ArrayList<CategoryProductModel> array_list = new ArrayList();
        database = this.getReadableDatabase();

        String Query = "select * from category";


        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int id = cursor.getInt(0);
            String categoryName = cursor.getString(1);
            String categoryEN = cursor.getString(2);

            cursor.moveToNext();
            array_list.add(new CategoryProductModel(id, categoryName, categoryEN));
        }
        return array_list;
    }




    public void addToCategoryProducts(List<Product> inventoryList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (Product productModel : inventoryList) {
            contentValues.put("productId", productModel.getId());
            contentValues.put("m$id", productModel.get$id());
            contentValues.put("productName", productModel.getProductNameAR());
            contentValues.put("productEN", productModel.getProductNameEN());
            contentValues.put("productCode", productModel.getProductCode());
            contentValues.put("productUnitPrice", productModel.getUnitPrice());
            contentValues.put("productBoxPrice", productModel.getBoxPrice());
            contentValues.put("boxUnit", productModel.getBoxUnit());
            contentValues.put("productQuantity", productModel.getProductAmount());
            contentValues.put("type", productModel.getType());
            contentValues.put("unitStockQty", productModel.getUnitStockQty());
            contentValues.put("categoryName", productModel.getCategoryNameEN());
            contentValues.put("categoryAR", productModel.getCategoryNameAR());
            contentValues.put("OfficialUnitPrice", productModel.getUnitPrice());

            db.insert("product", null, contentValues);
        }


    }

    public void updateProductQuantity(int productId, int quantity) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE  product SET productQuantity = " + quantity + " where productId=" + productId);
            db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductType(int productId, int type) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE  product SET type = " + type + " where productId=" + productId);
            db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductPrice(int productId, double productPrice) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE  product SET productUnitPrice = " + productPrice + " where productId=" + productId);
            db.close();
            Log.e("UPDATE"," >> ");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalPrice(int pos, double totalPrice) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE  product SET totalPrice = " + totalPrice + " where id=" + pos);
            db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAllProducts() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE  product SET productQuantity = 0 ");
            db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Product> getAllCategoryProducts(String catName) {
        ArrayList<Product> array_list = new ArrayList();
        database = this.getReadableDatabase();

        String Query = "select * from product where categoryName = '" +catName+"'";
//            db.execSQL("delete from product where id ='" + id + "'");

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {


            int id = cursor.getInt(0);
            int mId = cursor.getInt(1);
            String m$id = cursor.getString(2);
            String mProductNameAR = cursor.getString(3);
            String mProductNameEN = cursor.getString(4);
            String mProductCode = cursor.getString(5);
            double mUnitPrice = cursor.getDouble(6);
            double mBoxPrice = cursor.getDouble(7);
            int mBoxUnit = cursor.getInt(8);
            int productAmount = cursor.getInt(9);
            int type = cursor.getInt(10);
            int unitStockQty = cursor.getInt(11);
//            int unitStockQty = 120;
            String categoryName = cursor.getString(12);
            String categoryAR = cursor.getString(13);
            double officialUnitPrice = cursor.getDouble(14);


            cursor.moveToNext();


            array_list.add(new Product(id, mId, m$id,
                    mProductNameAR,
                    mProductNameEN,
                    mProductCode,
                    mBoxPrice,
                    mUnitPrice,
                    mBoxUnit,
                    productAmount,
                    type, unitStockQty,categoryName,categoryAR,officialUnitPrice));
        }
        return array_list;
    }



    public ArrayList<Product> getSelectedCategoryProducts() {
        ArrayList<Product> array_list = new ArrayList();
        database = this.getReadableDatabase();

        String Query = "select * from product where productQuantity > 0";

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {


            int id = cursor.getInt(0);
            int mId = cursor.getInt(1);
            String m$id = cursor.getString(2);
            String mProductNameAR = cursor.getString(3);
            String mProductNameEN = cursor.getString(4);
            String mProductCode = cursor.getString(5);
            double mUnitPrice = cursor.getDouble(6);
            double mBoxPrice = cursor.getDouble(7);
            int mBoxUnit = cursor.getInt(8);
            int productAmount = cursor.getInt(9);
            int type = cursor.getInt(10);
            int unitStockQty = cursor.getInt(11);
            String categoryName = cursor.getString(12);
            String categoryAR = cursor.getString(13);
            double officialUnitPrice = cursor.getDouble(14);


            cursor.moveToNext();


            array_list.add(new Product(id, mId, m$id,
                    mProductNameAR,
                    mProductNameEN,
                    mProductCode,
                    mBoxPrice,
                    mUnitPrice,
                    mBoxUnit,
                    productAmount,
                    type, unitStockQty,categoryName,categoryAR,officialUnitPrice));

        }
        return array_list;
    }


    public ArrayList<ProductModel> getSelectedProducts() {
        ArrayList<ProductModel> array_list = new ArrayList();
        database = this.getReadableDatabase();

        String Query = "select * from product where productQuantity > 0";

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {


            int id = cursor.getInt(0);
            int mId = cursor.getInt(1);
            String m$id = cursor.getString(2);
            String mProductNameAR = cursor.getString(3);
            String mProductNameEN = cursor.getString(4);
            String mProductCode = cursor.getString(5);
            double mUnitPrice = cursor.getDouble(6);
            double mBoxPrice = cursor.getDouble(7);
            int mBoxUnit = cursor.getInt(8);
            int productAmount = cursor.getInt(9);
            int type = cursor.getInt(10);
            int unitStockQty = cursor.getInt(11);


            cursor.moveToNext();


            array_list.add(new ProductModel(id, mId, m$id,
                    mProductNameAR,
                    mProductNameEN,
                    mProductCode,
                    mBoxPrice,
                    mUnitPrice,
                    mBoxUnit,
                    productAmount,
                    type, unitStockQty));

        }
        return array_list;
    }

    public double getTotalPrice() {
        Cursor cur = database.rawQuery("SELECT SUM(productBoxPrice) FROM product", null);
        if (cur.moveToFirst()) {
            return cur.getDouble(0);
        }
        return 0;
    }


    public int getProductsCount() {
        String countQuery = "select * from product";

        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public void deleteProductTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete  from product");
            db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategoryTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete  from category");
            db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
