package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.ShoppingList;

import java.util.Comparator;

public class ShoppingListAlphabetComparator implements Comparator<ShoppingList> {

    @Override
    public int compare(ShoppingList lhs, ShoppingList rhs) {
        if (lhs.getId() == rhs.getId()){
            return 0;
        }
        int result = lhs.getName().compareToIgnoreCase(rhs.getName());
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
