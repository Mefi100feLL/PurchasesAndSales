package com.PopCorp.Purchases.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DB {

    public static final String KEY_ID = "_id";


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private SQLiteDatabase db;

    private static DB instance;

    private DB() {
    }

    public static DB getInstance() {
        return instance;
    }

    public static void setInstance(Context context) {
        instance = new DB();
        instance.open(context);
    }

    public DB(SQLiteDatabase db){
        this.db = db;
    }

    private void open(Context context) {
        DBHelper DBHelper = new DBHelper(context);
        try {
            db = DBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = DBHelper.getReadableDatabase();
        }
    }

    public Cursor getAllData(String table) {
        try {
            return db.query(table, null, null, null, null, null, null);
        } catch (SQLiteException e) {
            return null;
        }
    }

    public long addRec(String table, String[] columns, String[] values) {
        try {
            ContentValues cv = new ContentValues();
            for (int i = 0; i < columns.length; i++) {
                cv.put(columns[i], values[i]);
            }
            return db.insert(table, null, cv);
        } catch (SQLiteException e) {
            return -1;
        }
    }

    public Cursor getData(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        try {
            return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        } catch (SQLiteException e) {
            return null;
        }
    }

    public Cursor getData(String table, String[] columns, String selection) {
        try {
            return db.query(table, columns, selection, null, null, null, null);
        } catch (SQLiteException e) {
            return null;
        }
    }

    public Cursor getData(String table, String selection) {
        try {
            return db.query(table, null, selection, null, null, null, null);
        } catch (SQLiteException e) {
            return null;
        }
    }

    public int deleteRows(String table, String uslovie) throws SQLiteException, IllegalStateException {
        return db.delete(table, uslovie, null);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }

    public int update(String table, String[] columns, String uslovie, String[] values) {
        ContentValues cv = new ContentValues();
        for (int i = 0; i < columns.length; i++) {
            cv.put(columns[i], values[i]);
        }
        return db.update(table, cv, uslovie, null);
    }

    public void beginTransaction(){
        db.beginTransaction();
    }

    public void endTransaction(){
        db.endTransaction();
    }

    public void successFull(){
        db.setTransactionSuccessful();
    }

    static public String trans(String s) {
        String res = "a";
        for (byte i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                res += "_";
            } else {
                res += s.charAt(i);
            }
        }
        return res;
    }

    static public String untrans(String s) {
        String res = "";
        for (byte i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '_') {
                res += " ";
            } else {
                res += s.charAt(i);
            }
        }
        return res.substring(1);
    }
}
