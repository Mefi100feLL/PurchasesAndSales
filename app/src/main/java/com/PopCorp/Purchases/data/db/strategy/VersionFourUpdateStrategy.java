package com.PopCorp.Purchases.data.db.strategy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.dao.ListItemDAO;
import com.PopCorp.Purchases.data.dao.ProductDAO;
import com.PopCorp.Purchases.data.dao.RegionDAO;
import com.PopCorp.Purchases.data.dao.SaleDAO;
import com.PopCorp.Purchases.data.dao.ShopDAO;
import com.PopCorp.Purchases.data.dao.ShoppingListDAO;
import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.db.UpdaterDB;

public class VersionFourUpdateStrategy implements VersionUpdateStrategy {

    @Override
    public void update(Context context, SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DB.trans("All") + " RENAME TO " + ProductDAO.TABLE_ALL_ITEMS + ";");
        db.execSQL(ListItemCategoryDAO.CREATE_TABLE_CATEGS);
        UpdaterDB.addCategs(context, db);
        db.execSQL(SaleDAO.CREATE_TABLE_SALES);
        db.execSQL(RegionDAO.CREATE_TABLE_CITIES);
        db.execSQL(ShopDAO.CREATE_TABLE_SHOPES);
        moveItemsFromTablesToOneTableWithItems(db);
        updateTableLists(context, db);
    }

    private void updateTableLists(Context context, SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DB.trans("Lists") + " RENAME TO " + ShoppingListDAO.TABLE_LISTS + ";");
        db.execSQL("ALTER TABLE " + ShoppingListDAO.TABLE_LISTS + " ADD COLUMN " + ShoppingListDAO.KEY_LISTS_CURRENCY + " text;");
        ContentValues cv = new ContentValues();
        cv.put(ShoppingListDAO.KEY_LISTS_CURRENCY, context.getString(R.string.default_one_currency));
        db.update(ShoppingListDAO.TABLE_LISTS, cv, null, null);
    }

    private void moveItemsFromTablesToOneTableWithItems(SQLiteDatabase db) {
        db.execSQL(ListItemDAO.CREATE_TABLE_ITEMS);
        Cursor cursorLists = db.query(DB.trans("Lists"), null, null, null, null, null, null);
        if (cursorLists == null) {
            return;
        }
        if (cursorLists.moveToFirst()) {
            moveItemsToTable(db, DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))));
            removeTable(db, DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))));
            while (cursorLists.moveToNext()) {
                moveItemsToTable(db, DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))));
                removeTable(db, DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))));
            }
        }
        cursorLists.close();
    }

    private void removeTable(SQLiteDatabase db, String table) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }

    private void moveItemsToTable(SQLiteDatabase db, String oldTable) {
        Cursor cursorItems = db.query(oldTable, null, null, null, null, null, null);
        if (cursorItems == null) {
            return;
        }
        if (cursorItems.moveToFirst()) {
            putItemsInTableFromCursor(db, cursorItems, DB.untrans(oldTable));
            while (cursorItems.moveToNext()) {
                putItemsInTableFromCursor(db, cursorItems, DB.untrans(oldTable));
            }
        }
        cursorItems.close();
    }

    private void putItemsInTableFromCursor(SQLiteDatabase db, Cursor cursorItems, String datelist) {
        ContentValues cv = new ContentValues();
        for (int i = 0; i < cursorItems.getColumnCount(); i++) {
            if (cursorItems.getColumnName(i).equals("_id")) {
                continue;
            }
            cv.put(cursorItems.getColumnName(i), cursorItems.getString(i));
        }
        cv.put(ListItemDAO.KEY_ITEMS_DATELIST, datelist);
        db.insert(ListItemDAO.TABLE_ITEMS, null, cv);
    }
}
