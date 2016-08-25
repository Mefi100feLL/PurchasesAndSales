package com.PopCorp.Purchases.data.dao.skidkaonline;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;

import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 06.07.2016.
 */
public class ShopDAO {


    public static final String TABLE_SHOPES = "skidkaonline_shops";

    public static final String KEY_SHOP_NAME = "name";
    public static final String KEY_SHOP_URL = "url";
    public static final String KEY_SHOP_IMAGE = "image";
    public static final String KEY_SHOP_CATEGORY_URL = "category_url";
    public static final String KEY_SHOP_CITY_URL = "city_url";
    public static final String KEY_SHOP_CITY_ID = "city_id";
    public static final String KEY_SHOP_FAVORITE = "favorite";

    public static final String[] COLUMNS_SHOPES = new String[]{
            KEY_SHOP_NAME,
            KEY_SHOP_URL,
            KEY_SHOP_IMAGE,
            KEY_SHOP_CATEGORY_URL,
            KEY_SHOP_CITY_URL,
            KEY_SHOP_CITY_ID,
            KEY_SHOP_FAVORITE
    };

    public static final String[] COLUMNS_SHOPES_WITHOUT_FAVORITE = new String[]{
            KEY_SHOP_NAME,
            KEY_SHOP_URL,
            KEY_SHOP_IMAGE,
            KEY_SHOP_CATEGORY_URL,
            KEY_SHOP_CITY_URL,
            KEY_SHOP_CITY_ID
    };

    public static final String CREATE_TABLE_SHOPES = "CREATE TABLE IF NOT EXISTS " + TABLE_SHOPES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_SHOP_NAME + " text, " +
            KEY_SHOP_URL + " text, " +
            KEY_SHOP_IMAGE + " text, " +
            KEY_SHOP_CATEGORY_URL + " text, " +
            KEY_SHOP_CITY_URL + " text, " +
            KEY_SHOP_CITY_ID + " integer, " +
            KEY_SHOP_FAVORITE + " text);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();
    private CategoryDAO categoryDAO = new CategoryDAO();

    private String[] values(Shop shop){
        return new String[]{
                shop.getName(),
                shop.getUrl(),
                shop.getImage(),
                shop.getCategory().getUrl(),
                shop.getCityUrl(),
                String.valueOf(shop.getCityId()),
                String.valueOf(shop.isFavorite())
        };
    }

    private String[] valuesWithoutFavorite(Shop shop){
        return new String[]{
                shop.getName(),
                shop.getUrl(),
                shop.getImage(),
                shop.getCategory().getUrl(),
                shop.getCityUrl(),
                String.valueOf(shop.getCityId())
        };
    }

    private long addRec(Shop shop) {
        return db.addRec(TABLE_SHOPES, COLUMNS_SHOPES, values(shop));
    }

    public long updateOrAddToDB(Shop shop) {
        int countUpdated = db.update(TABLE_SHOPES, COLUMNS_SHOPES, KEY_SHOP_URL + "='" + shop.getUrl() + "' AND " + KEY_SHOP_CITY_ID + "=" + shop.getCityId(), values(shop));
        if (countUpdated == 0) {
            categoryDAO.updateOrAddToDB(shop.getCategory());
            addRec(shop);
        }
        return countUpdated;
    }

    public long updateOrAddToDBWithoutFavorite(Shop shop) {
        int countUpdated = db.update(TABLE_SHOPES, COLUMNS_SHOPES_WITHOUT_FAVORITE, KEY_SHOP_URL + "='" + shop.getUrl() + "' AND " + KEY_SHOP_CITY_ID + "=" + shop.getCityId(), valuesWithoutFavorite(shop));
        if (countUpdated == 0) {
            categoryDAO.updateOrAddToDB(shop.getCategory());
            return addRec(shop);
        }
        return countUpdated;
    }

    public int remove(Shop shop) {
        return db.deleteRows(TABLE_SHOPES, KEY_SHOP_URL + "='" + shop.getUrl() + "' AND " + KEY_SHOP_CITY_ID + "=" + shop.getCityId());
    }

    public List<Shop> getShopsForCity(int cityId) {
        ArrayList<Shop> result = new ArrayList<>();
        Cursor cursor = db.getData(TABLE_SHOPES, COLUMNS_SHOPES, KEY_SHOP_CITY_ID + "=" + cityId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getShop(cursor));
                while (cursor.moveToNext()) {
                    result.add(getShop(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private Shop getShop(Cursor cursor) {
        return new Shop(
                cursor.getString(cursor.getColumnIndex(KEY_SHOP_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_SHOP_URL)),
                cursor.getString(cursor.getColumnIndex(KEY_SHOP_IMAGE)),
                categoryDAO.getCategory(cursor.getString(cursor.getColumnIndex(KEY_SHOP_CATEGORY_URL)), cursor.getInt(cursor.getColumnIndex(KEY_SHOP_CITY_ID))),
                cursor.getString(cursor.getColumnIndex(KEY_SHOP_CITY_URL)),
                cursor.getInt(cursor.getColumnIndex(KEY_SHOP_CITY_ID)),
                Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_SHOP_FAVORITE)))
        );
    }

    public void addAllShops(List<Shop> shops) {
        db.beginTransaction();
        try {
            for (Shop shop : shops) {
                updateOrAddToDBWithoutFavorite(shop);
            }
            db.successFull();
        } finally {
            db.endTransaction();
        }
    }

    public Shop getWithUrl(String shopUrl, int cityId) {
        Shop result = null;
        Cursor cursor = db.getData(TABLE_SHOPES, COLUMNS_SHOPES, KEY_SHOP_CITY_ID + "=" + cityId + " AND " + KEY_SHOP_URL + "='" + shopUrl + "'");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = getShop(cursor);
            }
            cursor.close();
        }
        return result;
    }
}
