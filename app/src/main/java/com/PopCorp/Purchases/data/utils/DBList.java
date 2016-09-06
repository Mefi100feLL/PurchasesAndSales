package com.PopCorp.Purchases.data.utils;

import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.AbstractList;
import java.util.List;

public class DBList<T> extends AbstractList<T> {

    public interface Factory<T> {
        T get(Cursor cursor);
    }

    public static <T> List<T> create(Cursor cursor, Factory<T> factory) {
        return new DBList<>(cursor, factory);
    }

    private final Cursor cursor;
    private final Factory<T> factory;

    private DBList(Cursor cursor, Factory<T> factory) {
        this.cursor = cursor;
        this.factory = factory;
    }

    @Override
    @Nullable
    public T get(int location) {
        if (cursor != null && cursor.moveToPosition(location)) {
            return factory.get(cursor);
        }
        return null;
    }

    @Override
    public int size() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public void close(){
        cursor.close();
    }
}