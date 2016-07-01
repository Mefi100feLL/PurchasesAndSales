package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopDAO {

    public static final String TABLE_SHOPES = "Shopes";

    public static final String KEY_SHOP_CITY_ID = "id_city";
    public static final String KEY_SHOP_ID = "id";
    public static final String KEY_SHOP_NAME = "name";
    public static final String KEY_SHOP_IMAGE_URL = "image_url";
    public static final String KEY_SHOP_COUNT_SALES = "count_sales";
    public static final String KEY_SHOP_FAVORITE = "favorite";

    public static final String[] COLUMNS_SHOPES = new String[]{
            KEY_SHOP_CITY_ID,
            KEY_SHOP_ID,
            KEY_SHOP_NAME,
            KEY_SHOP_IMAGE_URL,
            KEY_SHOP_COUNT_SALES,
            KEY_SHOP_FAVORITE
    };

    public static final String[] COLUMNS_SHOPES_WITHOUT_FAVORITE = new String[]{
            KEY_SHOP_CITY_ID,
            KEY_SHOP_ID,
            KEY_SHOP_NAME,
            KEY_SHOP_IMAGE_URL,
            KEY_SHOP_COUNT_SALES
    };

    public static final String CREATE_TABLE_SHOPES = "CREATE TABLE IF NOT EXISTS " + TABLE_SHOPES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_SHOP_CITY_ID + " integer, " +
            KEY_SHOP_ID + " integer, " +
            KEY_SHOP_NAME + " text, " +
            KEY_SHOP_IMAGE_URL + " text, " +
            KEY_SHOP_COUNT_SALES + " text, " +
            KEY_SHOP_FAVORITE + " text);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    private String[] values(Shop shop){
        return new String[]{
                String.valueOf(shop.getRegionId()),
                String.valueOf(shop.getId()),
                shop.getName(),
                shop.getImageUrl(),
                String.valueOf(shop.getCountSales()),
                String.valueOf(shop.isFavorite())
        };
    }

    private String[] valuesWithoutFavorite(Shop shop){
        return new String[]{
                String.valueOf(shop.getRegionId()),
                String.valueOf(shop.getId()),
                shop.getName(),
                shop.getImageUrl(),
                String.valueOf(shop.getCountSales())
        };
    }

    private long addRec(Shop shop) {
        return db.addRec(TABLE_SHOPES, COLUMNS_SHOPES, values(shop));
    }

    public long updateOrAddToDB(Shop shop) {
        int countUpdated = db.update(TABLE_SHOPES, COLUMNS_SHOPES, KEY_SHOP_ID + "=" + shop.getId() + " AND " + KEY_SHOP_CITY_ID + "=" + shop.getRegionId(), values(shop));
        if (countUpdated == 0) {
            addRec(shop);
        }
        return countUpdated;
    }

    public long updateOrAddToDBWithoutFavorite(Shop shop) {
        int countUpdated = db.update(TABLE_SHOPES, COLUMNS_SHOPES_WITHOUT_FAVORITE, KEY_SHOP_ID + "=" + shop.getId() + " AND " + KEY_SHOP_CITY_ID + "=" + shop.getRegionId(), valuesWithoutFavorite(shop));
        if (countUpdated == 0) {
            return addRec(shop);
        }
        return countUpdated;
    }

    public int remove(Shop shop) {
        return db.deleteRows(TABLE_SHOPES, KEY_SHOP_ID + "=" + shop.getId() + " AND " + KEY_SHOP_CITY_ID + "=" + shop.getRegionId());
    }

    public List<Shop> getShopsForRegion(int regionId) {
        ArrayList<Shop> result = new ArrayList<>();
        Cursor cursor = db.getData(TABLE_SHOPES, COLUMNS_SHOPES, KEY_SHOP_CITY_ID + "=" + regionId);
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
        return Shop.create(
                cursor.getInt(cursor.getColumnIndex(KEY_SHOP_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_SHOP_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_SHOP_IMAGE_URL)),
                cursor.getInt(cursor.getColumnIndex(KEY_SHOP_CITY_ID)),
                Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_SHOP_FAVORITE))),
                cursor.getInt(cursor.getColumnIndex(KEY_SHOP_COUNT_SALES))
        );
    }

    public void addAllShops(List<Shop> categories) {
        db.beginTransaction();
        try {
            for (Shop shop : categories) {
                updateOrAddToDBWithoutFavorite(shop);
            }
            db.successFull();
        } finally {
            db.endTransaction();
        }
    }

    public Shop getShop(Sale sale){
        Shop result = null;
        Cursor cursor = db.getData(TABLE_SHOPES, COLUMNS_SHOPES, KEY_SHOP_CITY_ID + "=" + sale.getCityId() + " AND " + KEY_SHOP_ID + "=" + sale.getShopId());
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result =  getShop(cursor);
            }
            cursor.close();
        }
        return result;
    }
}
