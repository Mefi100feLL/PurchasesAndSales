package com.PopCorp.Purchases.data.comparator;

import android.util.Log;

import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;

import java.util.Comparator;

public class ListItemDecoratorCategoryComparator implements Comparator<ListItemDecorator> {

    private Comparator<ListItemDecorator> child;

    public ListItemDecoratorCategoryComparator(Comparator<ListItemDecorator> child) {
        this.child = child;
    }

    @Override
    public int compare(ListItemDecorator lhs, ListItemDecorator rhs) {
        int result = 0;
        if (lhs.getCategory() != null && rhs.getCategory() != null){
            if (lhs.getCategory().getId() > rhs.getCategory().getId()){
                result = 1;
            } else if (lhs.getCategory().getId() < rhs.getCategory().getId()){
                result = -1;
            }
        } else if (lhs.getCategory() != null && rhs.getCategory() == null){
            result = 1;
        } else if (lhs.getCategory() == null && rhs.getCategory() != null){
            result = -1;
        }
        if (result == 0){
            result = child.compare(lhs, rhs);
        }
        //Log.d(getClass().getSimpleName(), "lhs=" + lhs.toString() + ", rhs=" + rhs.toString() + " result=" + result);
        return result;
    }

    public Comparator<ListItemDecorator> getChild() {
        return child;
    }

    public void setChild(Comparator<ListItemDecorator> child) {
        this.child = child;
    }
}
