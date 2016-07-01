package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.Shop;

import java.util.Comparator;

public class ShopComparator implements Comparator<Shop> {

    @Override
    public int compare(Shop lhs, Shop rhs) {
        return lhs.getName().compareToIgnoreCase(rhs.getName());
    }
}
