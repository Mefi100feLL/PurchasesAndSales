package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.Category;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Category> {

    @Override
    public int compare(Category lhs, Category rhs) {
        if (lhs == null && rhs == null){
            return 0;
        } else if (rhs == null){
            return -1;
        } else if (lhs == null){
            return 1;
        }
        if (lhs.getType() < rhs.getType()) {
            return -1;
        } else if (lhs.getType() > rhs.getType()) {
            return 1;
        }
        return lhs.getId() - rhs.getId();
    }
}
