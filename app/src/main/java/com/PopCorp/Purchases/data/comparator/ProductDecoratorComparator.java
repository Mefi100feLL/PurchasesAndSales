package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.presentation.decorator.ProductDecorator;

import java.util.Comparator;

public class ProductDecoratorComparator implements Comparator<ProductDecorator> {

    private Comparator<Product> comparator;

    @Override
    public int compare(ProductDecorator lhs, ProductDecorator rhs) {
        int result = 0;
        if (lhs.isHeader() || rhs.isHeader()) {
            if (lhs.getCategory() != null && rhs.getCategory() != null) {
                if (lhs.getCategory().getId() > rhs.getCategory().getId()) {
                    result = 1;
                } else if (lhs.getCategory().getId() < rhs.getCategory().getId()) {
                    result = -1;
                }
            } else if (lhs.getCategory() != null && rhs.getCategory() == null){
                result = 1;
            } else if (lhs.getCategory() == null && rhs.getCategory() != null){
                result = -1;
            }
        }
        if (result == 0){
            if (lhs.isHeader() && !rhs.isHeader()){
                return -1;
            }
            if (!lhs.isHeader() && rhs.isHeader()){
                return 1;
            }
            if (!(lhs.isHeader() || rhs.isHeader())){
                result = comparator.compare(lhs.getItem(), rhs.getItem());
            }
        }
        return result;
    }

    public Comparator<Product> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<Product> comparator) {
        this.comparator = comparator;
    }
}
