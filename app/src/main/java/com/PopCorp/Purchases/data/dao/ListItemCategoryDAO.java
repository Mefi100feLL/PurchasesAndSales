package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.ListItemCategory;

import java.util.ArrayList;
import java.util.List;

public class ListItemCategoryDAO {

    public static final String TABLE_CATEGORIES = "Categories";

    public static final String KEY_CATEGS_NAME = "name";
    public static final String KEY_CATEGS_COLOR = "color";

    public static final String[] COLUMNS_CATEGS = new String[]{
            KEY_CATEGS_NAME,
            KEY_CATEGS_COLOR
    };

    public static final String CREATE_TABLE_CATEGS = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_CATEGS_NAME + " text, " +
            KEY_CATEGS_COLOR + " integer);";

    private DB db;

    public ListItemCategoryDAO(){
        db = DB.getInstance();
    }

    public ListItemCategoryDAO(SQLiteDatabase db){
        this.db = new DB(db);
    }

    public long updateOrAddToDB(ListItemCategory category) {
        String[] values = new String[]{
                category.getName(),
                String.valueOf(category.getColor())
        };
        int countUpdated = db.update(TABLE_CATEGORIES, COLUMNS_CATEGS, DB.KEY_ID + "=" + category.getId(), values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_CATEGORIES, COLUMNS_CATEGS, values);
        }
        return countUpdated;
    }

    public int remove(ListItemCategory category) {
        return db.deleteRows(TABLE_CATEGORIES, DB.KEY_ID + "=" + category.getId());
    }

    public List<ListItemCategory> getAllCategories() {
        ArrayList<ListItemCategory> result = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_CATEGORIES);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getListItemCategory(cursor));
                while (cursor.moveToNext()) {
                    result.add(getListItemCategory(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private ListItemCategory getListItemCategory(Cursor cursor) {
        return new ListItemCategory(
                cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGS_NAME)),
                cursor.getInt(cursor.getColumnIndex(KEY_CATEGS_COLOR))
        );
    }

    public ListItemCategory getWithId(long id) {
        ListItemCategory result = null;
        Cursor cursor = db.getData(TABLE_CATEGORIES, DB.KEY_ID + "=" + id);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = getListItemCategory(cursor);
            }
            cursor.close();
        }
        return result;
    }

    public ListItemCategory getWithName(String name) {
        ListItemCategory result = null;
        Cursor cursor = db.getData(TABLE_CATEGORIES, KEY_CATEGS_NAME + "='" + name + "'");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = getListItemCategory(cursor);
            }
            cursor.close();
        }
        return result;
    }
}
