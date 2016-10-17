package com.PopCorp.Purchases.data.db.strategy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.dao.ListItemSaleDAO;
import com.PopCorp.Purchases.data.dao.ShoppingListDAO;
import com.PopCorp.Purchases.data.model.ShoppingList;

/**
 * Created by Apopsuenko on 10.10.2016.
 */

public class VersionSevenUpdateStrategy implements VersionUpdateStrategy {

    @Override
    public void update(Context context, SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + ListItemSaleDAO.TABLE_LIST_SALES + " ADD COLUMN " + ListItemSaleDAO.KEY_SALE_ID + " integer;");
        ShoppingList list = new ShoppingList(-1, "Список акций", 0, 0, "RUB");
        ShoppingListDAO dao = new ShoppingListDAO(db);
        dao.updateOrAddToDB(list);
    }
}
