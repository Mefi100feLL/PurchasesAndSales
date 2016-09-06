package com.PopCorp.Purchases.data.db.strategy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.dao.ListItemDAO;
import com.PopCorp.Purchases.data.dao.ListItemSaleDAO;
import com.PopCorp.Purchases.data.dao.ProductDAO;
import com.PopCorp.Purchases.data.dao.SaleCommentDAO;
import com.PopCorp.Purchases.data.dao.SaleDAO;
import com.PopCorp.Purchases.data.dao.SameSaleDAO;
import com.PopCorp.Purchases.data.dao.ShoppingListDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.CityDAO;
import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.db.UpdaterDB;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.ShoppingList;

import java.util.ArrayList;

public class VersionSixUpdateStrategy implements VersionUpdateStrategy {

    @Override
    public void update(Context context, SQLiteDatabase db) {
        addListIdColumnToItems(db);
        UpdaterDB.addCategs(context, db);
        replaceCategoryNameToIdInItems(db);
        replaceCategoryNameToIdInProducts(db);
        db.execSQL("ALTER TABLE " + ListItemDAO.TABLE_ITEMS + " ADD COLUMN " + ListItemDAO.KEY_ITEMS_SALE_ID + " integer;");

        db.execSQL("DROP TABLE IF EXISTS " + SaleDAO.TABLE_SALES);


        db.execSQL(ListItemSaleDAO.CREATE_TABLE_SALES);
        db.execSQL(SaleDAO.CREATE_TABLE_SALES);
        db.execSQL(SameSaleDAO.CREATE_TABLE_SALES_SAMES);
        db.execSQL(SaleCommentDAO.CREATE_TABLE_SALES_COMMENTS);


        db.execSQL(CityDAO.CREATE_TABLE_CITIES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.CategoryDAO.CREATE_TABLE_CATEGORIES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.ShopDAO.CREATE_TABLE_SHOPES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.SaleDAO.CREATE_TABLE_SALES);
        db.execSQL(com.PopCorp.Purchases.data.dao.skidkaonline.SaleCommentDAO.CREATE_TABLE_SALES_COMMENTS);
    }

    private void replaceCategoryNameToIdInProducts(SQLiteDatabase db) {
        ArrayList<ListItemCategory> categories = getCategories(db);
        Cursor cursor = db.query(ProductDAO.TABLE_ALL_ITEMS, null, null, null, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                findAndReplaceNameToIdInProducts(db, categories, cursor);
                while (cursor.moveToNext()){
                    findAndReplaceNameToIdInProducts(db, categories, cursor);
                }
            }
            cursor.close();
        }
    }

    private void findAndReplaceNameToIdInProducts(SQLiteDatabase db, ArrayList<ListItemCategory> categories, Cursor cursor) {
        String category = cursor.getString(cursor.getColumnIndex(ProductDAO.KEY_ALL_ITEMS_CATEGORY));
        for (ListItemCategory listItemCategory : categories){
            if ((category == null || category.isEmpty() || category.equals("Нет категории"))){
                if (listItemCategory.getName().equals("Другое")) {
                    putListItemCategoryIdInProducts(db, cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)), listItemCategory.getId());
                    return;
                }
            } else {
                if (category.equals(listItemCategory.getName())){
                    putListItemCategoryIdInProducts(db, cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)), listItemCategory.getId());
                    return;
                }
            }
        }
    }

    private void putListItemCategoryIdInProducts(SQLiteDatabase db, long itemId, long listItemCategoryId) {
        ContentValues cv = new ContentValues();
        cv.put(ProductDAO.KEY_ALL_ITEMS_CATEGORY, listItemCategoryId);
        db.update(ProductDAO.TABLE_ALL_ITEMS, cv, DB.KEY_ID + "=" + itemId, null);
    }


    private void replaceCategoryNameToIdInItems(SQLiteDatabase db) {
        ArrayList<ListItemCategory> categories = getCategories(db);
        Cursor cursor = db.query(ListItemDAO.TABLE_ITEMS, null, null, null, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                findAndReplaceNameToId(db, categories, cursor);
                while (cursor.moveToNext()){
                    findAndReplaceNameToId(db, categories, cursor);
                }
            }
            cursor.close();
        }
    }

    private void findAndReplaceNameToId(SQLiteDatabase db, ArrayList<ListItemCategory> categories, Cursor cursor) {
        String category = cursor.getString(cursor.getColumnIndex(ListItemDAO.KEY_ITEMS_CATEGORY));
        for (ListItemCategory listItemCategory : categories){
            if ((category == null || category.isEmpty() || category.equals("Нет категории"))){
                if (listItemCategory.getName().equals("Другое")) {
                    putListItemCategoryId(db, cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)), listItemCategory.getId());
                    return;
                }
            } else {
                if (category.equals(listItemCategory.getName())){
                    putListItemCategoryId(db, cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)), listItemCategory.getId());
                    return;
                }
            }
        }
    }

    private void putListItemCategoryId(SQLiteDatabase db, long itemId, long listItemCategoryId) {
        ContentValues cv = new ContentValues();
        cv.put(ListItemDAO.KEY_ITEMS_CATEGORY, listItemCategoryId);
        db.update(ListItemDAO.TABLE_ITEMS, cv, DB.KEY_ID + "=" + itemId, null);
    }

    private ArrayList<ListItemCategory> getCategories(SQLiteDatabase db) {
        ArrayList<ListItemCategory> result = new ArrayList<>();
        Cursor cursor = db.query(ListItemCategoryDAO.TABLE_CATEGORIES, null, null, null, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                result.add(getListItemCategory(cursor));
                while (cursor.moveToNext()){
                    result.add(getListItemCategory(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private ListItemCategory getListItemCategory(Cursor cursor) {
        return new ListItemCategory(
                cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(ListItemCategoryDAO.KEY_CATEGS_NAME)),
                cursor.getInt(cursor.getColumnIndex(ListItemCategoryDAO.KEY_CATEGS_COLOR))
        );
    }

    private void addListIdColumnToItems(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + ListItemDAO.TABLE_ITEMS + " ADD COLUMN " + ListItemDAO.KEY_ITEMS_LIST_ID + " integer;");
        Cursor cursor = db.query(ShoppingListDAO.TABLE_LISTS, null, null, null, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                updateItemsForList(db, cursor);
                while (cursor.moveToNext()){
                    updateItemsForList(db, cursor);
                }
            }
            cursor.close();
        }
    }

    private void updateItemsForList(SQLiteDatabase db, Cursor cursor) {
        ShoppingList list = getShoppingList(cursor);
        ContentValues cv = new ContentValues();
        cv.put(ListItemDAO.KEY_ITEMS_LIST_ID, list.getId());
        db.update(ListItemDAO.TABLE_ITEMS, cv, ListItemDAO.KEY_ITEMS_DATELIST + "='" + list.getDateTime() + "'", null);
    }

    private ShoppingList getShoppingList(Cursor cursor) {
        return new ShoppingList(
                cursor.getLong(cursor.getColumnIndex(DB.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(ShoppingListDAO.KEY_LISTS_NAME)),
                cursor.getLong(cursor.getColumnIndex(ShoppingListDAO.KEY_LISTS_DATELIST)),
                cursor.getLong(cursor.getColumnIndex(ShoppingListDAO.KEY_LISTS_ALARM)),
                cursor.getString(cursor.getColumnIndex(ShoppingListDAO.KEY_LISTS_CURRENCY))
        );
    }
}
