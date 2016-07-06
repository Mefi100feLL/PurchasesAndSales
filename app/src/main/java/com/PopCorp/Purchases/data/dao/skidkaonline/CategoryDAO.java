package com.PopCorp.Purchases.data.dao.skidkaonline;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.skidkaonline.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 06.07.2016.
 */
public class CategoryDAO {


    public static final String TABLE_CATEGORIES = "skidkaonline_categories";

    public static final String KEY_CATEGORY_NAME = "name";
    public static final String KEY_CATEGORY_URL = "url";
    public static final String KEY_CATEGORY_CITY_URL = "city_url";
    public static final String KEY_CATEGORY_CITY_ID = "city_id";

    public static final String[] COLUMNS_CATEGORIES = new String[]{
            KEY_CATEGORY_NAME,
            KEY_CATEGORY_URL,
            KEY_CATEGORY_CITY_URL,
            KEY_CATEGORY_CITY_ID
    };

    public static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_CATEGORY_NAME + " text, " +
            KEY_CATEGORY_URL + " text, " +
            KEY_CATEGORY_CITY_URL + " text, " +
            KEY_CATEGORY_CITY_ID + " integer);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    private String[] values(Category category){
        return new String[]{
                category.getName(),
                category.getUrl(),
                category.getCityUrl(),
                String.valueOf(category.getCityId())
        };
    }

    private long addRec(Category category){
        return db.addRec(TABLE_CATEGORIES, COLUMNS_CATEGORIES, values(category));
    }

    public long updateOrAddToDB(Category category) {
        int countUpdated = db.update(TABLE_CATEGORIES, COLUMNS_CATEGORIES, KEY_CATEGORY_URL + "='" + category.getUrl() + "' AND " + KEY_CATEGORY_CITY_ID + "=" + category.getCityId(), values(category));
        if (countUpdated == 0) {
            return addRec(category);
        }
        return countUpdated;
    }

    public int remove(Category category) {
        return db.deleteRows(TABLE_CATEGORIES, KEY_CATEGORY_URL + "='" + category.getUrl() + "' AND " + KEY_CATEGORY_CITY_ID + "=" + category.getCityId());
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
                cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_URL)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_CITY_URL)),
                cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_CITY_ID))
        );
    }

    public Category getCategory(String url, int cityId) {
        Category result = null;
        Cursor cursor = db.getData(TABLE_CATEGORIES, COLUMNS_CATEGORIES, KEY_CATEGORY_URL + "='" + url + "' AND " + KEY_CATEGORY_CITY_ID + "=" + cityId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result =  getCategory(cursor);
            }
            cursor.close();
        }
        return result;
    }
}
