package com.PopCorp.Purchases.data.dao.skidkaonline;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.skidkaonline.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 06.07.2016.
 */
public class CityDAO {

    public static final String TABLE_CITIES = "skidkaonline_cities";

    public static final String KEY_CITY_ID = "id";
    public static final String KEY_CITY_NAME = "name";
    public static final String KEY_CITY_REGION = "name";
    public static final String KEY_CITY_URL = "name";

    public static final String[] COLUMNS_CITIES = new String[]{
            KEY_CITY_ID,
            KEY_CITY_NAME,
            KEY_CITY_REGION,
            KEY_CITY_URL
    };

    public static final String CREATE_TABLE_CITIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CITIES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_CITY_ID + " integer, " +
            KEY_CITY_NAME + " text, " +
            KEY_CITY_REGION + " text, " +
            KEY_CITY_URL + " text);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    public long updateOrAddToDB(City city) {
        String[] values = new String[]{
                String.valueOf(city.getId()),
                city.getName(),
                city.getRegion(),
                city.getUrl()
        };
        int countUpdated = db.update(TABLE_CITIES, COLUMNS_CITIES, KEY_CITY_ID + "=" + city.getId(), values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_CITIES, COLUMNS_CITIES, values);
        }
        return countUpdated;
    }

    public int remove(City city) {
        return db.deleteRows(TABLE_CITIES, KEY_CITY_ID + "=" + city.getId());
    }

    public List<City> getAllCities() {
        ArrayList<City> result = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_CITIES);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getCity(cursor));
                while (cursor.moveToNext()) {
                    result.add(getCity(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private City getCity(Cursor cursor) {
        return new City(
                cursor.getInt(cursor.getColumnIndex(KEY_CITY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_CITY_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_CITY_REGION)),
                cursor.getString(cursor.getColumnIndex(KEY_CITY_URL))
        );
    }

    public void addAllCities(List<City> cities) {
        db.beginTransaction();
        try {
            for (City region : cities) {
                updateOrAddToDB(region);
            }
            db.successFull();
        } finally {
            db.endTransaction();
        }
    }
}
