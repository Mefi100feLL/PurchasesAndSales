package com.PopCorp.Purchases.data.db.strategy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface VersionUpdateStrategy {

    void update(Context context, SQLiteDatabase db);
}
