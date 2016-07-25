package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.Product;

import java.util.Comparator;

public class ProductAlphabetComparator implements Comparator<Product> {

    @Override
    public int compare(Product lhs, Product rhs) {
        return lhs.getName().compareToIgnoreCase(rhs.getName());
    }
}
