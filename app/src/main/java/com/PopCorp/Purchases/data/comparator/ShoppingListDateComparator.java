package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.ShoppingList;

import java.util.Comparator;

public class ShoppingListDateComparator implements Comparator<ShoppingList> {

    @Override
    public int compare(ShoppingList lhs, ShoppingList rhs) {
        int result = 0;
        if (lhs.getDateTime() > rhs.getDateTime()){
            result = 1;
        } else if (lhs.getDateTime() < rhs.getDateTime()){
            result = -1;
        }
        if (result == 0){
            if (lhs.getId() > rhs.getId()){
                result = 1;
            } else if (lhs.getId() < rhs.getId()){
                result = -1;
            }
        }
        return result;
    }
}
