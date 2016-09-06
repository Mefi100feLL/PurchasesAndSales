package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.ListItemSale;

import java.util.ArrayList;
import java.util.List;

public class ListItemSaleDAO {

    public static final String TABLE_LIST_SALES = "list_sales";

    public static final String KEY_CATEGS_IMAGE = "image";
    public static final String KEY_CATEGS_PERIOD_START = "period_start";
    public static final String KEY_CATEGS_PERIOD_END = "period_end";

    public static final String[] COLUMNS_LIST_SALES = new String[]{
            KEY_CATEGS_IMAGE,
            KEY_CATEGS_PERIOD_START,
            KEY_CATEGS_PERIOD_END
    };

    public static final String CREATE_TABLE_SALES = "CREATE TABLE IF NOT EXISTS " + TABLE_LIST_SALES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_CATEGS_IMAGE + " text, " +
            KEY_CATEGS_PERIOD_START + " text, " +
            KEY_CATEGS_PERIOD_END + " text);" ;

    private DB db = DB.getInstance();

    public long updateOrAddToDB(ListItemSale sale) {
        String[] values = new String[]{
                sale.getImage(),
                sale.getPeriodStart(),
                sale.getPeriodEnd()
        };
        int countUpdated = db.update(TABLE_LIST_SALES, COLUMNS_LIST_SALES, DB.KEY_ID + "=" + sale.getId(), values);
        if (countUpdated == 0) {
            long id = db.addRec(TABLE_LIST_SALES, COLUMNS_LIST_SALES, values);
            sale.setId(id);
            return id;
        }
        return countUpdated;
    }

    public int remove(ListItemSale sale) {
        return db.deleteRows(TABLE_LIST_SALES, DB.KEY_ID + "=" + sale.getId());
    }

    public List<ListItemSale> getAll() {
        ArrayList<ListItemSale> result = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_LIST_SALES);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getListItemSale(cursor));
                while (cursor.moveToNext()) {
                    result.add(getListItemSale(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private ListItemSale getListItemSale(Cursor cursor) {
        return new ListItemSale(
                cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGS_IMAGE)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGS_PERIOD_START)),
                cursor.getString(cursor.getColumnIndex(KEY_CATEGS_PERIOD_END))
        );
    }

    public ListItemSale getWithId(long id) {
        ListItemSale result = null;
        Cursor cursor = db.getData(TABLE_LIST_SALES, DB.KEY_ID + "=" + id);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = getListItemSale(cursor);
            }
            cursor.close();
        }
        return result;
    }

    public int countWithImage(String image) {
        int result = 0;
        Cursor cursor = db.getData(TABLE_LIST_SALES, KEY_CATEGS_IMAGE + "='" + image + "'");
        if (cursor != null) {
            return cursor.getCount();
        }
        return result;
    }
}
