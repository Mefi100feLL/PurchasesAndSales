package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.presentation.decorator.skidkaonline.SaleDecorator;

import java.util.Comparator;

public class SaleDecoratorComparator implements Comparator<SaleDecorator> {

    private SaleComparator comparator = new SaleComparator();

    @Override
    public int compare(SaleDecorator lhs, SaleDecorator rhs) {
        int result = 0;
        if (lhs.getCatalog() != null && rhs.getCatalog() != null) {
            result = lhs.getCatalog().compareToIgnoreCase(rhs.getCatalog());
        }
        if (result == 0) {
            if (!lhs.isHeader() && rhs.isHeader()) {
                result = 1;
            } else if (lhs.isHeader() && !rhs.isHeader()) {
                result = -1;
            }
        }
        if (result == 0 && lhs.getSale() != null && rhs.getSale() != null) {
            result = comparator.compare(lhs.getSale(), rhs.getSale());
        }
        return result;
    }
}
