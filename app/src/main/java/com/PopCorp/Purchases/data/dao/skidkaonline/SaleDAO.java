package com.PopCorp.Purchases.data.dao.skidkaonline;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 06.07.2016.
 */
public class SaleDAO {


    public static final String TABLE_SALES = "skidkaonline_sales";

    public static final String KEY_SALE_ID = "id";
    public static final String KEY_SALE_SHOP_URL = "shop_url";
    public static final String KEY_SALE_IMAGE_SMALL = "image_small";
    public static final String KEY_SALE_IMAGE_BIG = "image_big";
    public static final String KEY_SALE_PERIOD_START = "period_start";
    public static final String KEY_SALE_PERIOD_END = "period_end";
    public static final String KEY_SALE_CATALOG = "catalog";
    public static final String KEY_SALE_CITY_URL = "city_url";
    public static final String KEY_SALE_CITY_ID = "city_id";
    public static final String KEY_SALE_IMAGE_WIDTH = "image_width";
    public static final String KEY_SALE_IMAGE_HEIGHT = "image_height";

    public static final String[] COLUMNS_SALES = new String[]{
            KEY_SALE_ID,
            KEY_SALE_SHOP_URL,
            KEY_SALE_IMAGE_SMALL,
            KEY_SALE_IMAGE_BIG,
            KEY_SALE_PERIOD_START,
            KEY_SALE_PERIOD_END,
            KEY_SALE_CATALOG,
            KEY_SALE_CITY_URL,
            KEY_SALE_CITY_ID,
            KEY_SALE_IMAGE_WIDTH,
            KEY_SALE_IMAGE_HEIGHT
    };

    public static final String CREATE_TABLE_SALES = "CREATE TABLE IF NOT EXISTS " + TABLE_SALES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_SALE_ID + " integer, " +
            KEY_SALE_SHOP_URL + " text, " +
            KEY_SALE_IMAGE_SMALL + " text, " +
            KEY_SALE_IMAGE_BIG + " text, " +
            KEY_SALE_PERIOD_START + " integer, " +
            KEY_SALE_PERIOD_END + " integer, " +
            KEY_SALE_CATALOG + " text, " +
            KEY_SALE_CITY_URL + " text, " +
            KEY_SALE_CITY_ID + " integer, " +
            KEY_SALE_IMAGE_WIDTH + " integer, " +
            KEY_SALE_IMAGE_HEIGHT + " integer);";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    private String[] values(Sale sale) {
        return new String[]{
                String.valueOf(sale.getId()),
                sale.getShopUrl(),
                sale.getImageSmall(),
                sale.getImageBig(),
                String.valueOf(sale.getPeriodStart()),
                String.valueOf(sale.getPeriodEnd()),
                sale.getCatalog(),
                sale.getCityUrl(),
                String.valueOf(sale.getCityId()),
                String.valueOf(sale.getImageWidth()),
                String.valueOf(sale.getImageHeight())
        };
    }

    public long updateOrAddToDB(Sale sale) {
        int countUpdated = db.update(TABLE_SALES, COLUMNS_SALES, KEY_SALE_CITY_ID + "=" + sale.getCityId() + " AND " + KEY_SALE_ID + "=" + sale.getId(), values(sale));
        if (countUpdated == 0) {
            return db.addRec(TABLE_SALES, COLUMNS_SALES, values(sale));
        }
        return countUpdated;
    }

    public int remove(Sale sale) {
        return db.deleteRows(TABLE_SALES, KEY_SALE_CITY_ID + "=" + sale.getCityId() + " AND " + KEY_SALE_ID + "=" + sale.getId());
    }

    public List<Sale> getSales(int cityId, String shopUrl) {
        ArrayList<Sale> result = new ArrayList<>();
        Cursor cursor = db.getData(TABLE_SALES, COLUMNS_SALES, KEY_SALE_CITY_ID + "=" + cityId + " AND " + KEY_SALE_SHOP_URL + "='" + shopUrl + "'");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getSale(cursor));
                while (cursor.moveToNext()) {
                    result.add(getSale(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private Sale getSale(Cursor cursor) {
        Sale result = new Sale(
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_SHOP_URL)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_IMAGE_SMALL)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_IMAGE_BIG)),
                cursor.getLong(cursor.getColumnIndex(KEY_SALE_PERIOD_START)),
                cursor.getLong(cursor.getColumnIndex(KEY_SALE_PERIOD_END)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_CATALOG)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_CITY_URL)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_CITY_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_IMAGE_WIDTH)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_IMAGE_HEIGHT))
        );
        return result;
    }

    public Sale getSale(int cityId, int saleId) {
        Cursor cursor = db.getData(TABLE_SALES, COLUMNS_SALES, KEY_SALE_CITY_ID + "=" + cityId + " AND " + KEY_SALE_ID + "=" + saleId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return getSale(cursor);
            }
        }
        return null;
    }

    public void addAllSales(List<Sale> sales) {
        db.beginTransaction();
        try {
            for (Sale sale : sales) {
                updateOrAddToDB(sale);
            }
            db.successFull();
        } finally {
            db.endTransaction();
        }
    }
}
