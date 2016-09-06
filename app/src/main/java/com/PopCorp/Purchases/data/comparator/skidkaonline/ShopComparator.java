package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Shop;

import java.util.Comparator;

/**
 * Created by Александр on 06.07.2016.
 */
public class ShopComparator implements Comparator<Shop> {

    @Override
    public int compare(Shop lhs, Shop rhs) {
        int result = 0;
        if (lhs.getCategory() != null && rhs.getCategory() != null){
            result = lhs.getCategory().getName().compareToIgnoreCase(rhs.getCategory().getName());
        }
        if (result == 0){
            result = lhs.getName().compareToIgnoreCase(rhs.getName());
        }
        return result;
    }
}
