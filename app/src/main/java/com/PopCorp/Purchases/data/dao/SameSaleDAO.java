package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.SameSale;

import java.util.ArrayList;
import java.util.List;

public class SameSaleDAO {

    public static final String TABLE_SALES_SAMES = "sales_sames";

    public static final String KEY_SAME_PARENT_ID = "parent_id";
    public static final String KEY_SAME_CITY_ID = "city_id";
    public static final String KEY_SAME_ID = "id";
    public static final String KEY_SAME_TEXT = "text";
    public static final String KEY_SAME_COAST = "coast";
    public static final String KEY_SAME_SHOP_NAME = "shop_name";
    public static final String KEY_SAME_PERIOD_START = "period_start";
    public static final String KEY_SAME_PERIOD_END = "period_end";

    public static final String[] COLUMNS_SAMES = new String[]{
            KEY_SAME_PARENT_ID,
            KEY_SAME_CITY_ID,
            KEY_SAME_ID,
            KEY_SAME_TEXT,
            KEY_SAME_COAST,
            KEY_SAME_SHOP_NAME,
            KEY_SAME_PERIOD_START,
            KEY_SAME_PERIOD_END
    };

    public static final String CREATE_TABLE_SALES_SAMES = "CREATE TABLE IF NOT EXISTS " + TABLE_SALES_SAMES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_SAME_PARENT_ID + " integer, " +
            KEY_SAME_CITY_ID + " integer, " +
            KEY_SAME_ID + " integer, " +
            KEY_SAME_TEXT + " text, " +
            KEY_SAME_COAST + " text, " +
            KEY_SAME_SHOP_NAME + " text, " +
            KEY_SAME_PERIOD_START + " text, " +
            KEY_SAME_PERIOD_END + " text);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    public long updateOrAddToDB(SameSale sameSale) {
        String[] values = new String[]{
                String.valueOf(sameSale.getParentSaleId()),
                String.valueOf(sameSale.getCityId()),
                String.valueOf(sameSale.getSaleId()),
                sameSale.getText(),
                sameSale.getCoast(),
                sameSale.getShopName(),
                sameSale.getPeriodStart(),
                sameSale.getPeriodEnd()
        };
        int countUpdated = db.update(TABLE_SALES_SAMES, COLUMNS_SAMES, selection(sameSale), values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_SALES_SAMES, COLUMNS_SAMES, values);
        }
        return countUpdated;
    }

    private String selection(SameSale sameSale) {
        return KEY_SAME_PARENT_ID + "=" + sameSale.getParentSaleId() + " AND " +
                KEY_SAME_CITY_ID + "=" + sameSale.getCityId() + " AND " +
                KEY_SAME_COAST + "=" + sameSale.getSaleId();
    }

    public int remove(SameSale sameSale) {
        return db.deleteRows(TABLE_SALES_SAMES, selection(sameSale));
    }

    public List<SameSale> getForSale(int cityId, int saleId) {
        ArrayList<SameSale> result = new ArrayList<>();
        Cursor cursor = db.getData(TABLE_SALES_SAMES, COLUMNS_SAMES, KEY_SAME_PARENT_ID + "=" + saleId + " AND " + KEY_SAME_CITY_ID + "=" + cityId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getSame(cursor));
                while (cursor.moveToNext()) {
                    result.add(getSame(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private SameSale getSame(Cursor cursor) {
        return new SameSale(
                cursor.getInt(cursor.getColumnIndex(KEY_SAME_PARENT_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_SAME_CITY_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_SAME_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_SAME_TEXT)),
                cursor.getString(cursor.getColumnIndex(KEY_SAME_COAST)),
                cursor.getString(cursor.getColumnIndex(KEY_SAME_SHOP_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_SAME_PERIOD_START)),
                cursor.getString(cursor.getColumnIndex(KEY_SAME_PERIOD_END))
        );
    }

    public void addAllSaleSames(List<SameSale> sameSales) {
        for (SameSale sameSale : sameSales) {
            updateOrAddToDB(sameSale);
        }
    }
}
