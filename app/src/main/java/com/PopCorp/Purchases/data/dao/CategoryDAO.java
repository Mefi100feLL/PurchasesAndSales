package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Sale;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static final String TABLE_CATEGORIES = "Sale_categories";

    public static final String KEY_CATEGORY_TYPE = "type";
    public static final String KEY_CATEGORY_ID = "id";
    public static final String KEY_CATEGORY_NAME = "name";
    public static final String KEY_CATEGORY_IMAGE_URL = "image_url";
    public static final String KEY_CATEGORY_FAVORITE = "favorite";

    public static final String[] COLUMNS_SALE_CATEGS = new String[]{
            KEY_CATEGORY_TYPE,
            KEY_CATEGORY_ID,
            KEY_CATEGORY_NAME,
            KEY_CATEGORY_IMAGE_URL,
            KEY_CATEGORY_FAVORITE
    };

    public static final String[] COLUMNS_SALE_CATEGS_WITHOUT_FAVORITE = new String[]{
            KEY_CATEGORY_TYPE,
            KEY_CATEGORY_ID,
            KEY_CATEGORY_NAME,
            KEY_CATEGORY_IMAGE_URL
    };

    public static final String CREATE_TABLE_SALE_CATEGS = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_CATEGORY_TYPE + " integer, " +
            KEY_CATEGORY_ID + " integer, " +
            KEY_CATEGORY_NAME + " text, " +
            KEY_CATEGORY_IMAGE_URL + " text, " +
            KEY_CATEGORY_FAVORITE + " text);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    private String[] values(Category category){
        return new String[]{
                String.valueOf(category.getType()),
                String.valueOf(category.getId()),
                category.getName(),
                category.getImageUrl(),
                String.valueOf(category.isFavorite())
        };
    }

    private String[] valuesWithoutFavorite(Category category){
        return new String[]{
                String.valueOf(category.getType()),
                String.valueOf(category.getId()),
                category.getName(),
                category.getImageUrl()
        };
    }

    private long addRec(Category category){
        return db.addRec(TABLE_CATEGORIES, COLUMNS_SALE_CATEGS, values(category));
    }

    public long updateOrAddToDB(Category category) {
        int countUpdated = db.update(TABLE_CATEGORIES, COLUMNS_SALE_CATEGS, KEY_CATEGORY_ID + "=" + category.getId() + " AND " + KEY_CATEGORY_TYPE + "=" + category.getType(), values(category));
        if (countUpdated == 0) {
            return addRec(category);
        }
        return countUpdated;
    }

    public long updateOrAddToDBWithoutFavorite(Category category) {
        int countUpdated = db.update(TABLE_CATEGORIES, COLUMNS_SALE_CATEGS_WITHOUT_FAVORITE, KEY_CATEGORY_ID + "=" + category.getId() + " AND " + KEY_CATEGORY_TYPE + "=" + category.getType(), valuesWithoutFavorite(category));
        if (countUpdated == 0) {
            return addRec(category);
        }
        return countUpdated;
    }

    public int remove(Category category) {
        return db.deleteRows(TABLE_CATEGORIES, KEY_CATEGORY_ID + "=" + category.getId() + " AND " + KEY_CATEGORY_TYPE + "=" + category.getType());
    }

    public List<Category> getAllCategories() {
        ArrayList<Category> result = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_CATEGORIES);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getCategory(cursor));
                while (cursor.moveToNext()) {
                    result.add(getCategory(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private Category getCategory(Cursor cursor) {
        return Category.create(
                cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME)),
                cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_TYPE)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_IMAGE_URL)),
                Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_FAVORITE)))
        );
    }

    public void addAllCategories(List<Category> categories) {
        db.beginTransaction();
        try {
            for (Category category : categories) {
                updateOrAddToDBWithoutFavorite(category);
            }
            db.successFull();
        } finally {
            db.endTransaction();
        }
    }

    public Category getCategory(int id, int type) {
        Category result = null;
        Cursor cursor = db.getData(TABLE_CATEGORIES, COLUMNS_SALE_CATEGS, KEY_CATEGORY_ID + "=" + id + " AND " + KEY_CATEGORY_TYPE + "=" + type);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result =  getCategory(cursor);
            }
            cursor.close();
        }
        return result;
    }

    public Category getCategory(Sale sale) {
        return getCategory(sale.getCategoryId(), sale.getCategoryType());
    }
}
