package com.PopCorp.Purchases.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.PopCorp.Purchases.R;
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
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Product;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

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
        db.execSQL(ListItemCategoryDAO.CREATE_TABLE_CATEGS);
        db.execSQL(ListItemSaleDAO.CREATE_TABLE_SALES);
        db.execSQL(ProductDAO.CREATE_TABLE_ALL_ITEMS);
        db.execSQL(SaleDAO.CREATE_TABLE_SALES);
        db.execSQL(RegionDAO.CREATE_TABLE_CITIES);
        db.execSQL(ShopDAO.CREATE_TABLE_SHOPES);
        db.execSQL(CategoryDAO.CREATE_TABLE_SALE_CATEGS);
        db.execSQL(SameSaleDAO.CREATE_TABLE_SALES_SAMES);
        db.execSQL(SaleCommentDAO.CREATE_TABLE_SALES_COMMENTS);

        //db.execSQL(DB.CREATE_TABLE_SHOP_SALES);

        addCategs(db);
        addAllProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            updateTableAllItems(db, oldVersion);
            updateTableWithItems(db, oldVersion);
            updateTableLists(db, oldVersion);
            if (oldVersion < 3) {
                addAllProducts(db);
            }
            if (oldVersion < 4) {
                addCategs(db);
                db.execSQL(SaleDAO.CREATE_TABLE_SALES);
                db.execSQL(RegionDAO.CREATE_TABLE_CITIES);
                db.execSQL(ShopDAO.CREATE_TABLE_SHOPES);
            }

            //in market
            if (oldVersion < 5) {
                db.execSQL(CategoryDAO.CREATE_TABLE_SALE_CATEGS);
                db.execSQL("ALTER TABLE " + SaleDAO.TABLE_SALES + " ADD COLUMN " + SaleDAO.KEY_SALE_CATEGORY + " text;");
            }
            if (oldVersion < 6){
                db.execSQL("ALTER TABLE " + ListItemDAO.TABLE_ITEMS + " ADD COLUMN " + ListItemDAO.KEY_ITEMS_SALE_ID + " integer;");

                //db.execSQL(DB.CREATE_TABLE_SHOP_SALES);

                db.execSQL("ALTER TABLE " + SaleDAO.TABLE_SALES + " ADD COLUMN " + SaleDAO.KEY_SALE_CATEGORY_TYPE + " text;");
            }
            if (oldVersion < 7){
                removeTable(db, SaleDAO.TABLE_SALES);

                db.execSQL(SaleDAO.CREATE_TABLE_SALES);
                db.execSQL(SameSaleDAO.CREATE_TABLE_SALES_SAMES);
                db.execSQL(SaleCommentDAO.CREATE_TABLE_SALES_COMMENTS);
            }
            if (oldVersion < 8){
                addCategs(db);
            }
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

    private void addCategs(SQLiteDatabase db) {
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(context.getResources().getColor(R.color.md_brown_500));
        colors.add(context.getResources().getColor(R.color.md_orange_500));
        colors.add(context.getResources().getColor(R.color.md_blue_500));
        colors.add(context.getResources().getColor(R.color.md_teal_500));
        colors.add(context.getResources().getColor(R.color.md_deep_purple_500));
        colors.add(context.getResources().getColor(R.color.md_red_500));
        colors.add(context.getResources().getColor(R.color.md_green_500));

        categories.add(context.getResources().getString(R.string.default_categ_one));
        categories.add(context.getResources().getString(R.string.default_categ_two));
        categories.add(context.getResources().getString(R.string.default_categ_three));
        categories.add(context.getResources().getString(R.string.default_categ_four));
        categories.add(context.getResources().getString(R.string.default_categ_five));
        categories.add(context.getResources().getString(R.string.default_categ_six));
        categories.add(context.getResources().getString(R.string.default_categ_seven));

        ListItemCategoryDAO categoryDAO = new ListItemCategoryDAO(db);
        for (int i=0; i<categories.size(); i++){
            ListItemCategory category = new ListItemCategory(-1, categories.get(i), colors.get(i));
            categoryDAO.updateOrAddToDB(category);
        }
    }

    private void updateTableAllItems(SQLiteDatabase db, int oldVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB.trans("All") + "( _id integer primary key autoincrement, name text, count integer, edizm text, coast integer, category text, shop text, comment text);");
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN edizm text;");
            db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN category text;");
            db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN count integer;");
            db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN coast integer;");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN shop text;");
            db.execSQL("ALTER TABLE " + DB.trans("All") + " ADD COLUMN comment text;");
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + DB.trans("All") + " RENAME TO " + ProductDAO.TABLE_ALL_ITEMS + ";");
        }
    }

    private void updateTableWithItems(SQLiteDatabase db, int oldVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB.trans("Lists") + "( _id integer primary key autoincrement, name text, date text, datealarm text);");
        if (oldVersion < 3) {
            updateTableItemsForOldDataBases(db, oldVersion);
        }
        if (oldVersion < 4) {
            moveItemsFromTablesToOneTableWithItems(db);
        }
    }

    private void updateTableItemsForOldDataBases(SQLiteDatabase db, int oldVersion) {
        Cursor cursorLists = db.query(DB.trans("Lists"), null, null, null, null, null, null);
        if (cursorLists == null) {
            return;
        }
        if (cursorLists.moveToFirst()) {
            changeTablesWithItemsForOldDataBases(db, cursorLists, oldVersion);
            while (cursorLists.moveToNext()) {
                changeTablesWithItemsForOldDataBases(db, cursorLists, oldVersion);
            }
        }
        cursorLists.close();
    }

    private void changeTablesWithItemsForOldDataBases(SQLiteDatabase db, Cursor cursorLists, int oldVersion) {
        db.execSQL("ALTER TABLE " + DB.trans(cursorLists.getString(cursorLists.getColumnIndex("name")) + cursorLists.getString(cursorLists.getColumnIndex("date"))) + " ADD COLUMN category text;");
        db.execSQL("ALTER TABLE " + DB.trans(cursorLists.getString(cursorLists.getColumnIndex("name")) + cursorLists.getString(cursorLists.getColumnIndex("date"))) + " RENAME TO " + DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))) + ";");

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))) + " ADD COLUMN shop text;");
            db.execSQL("ALTER TABLE " + DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))) + " ADD COLUMN comment text;");
            db.execSQL("ALTER TABLE " + DB.trans(cursorLists.getString(cursorLists.getColumnIndex("date"))) + " ADD COLUMN important text;");
        }
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

    private void updateTableLists(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + DB.trans("Lists") + " RENAME TO " + ShoppingListDAO.TABLE_LISTS + ";");
            db.execSQL("ALTER TABLE " + ShoppingListDAO.TABLE_LISTS + " ADD COLUMN " + ShoppingListDAO.KEY_LISTS_CURRENCY + " text;");
            ContentValues cv = new ContentValues();
            cv.put(ShoppingListDAO.KEY_LISTS_CURRENCY, context.getString(R.string.default_one_currency));
            db.update(ShoppingListDAO.TABLE_LISTS, cv, null, null);
        }
    }

    private void addAllProducts(SQLiteDatabase db) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> counts = new ArrayList<>();
        ArrayList<String> coasts = new ArrayList<>();
        ArrayList<String> edizms = new ArrayList<>();
        try {
            XmlPullParser xpp = context.getResources().getXml(R.xml.all);
            String tag = "";
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals("name")) {
                            tag = "name";
                        } else if (xpp.getName().equals("category")) {
                            tag = "category";
                        } else if (xpp.getName().equals("count")) {
                            tag = "count";
                        } else if (xpp.getName().equals("coast")) {
                            tag = "coast";
                        } else if (xpp.getName().equals("edizm")) {
                            tag = "edizm";
                        } else {
                            tag = "";
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (tag.equals("name")) {
                            names.add(xpp.getText());
                        }
                        if (tag.equals("category")) {
                            categories.add(xpp.getText());
                        }
                        if (tag.equals("coast")) {
                            coasts.add(xpp.getText());
                        }
                        if (tag.equals("count")) {
                            counts.add(xpp.getText());
                        }
                        if (tag.equals("edizm")) {
                            edizms.add(xpp.getText());
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException | IOException ignored) {
        }

        ProductDAO dao = new ProductDAO(db);
        ListItemCategoryDAO categoryDAO = new ListItemCategoryDAO(db);
        for (int i = 0; i < names.size(); i++) {
            Product product = new Product(-1, names.get(i), counts.get(i), edizms.get(i), coasts.get(i), categoryDAO.getWithName(categories.get(i)), "", "", false);
            dao.updateOrAddToDB(product);
        }
    }
}
