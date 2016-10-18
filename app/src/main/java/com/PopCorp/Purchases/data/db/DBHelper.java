package com.PopCorp.Purchases.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.PopCorp.Purchases.data.dao.CategoryDAO;
import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.dao.ListItemDAO;
import com.PopCorp.Purchases.data.dao.ListItemSaleDAO;
import com.PopCorp.Purchases.data.dao.ProductDAO;
import com.PopCorp.Purchases.data.dao.RegionDAO;
import com.PopCorp.Purchases.data.dao.SaleCommentDAO;
import com.PopCorp.Purchases.data.dao.SaleDAO;
import com.PopCorp.Purchases.data.dao.SameSaleDAO;
import com.PopCorp.Purchases.data.dao.ShopDAO;
import com.PopCorp.Purchases.data.dao.ShoppingListDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.CityDAO;
import com.PopCorp.Purchases.data.model.ShoppingList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PopCorp.Purchases.DB";
    private static final int DB_VERSION = 7;

    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ShoppingListDAO.CREATE_TABLE_LISTS);
        db.execSQL(ListItemDAO.CREATE_TABLE_ITEMS);
        db.execSQL(ProductDAO.CREATE_TABLE_ALL_ITEMS);
        db.execSQL(SaleDAO.CREATE_TABLE_SALES);
        db.execSQL(RegionDAO.CREATE_TABLE_CITIES);
        db.execSQL(ShopDAO.CREATE_TABLE_SHOPES);
        db.execSQL(CategoryDAO.CREATE_TABLE_SALE_CATEGS);
        db.execSQL(ListItemCategoryDAO.CREATE_TABLE_CATEGS);

        //version 6
        db.execSQL(ListItemSaleDAO.CREATE_TABLE_SALES);
        db.execSQL(SameSaleDAO.CREATE_TABLE_SALES_SAMES);
        db.execSQL(SaleCommentDAO.CREATE_TABLE_SALES_COMMENTS);

        //skidkaonline
        db.execSQL(CityDAO.CREATE_TABLE_CITIES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.CategoryDAO.CREATE_TABLE_CATEGORIES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.ShopDAO.CREATE_TABLE_SHOPES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.SaleDAO.CREATE_TABLE_SALES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.SaleCommentDAO.CREATE_TABLE_SALES_COMMENTS);

        UpdaterDB.addCategs(context, db);
        UpdaterDB.addAllProducts(context, db);
        ShoppingList list = new ShoppingList(-1, "Список акций", 0, 0, "RUB");
        ShoppingListDAO dao = new ShoppingListDAO(db);
        dao.updateOrAddToDB(list);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            new UpdaterDB().update(context, db, oldVersion, newVersion);
        } catch (Exception e) {
            throw new RuntimeException("Error in updating DB");
        }
    }

    /*private void changeFavorites(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DB.TABLE_ALL_ITEMS + " ADD COLUMN " + DB.KEY_ALL_ITEMS_FAVORITE + " boolean;");
        Cursor cursor = db.query(DB.TABLE_FAVORITE_ITEMS, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                db.execSQL("UPDATE " + DB.TABLE_ALL_ITEMS + " SET " + DB.KEY_ALL_ITEMS_FAVORITE + "='" + "true" + "' WHERE " + DB.KEY_ALL_ITEMS_NAME + "='" + cursor.getString(cursor.getColumnIndex(DB.KEY_FAVORITE_ITEMS_NAME)) + "'" + ";");
                while (cursor.moveToNext()) {
                    db.execSQL("UPDATE " + DB.TABLE_ALL_ITEMS + " SET " + DB.KEY_ALL_ITEMS_FAVORITE + "='" + "true" + "' WHERE " + DB.KEY_ALL_ITEMS_NAME + "='" + cursor.getString(cursor.getColumnIndex(DB.KEY_FAVORITE_ITEMS_NAME)) + "'" + ";");
                }
            }
            cursor.close();
        }
        removeTable(db, DB.TABLE_FAVORITE_ITEMS);
    }*/
}