package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public static final String TABLE_ALL_ITEMS = "AllItems";

    public static final String KEY_ALL_ITEMS_NAME = "name";
    public static final String KEY_ALL_ITEMS_COUNT = "count";
    public static final String KEY_ALL_ITEMS_EDIZM = "edizm";
    public static final String KEY_ALL_ITEMS_COAST = "coast";
    public static final String KEY_ALL_ITEMS_CATEGORY = "category";
    public static final String KEY_ALL_ITEMS_SHOP = "shop";
    public static final String KEY_ALL_ITEMS_COMMENT = "comment";
    public static final String KEY_ALL_ITEMS_FAVORITE = "favorite";

    public static final String[] COLUMNS_ALL_ITEMS = new String[]{
            KEY_ALL_ITEMS_NAME,
            KEY_ALL_ITEMS_COUNT,
            KEY_ALL_ITEMS_EDIZM,
            KEY_ALL_ITEMS_COAST,
            KEY_ALL_ITEMS_CATEGORY,
            KEY_ALL_ITEMS_SHOP,
            KEY_ALL_ITEMS_COMMENT,
            KEY_ALL_ITEMS_FAVORITE
    };

    public static final String CREATE_TABLE_ALL_ITEMS = "CREATE TABLE IF NOT EXISTS " + TABLE_ALL_ITEMS +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_ALL_ITEMS_NAME + " text, " +
            KEY_ALL_ITEMS_COUNT + " integer, " +
            KEY_ALL_ITEMS_EDIZM + " text, " +
            KEY_ALL_ITEMS_COAST + " integer, " +
            KEY_ALL_ITEMS_CATEGORY + " text, " +
            KEY_ALL_ITEMS_SHOP + " text, " +
            KEY_ALL_ITEMS_COMMENT + " text, " +
            KEY_ALL_ITEMS_FAVORITE + " boolean);";

    private DB db;
    private ListItemCategoryDAO categoryDAO;

    public ProductDAO(){
        db = DB.getInstance();
        categoryDAO = new ListItemCategoryDAO();
    }

    public ProductDAO(SQLiteDatabase db){
        this.db = new DB(db);
        categoryDAO = new ListItemCategoryDAO(db);
    }

    public long updateOrAddToDB(Product product) {
        String categoryId = "-1";
        if (product.getCategory() != null){
            categoryId = String.valueOf(product.getCategory().getId());
        }
        String[] values = new String[]{
                product.getName(),
                String.valueOf(product.getCount()),
                product.getEdizm(),
                String.valueOf(product.getCoast()),
                categoryId,
                product.getShop(),
                product.getComment(),
                String.valueOf(product.isFavorite())
        };
        int countUpdated = db.update(TABLE_ALL_ITEMS, COLUMNS_ALL_ITEMS, KEY_ALL_ITEMS_NAME + "='" + product.getName() + "'", values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_ALL_ITEMS, COLUMNS_ALL_ITEMS, values);
        }
        return countUpdated;
    }

    public int remove(Product category) {
        return db.deleteRows(TABLE_ALL_ITEMS, DB.KEY_ID + "=" + category.getId());
    }

    public List<Product> getAllProducts() {
        ArrayList<Product> result = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_ALL_ITEMS);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getProduct(cursor));
                while (cursor.moveToNext()) {
                    result.add(getProduct(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private Product getProduct(Cursor cursor) {
        return new Product(
                cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_ALL_ITEMS_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_ALL_ITEMS_COUNT)),
                cursor.getString(cursor.getColumnIndex(KEY_ALL_ITEMS_EDIZM)),
                cursor.getString(cursor.getColumnIndex(KEY_ALL_ITEMS_COAST)),
                categoryDAO.getWithId(cursor.getLong(cursor.getColumnIndex(KEY_ALL_ITEMS_CATEGORY))),
                cursor.getString(cursor.getColumnIndex(KEY_ALL_ITEMS_SHOP)),
                cursor.getString(cursor.getColumnIndex(KEY_ALL_ITEMS_COMMENT)),
                Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_ALL_ITEMS_FAVORITE)))
        );
    }
}
