package com.PopCorp.Purchases.data.dao;

import android.database.Cursor;

import com.PopCorp.Purchases.data.db.DB;
import com.PopCorp.Purchases.data.model.SaleComment;

import java.util.ArrayList;
import java.util.List;

public class SaleCommentDAO {

    public static final String TABLE_SALES_COMMENTS = "sales_comments";

    public static final String KEY_COMMENT_SALE_ID = "id";
    public static final String KEY_COMMENT_AUTHOR = "author";
    public static final String KEY_COMMENT_WHOM = "whom";
    public static final String KEY_COMMENT_TEXT = "text";
    public static final String KEY_COMMENT_DATE_TIME = "date_time";

    public static final String[] COLUMNS_COMMENTS = new String[]{
            KEY_COMMENT_SALE_ID,
            KEY_COMMENT_AUTHOR,
            KEY_COMMENT_WHOM,
            KEY_COMMENT_TEXT,
            KEY_COMMENT_DATE_TIME
    };

    public static final String CREATE_TABLE_SALES_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TABLE_SALES_COMMENTS +
            "( " + DB.KEY_ID + " integer primary key autoincrement, " +
            KEY_COMMENT_SALE_ID + " integer, " +
            KEY_COMMENT_AUTHOR + " text, " +
            KEY_COMMENT_WHOM + " text, " +
            KEY_COMMENT_TEXT + " text, " +
            KEY_COMMENT_DATE_TIME + " integer);";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB db = DB.getInstance();

    public long updateOrAddToDB(SaleComment saleComment) {
        String[] values = new String[]{
                String.valueOf(saleComment.getSaleId()),
                saleComment.getAuthor(),
                saleComment.getWhom(),
                saleComment.getText(),
                String.valueOf(saleComment.getDateTime())
        };
        int countUpdated = db.update(TABLE_SALES_COMMENTS, COLUMNS_COMMENTS, selection(saleComment), values);
        if (countUpdated == 0) {
            return db.addRec(TABLE_SALES_COMMENTS, COLUMNS_COMMENTS, values);
        }
        return countUpdated;
    }

    private String selection(SaleComment saleComment) {
        return KEY_COMMENT_SALE_ID + "=" + saleComment.getSaleId() + " AND " +
                KEY_COMMENT_AUTHOR + "='" + saleComment.getAuthor() + "' AND " +
                KEY_COMMENT_DATE_TIME + "=" + saleComment.getDateTime();
    }

    public int remove(SaleComment saleComment) {
        return db.deleteRows(TABLE_SALES_COMMENTS, selection(saleComment));
    }

    public List<SaleComment> getForSale(int saleId) {
        ArrayList<SaleComment> result = new ArrayList<>();
        Cursor cursor = db.getData(TABLE_SALES_COMMENTS, COLUMNS_COMMENTS, KEY_COMMENT_SALE_ID + "=" + saleId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.add(getComment(cursor));
                while (cursor.moveToNext()) {
                    result.add(getComment(cursor));
                }
            }
            cursor.close();
        }
        return result;
    }

    private SaleComment getComment(Cursor cursor) {
        return new SaleComment(
                cursor.getInt(cursor.getColumnIndex(KEY_COMMENT_SALE_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_COMMENT_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(KEY_COMMENT_WHOM)),
                cursor.getString(cursor.getColumnIndex(KEY_COMMENT_TEXT)),
                cursor.getLong(cursor.getColumnIndex(KEY_COMMENT_DATE_TIME))
        );
    }

    public void addAllSaleComments(List<SaleComment> comments) {
        for (SaleComment saleComment : comments) {
            updateOrAddToDB(saleComment);
        }
    }

    public void removeForSale(int saleId) {
        db.deleteRows(TABLE_SALES_COMMENTS, KEY_COMMENT_SALE_ID + "=" + saleId);
    }
}
