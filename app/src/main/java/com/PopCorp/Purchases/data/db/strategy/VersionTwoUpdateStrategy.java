package com.PopCorp.Purchases.data.db.strategy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.db.DB;

public class VersionTwoUpdateStrategy implements VersionUpdateStrategy {
    @Override
    public void update(Context context, SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB.trans("All") + "( _id integer primary key autoincrement, name text, count integer, edizm text, coast integer, category text, shop text, comment text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB.trans("Lists") + "( _id integer primary key autoincrement, name text, date text, datealarm text);");
        db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN edizm text;");
        db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN category text;");
        db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN count integer;");
        db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN coast integer;");
    }
}
