package com.PopCorp.Purchases.data.comparator;

import android.util.Log;

import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;

import java.util.Comparator;

public class ListItemDecoratorAddedComparator implements Comparator<ListItemDecorator> {

    @Override
    public int compare(ListItemDecorator lhs, ListItemDecorator rhs) {
        int result = 0;
        if (lhs.getItem() != null && rhs.getItem() != null) {
            if (lhs.getItem().getId() > rhs.getItem().getId()) {
                result = 1;
            } else if (lhs.getItem().getId() < rhs.getItem().getId()) {
                result = -1;
            }
        } else {
            result = lhs.getName().compareToIgnoreCase(rhs.getName());
        }
        //Log.d(getClass().getSimpleName(), "lhs=" + lhs.toString() + ", rhs=" + rhs.toString() + " result=" + result);
        return result;
    }
}
