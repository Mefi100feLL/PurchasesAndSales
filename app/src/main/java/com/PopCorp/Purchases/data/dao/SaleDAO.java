package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.utils.DBList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaleDAO {

    public static final String TABLE_SALES = "Sales";

    public static final String KEY_SALE_ID = "id_sale";
    public static final String KEY_SALE_TITLE = "title";
    public static final String KEY_SALE_SUBTITLET = "subtitle";
    public static final String KEY_SALE_COAST = "coast";
    public static final String KEY_SALE_QUANTITY = "count";
    public static final String KEY_SALE_COAST_FOR_QUANTITY = "coast_for";
    public static final String KEY_SALE_IMAGE_URL = "image_url";
    public static final String KEY_SALE_CITY_ID = "city_id";
    public static final String KEY_SALE_SHOP = "shop";
    public static final String KEY_SALE_CATEGORY = "category";
    public static final String KEY_SALE_CATEGORY_TYPE = "category_type";
    public static final String KEY_SALE_COUNT_COMMENTS = "count_comments";
    public static final String KEY_SALE_PERIOD_BEGIN = "period_begin";
    public static final String KEY_SALE_PERIOD_FINISH = "period_finish";

    public static final String[] COLUMNS_SALES = new String[]{
            KEY_SALE_ID,
            KEY_SALE_TITLE,
            KEY_SALE_SUBTITLET,
            KEY_SALE_COAST,
            KEY_SALE_QUANTITY,
            KEY_SALE_COAST_FOR_QUANTITY,
            KEY_SALE_IMAGE_URL,
            KEY_SALE_CITY_ID,
            KEY_SALE_SHOP,
            KEY_SALE_CATEGORY,
            KEY_SALE_CATEGORY_TYPE,
            KEY_SALE_COUNT_COMMENTS,
            KEY_SALE_PERIOD_BEGIN,
            KEY_SALE_PERIOD_FINISH
    };

    public static final String CREATE_TABLE_SALES = "CREATE TABLE IF NOT EXISTS " + TABLE_SALES +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_SALE_ID + " integer, " +
            KEY_SALE_TITLE + " text, " +
            KEY_SALE_SUBTITLET + " text, " +
            KEY_SALE_COAST + " text, " +
            KEY_SALE_QUANTITY + " text, " +
            KEY_SALE_COAST_FOR_QUANTITY + " text, " +
            KEY_SALE_IMAGE_URL + " text, " +
            KEY_SALE_CITY_ID + " integer, " +
            KEY_SALE_SHOP + " integer, " +
            KEY_SALE_CATEGORY + " integer, " +
            KEY_SALE_CATEGORY_TYPE + " integer, " +
            KEY_SALE_COUNT_COMMENTS + " integer, " +
            KEY_SALE_PERIOD_BEGIN + " integer, " +
            KEY_SALE_PERIOD_FINISH + " integer);";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();
    private SameSaleDAO sameSaleDao = new SameSaleDAO();
    private SaleCommentDAO saleCommentDao = new SaleCommentDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private ShopDAO shopDAO = new ShopDAO();

    private String[] values(Sale sale) {
        return new String[]{
                String.valueOf(sale.getId()),
                sale.getTitle(),
                sale.getSubTitle(),
                sale.getCoast(),
                sale.getQuantity(),
                sale.getCoastForQuantity(),
                sale.getImage(),
                String.valueOf(sale.getCityId()),
                String.valueOf(sale.getShopId()),
                String.valueOf(sale.getCategoryId()),
                String.valueOf(sale.getCategoryType()),
                String.valueOf(sale.getCountComments()),
                String.valueOf(sale.getPeriodStart()),
                String.valueOf(sale.getPeriodEnd())
        };
    }

    public long updateOrAddToDB(Sale sale) {
        if (sale.getSameSales() != null && sale.getSameSales().size() > 0) {
            sameSaleDao.addAllSaleSames(sale.getSameSales());
        }
        if (sale.getComments() != null && sale.getComments().size() > 0) {
            saleCommentDao.addAllSaleComments(sale.getComments());
        }
        int countUpdated = db.update(TABLE_SALES, COLUMNS_SALES, KEY_SALE_CITY_ID + "=" + sale.getCityId() + " AND " + KEY_SALE_ID + "=" + sale.getId(), values(sale));
        if (countUpdated == 0) {
            return db.addRec(TABLE_SALES, COLUMNS_SALES, values(sale));
        }
        return countUpdated;
    }

    public int remove(Sale sale) {
        return db.deleteRows(TABLE_SALES, KEY_SALE_CITY_ID + "=" + sale.getCityId() + " AND " + KEY_SALE_ID + "=" + sale.getId());
    }

    public List<Sale> getSales(int regionId, int[] shops, int[] categories, int[] categoriesTypes) {
        ArrayList<Sale> result = new ArrayList<>();
        String selection = KEY_SALE_CITY_ID + "=" + regionId;
        if (shops.length == 0) {
            return result;
        } else {
            selection += " AND " + KEY_SALE_SHOP + " in " + Arrays.toString(shops).replace("[", "(").replace("]", ")");
        }
        if (categories.length == 0) {
            return result;
        } else {
            if (categories.length > 0) {
                selection += " AND (";
                for (int i = 0; i < categories.length; i++) {
                    if (i > 0) {
                        selection += " OR ";
                    }
                    int category = categories[i];
                    int type = categoriesTypes[i];
                    selection += "(" + KEY_SALE_CATEGORY + "=" + category + " AND " + KEY_SALE_CATEGORY_TYPE + "=" + type + ")";
                }
                selection += ")";
            }
        }
        Cursor cursor = db.getData(TABLE_SALES, COLUMNS_SALES, selection);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(get(cursor));
                while (cursor.moveToNext()) {
                    result.add(get(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    public Sale getSale(int regionId, int saleId) {
        Cursor cursor = db.getData(TABLE_SALES, COLUMNS_SALES, KEY_SALE_CITY_ID + "=" + regionId + " AND " + KEY_SALE_ID + "=" + saleId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return get(cursor);
            }
        }
        return null;
    }

    public void addAllSales(List<Sale> sales) {
        db.beginTransaction();
        try {
            for (Sale sale : sales) {
                updateOrAddToDB(sale);
            }
            db.successFull();
        } finally {
            db.endTransaction();
        }
    }

    public Sale get(Cursor cursor) {
        Sale result = new Sale(
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_TITLE)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_SUBTITLET)),
                cursor.getLong(cursor.getColumnIndex(KEY_SALE_PERIOD_BEGIN)),
                cursor.getLong(cursor.getColumnIndex(KEY_SALE_PERIOD_FINISH)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_COAST)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_QUANTITY)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_COAST_FOR_QUANTITY)),
                cursor.getString(cursor.getColumnIndex(KEY_SALE_IMAGE_URL)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_CITY_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_SHOP)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_CATEGORY)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_CATEGORY_TYPE)),
                cursor.getInt(cursor.getColumnIndex(KEY_SALE_COUNT_COMMENTS))

        );
        result.setShop(shopDAO.getShop(result));
        result.setCategory(categoryDAO.getCategory(result));
        result.setSameSales(sameSaleDao.getForSale(result.getCityId(), result.getId()));
        //result.setComments(saleCommentDao.getForSale(result.getId()));
        return result;
    }

    public List<Sale> getForShop(int shopId) {
        ArrayList<Sale> result = new ArrayList<>();
        Cursor cursor = db.getData(TABLE_SALES, COLUMNS_SALES, KEY_SALE_SHOP + "=" + shopId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(get(cursor));
                while (cursor.moveToNext()) {
                    result.add(get(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }
}
