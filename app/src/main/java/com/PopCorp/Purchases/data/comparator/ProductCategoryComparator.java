package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.Product;

import java.util.Comparator;

public class ProductCategoryComparator implements Comparator<Product> {

    @Override
    public int compare(Product lhs, Product rhs) {
        if (lhs.getName().equals(rhs.getName())) {
            return 0;
        }
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
            result = lhs.getName().compareToIgnoreCase(rhs.getName());
        }
        return result;
    }
}
