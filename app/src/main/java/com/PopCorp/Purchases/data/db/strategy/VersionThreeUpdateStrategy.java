package com.PopCorp.Purchases.data.db.strategy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.db.UpdaterDB;

public class VersionThreeUpdateStrategy implements VersionUpdateStrategy {

    @Override
    public void update(Context context, SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN shop text;");
        db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN comment text;");
        UpdaterDB.addAllProducts(context, db);
    }
}
