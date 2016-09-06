package com.PopCorp.Purchases.data.db.strategy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.dao.CategoryDAO;
import com.PopCorp.Purchases.data.dao.SaleDAO;

public class VersionFiveUpdateStrategy implements VersionUpdateStrategy {

    @Override
    public void update(Context context, SQLiteDatabase db) {
        db.execSQL(CategoryDAO.CREATE_TABLE_SALE_CATEGS);
        db.execSQL("ALTER TABLE " + SaleDAO.TABLE_SALES + " ADD COLUMN " + SaleDAO.KEY_SALE_CATEGORY + " text;");
    }
}
