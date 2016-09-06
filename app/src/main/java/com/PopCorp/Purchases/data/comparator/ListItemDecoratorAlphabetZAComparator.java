package com.PopCorp.Purchases.data.comparator;

import android.util.Log;

import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;

import java.util.Comparator;

public class ListItemDecoratorAlphabetZAComparator implements Comparator<ListItemDecorator> {

    @Override
    public int compare(ListItemDecorator lhs, ListItemDecorator rhs) {
        int result;
        if (lhs.getItem() != null && rhs.getItem() != null){
            result = rhs.getItem().getName().compareToIgnoreCase(lhs.getItem().getName());
        } else {
            result = rhs.getName().compareToIgnoreCase(lhs.getName());
        }
        //Log.d(getClass().getSimpleName(), "lhs=" + lhs.toString() + ", rhs=" + rhs.toString() + " result=" + result);
        return result;
    }
}
