package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.ListItem;

import java.util.ArrayList;
import java.util.List;

public class ListItemDAO {

    public static final String TABLE_ITEMS = "Items";

    public static final String KEY_ITEMS_DATELIST = "date";
    public static final String KEY_ITEMS_LIST_ID = "list_id";
    public static final String KEY_ITEMS_NAME = "name";
    public static final String KEY_ITEMS_COUNT = "count";
    public static final String KEY_ITEMS_EDIZM = "edizm";
    public static final String KEY_ITEMS_COAST = "coast";
    public static final String KEY_ITEMS_CATEGORY = "category";
    public static final String KEY_ITEMS_SHOP = "shop";
    public static final String KEY_ITEMS_COMMENT = "comment";
    public static final String KEY_ITEMS_BUYED = "buyed";
    public static final String KEY_ITEMS_IMPORTANT = "important";
    public static final String KEY_ITEMS_SALE_ID = "sale_id";

    public static final String[] COLUMNS_ITEMS = new String[]{
            KEY_ITEMS_LIST_ID,
            KEY_ITEMS_NAME,
            KEY_ITEMS_COUNT,
            KEY_ITEMS_EDIZM,
            KEY_ITEMS_COAST,
            KEY_ITEMS_CATEGORY,
            KEY_ITEMS_SHOP,
            KEY_ITEMS_COMMENT,
            KEY_ITEMS_BUYED,
            KEY_ITEMS_IMPORTANT,
            KEY_ITEMS_SALE_ID
    };

    public static final String CREATE_TABLE_ITEMS = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_ITEMS_LIST_ID + " long, " +
            KEY_ITEMS_NAME + " text, " +
            KEY_ITEMS_COUNT + " integer, " +
            KEY_ITEMS_EDIZM + " text, " +
            KEY_ITEMS_COAST + " integer, " +
            KEY_ITEMS_CATEGORY + " text, " +
            KEY_ITEMS_SHOP + " text, " +
            KEY_ITEMS_COMMENT + " text, " +
            KEY_ITEMS_BUYED + " boolean, " +
            KEY_ITEMS_IMPORTANT + " boolean, " +
            KEY_ITEMS_SALE_ID + " integer);";

    private DB db = DB.getInstance();
    private ListItemCategoryDAO categoryDAO = new ListItemCategoryDAO();
    private ListItemSaleDAO saleDAO = new ListItemSaleDAO();

    public long updateOrAddToDB(ListItem item) {
        String saleId = item.getSale() != null ? String.valueOf(item.getSale().getId()) : "0";
        String[] values = new String[]{
                String.valueOf(item.getListId()),
                item.getName(),
                String.valueOf(item.getCount()),
                item.getEdizm(),
                String.valueOf(item.getCoast()),
                String.valueOf(item.getCategory().getId()),
                item.getShop(),
                item.getComment(),
                String.valueOf(item.isBuyed()),
                String.valueOf(item.isImportant()),
                saleId
        };
        int countUpdated = db.update(TABLE_ITEMS, COLUMNS_ITEMS, DB.KEY_ID + "=" + item.getId(), values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_ITEMS, COLUMNS_ITEMS, values);
        }
        return countUpdated;
    }

    public int remove(ListItem category) {
        return db.deleteRows(TABLE_ITEMS, DB.KEY_ID + "=" + category.getId());
    }

    public List<ListItem> getAllItemsForList(long listId) {
        ArrayList<ListItem> result = new ArrayList<>();
        Cursor cursor = db.getData(TABLE_ITEMS, KEY_ITEMS_LIST_ID + "=" + listId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getListItem(cursor));
                while (cursor.moveToNext()) {
                    result.add(getListItem(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private ListItem getListItem(Cursor cursor) {
        return new ListItem(
                cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)),
                cursor.getLong(cursor.getColumnIndex(KEY_ITEMS_LIST_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_ITEMS_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_ITEMS_COUNT)),
                cursor.getString(cursor.getColumnIndex(KEY_ITEMS_EDIZM)),
                cursor.getString(cursor.getColumnIndex(KEY_ITEMS_COAST)),
                categoryDAO.getWithId(cursor.getLong(cursor.getColumnIndex(KEY_ITEMS_CATEGORY))),
                cursor.getString(cursor.getColumnIndex(KEY_ITEMS_SHOP)),
                cursor.getString(cursor.getColumnIndex(KEY_ITEMS_COMMENT)),
                Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_ITEMS_BUYED))),
                Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_ITEMS_IMPORTANT))),
                saleDAO.getWithId(cursor.getLong(cursor.getColumnIndex(KEY_ITEMS_SALE_ID)))
        );
    }

    public ListItem getWithId(long itemId) {
        ListItem result = null;
        Cursor cursor = db.getData(TABLE_ITEMS, DB.KEY_ID + "=" + itemId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = (getListItem(cursor));
            }
            cursor.close();
        }
        return result;
    }

    public boolean existsWithName(long listId, String name) {
        return db.getData(TABLE_ITEMS, KEY_ITEMS_LIST_ID + "=" + listId + " AND " + KEY_ITEMS_NAME + "='" + name + "'").moveToNext();
    }

    public void addAllItems(List<ListItem> items) {
        for (ListItem item : items){
            updateOrAddToDB(item);
        }
    }
}
