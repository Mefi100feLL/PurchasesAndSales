package com.PopCorp.Purchases.data.comparator;

import android.util.Log;

import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;

import java.util.Comparator;

public class ListItemDecoratorAlphabetAZComparator implements Comparator<ListItemDecorator> {

    @Override
    public int compare(ListItemDecorator lhs, ListItemDecorator rhs) {
        int result;
        if (lhs.getItem() != null && rhs.getItem() != null){
            result = lhs.getItem().getName().compareToIgnoreCase(rhs.getItem().getName());
        } else {
            result = lhs.getName().compareToIgnoreCase(rhs.getName());
        }
        //Log.d(getClass().getSimpleName(), "lhs=" + lhs.toString() + ", rhs=" + rhs.toString() + " result=" + result);
        return result;
    }
}
