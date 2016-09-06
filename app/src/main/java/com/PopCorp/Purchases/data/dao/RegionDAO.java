package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.Region;

import java.util.ArrayList;
import java.util.List;

public class RegionDAO {

    public static final String TABLE_CITIES = "Cities";

    public static final String KEY_CITY_ID = "id";
    public static final String KEY_CITY_NAME = "name";

    public static final String[] COLUMNS_CITIES = new String[]{
            KEY_CITY_ID,
            KEY_CITY_NAME
    };

    public static final String CREATE_TABLE_CITIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CITIES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_CITY_NAME + " text, " +
            KEY_CITY_ID + " integer);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    public long updateOrAddToDB(Region region) {
        String[] values = new String[]{
                String.valueOf(region.getId()),
                region.getName()
        };
        int countUpdated = db.update(TABLE_CITIES, COLUMNS_CITIES, KEY_CITY_ID + "=" + region.getId(), values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_CITIES, COLUMNS_CITIES, values);
        }
        return countUpdated;
    }

    public int remove(Region region) {
        return db.deleteRows(TABLE_CITIES, KEY_CITY_ID + "=" + region.getId());
    }

    public List<Region> getAllRegions() {
        ArrayList<Region> result = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_CITIES);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getRegion(cursor));
                while (cursor.moveToNext()) {
                    result.add(getRegion(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private Region getRegion(Cursor cursor) {
        return Region.create(
                cursor.getInt(cursor.getColumnIndex(KEY_CITY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_CITY_NAME))
        );
    }

    public void addAllRegions(List<Region> regions) {
        db.beginTransaction();
        try {
            for (Region region : regions) {
                updateOrAddToDB(region);
            }
            db.successFull();
        } finally {
            db.endTransaction();
        }
    }

    public Region getWithId(String id) {
        Region result = null;
        Cursor cursor = db.getData(TABLE_CITIES, KEY_CITY_ID + "=" + id);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = getRegion(cursor);
            }
            cursor.close();
        }
        return result;
    }
}
