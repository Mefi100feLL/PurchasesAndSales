package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDAO {

    public static final String TABLE_LISTS = "Lists";

    public static final String KEY_LISTS_NAME = "name";
    public static final String KEY_LISTS_DATELIST = "date";
    public static final String KEY_LISTS_ALARM = "datealarm";
    public static final String KEY_LISTS_CURRENCY = "currency";

    public static final String[] COLUMNS_LISTS = new String[]{
            KEY_LISTS_NAME,
            KEY_LISTS_DATELIST,
            KEY_LISTS_ALARM,
            KEY_LISTS_CURRENCY
    };

    public static final String CREATE_TABLE_LISTS = "CREATE TABLE IF NOT EXISTS " + TABLE_LISTS +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_LISTS_NAME + " text, " +
            KEY_LISTS_DATELIST + " text, " +
            KEY_LISTS_ALARM + " text, " +
            KEY_LISTS_CURRENCY + " text);";


    private DB db = DB.getInstance();
    private ListItemDAO listItemDAO = new ListItemDAO();

    public long updateOrAddToDB(ShoppingList list) {
        String[] values = new String[]{
                list.getName(),
                String.valueOf(list.getDateTime()),
                String.valueOf(list.getAlarm()),
                list.getCurrency()
        };
        int countUpdated = db.update(TABLE_LISTS, COLUMNS_LISTS, DB.KEY_ID + "=" + list.getId(), values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_LISTS, COLUMNS_LISTS, values);
        }
        return countUpdated;
    }

    public int remove(ShoppingList list) {
        return db.deleteRows(TABLE_LISTS, DB.KEY_ID + "=" + list.getId());
    }

    public List<ShoppingList> getAllLists() {
        ArrayList<ShoppingList> result = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_LISTS);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getShoppingList(cursor));
                while (cursor.moveToNext()) {
                    result.add(getShoppingList(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private ShoppingList getShoppingList(Cursor cursor) {
        ShoppingList result = new ShoppingList(
                cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_LISTS_NAME)),
                cursor.getLong(cursor.getColumnIndex(KEY_LISTS_DATELIST)),
                cursor.getLong(cursor.getColumnIndex(KEY_LISTS_ALARM)),
                cursor.getString(cursor.getColumnIndex(KEY_LISTS_CURRENCY))
        );
        result.setItems(listItemDAO.getAllItemsForList(result.getId()));
        return result;
    }

    public ShoppingList getListWithId(long listId) {
        ShoppingList result = null;
        Cursor cursor = db.getData(TABLE_LISTS, DB.KEY_ID + "=" + listId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = getShoppingList(cursor);
            }
            cursor.close();
        }
        return result;
    }
}
