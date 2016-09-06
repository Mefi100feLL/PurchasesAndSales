package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.presentation.decorator.SaleCategoryDecorator;
import com.PopCorp.Purchases.presentation.decorator.SaleDecorator;

import java.util.Comparator;

public class SalesCategoryComparator implements Comparator<SaleDecorator> {

    private CategoryComparator categoryComparator = new CategoryComparator();
    private SalesComparator salesComparator = new SalesComparator();

    @Override
    public int compare(SaleDecorator lhs, SaleDecorator rhs) {
        SaleCategoryDecorator left = (SaleCategoryDecorator) lhs;
        SaleCategoryDecorator right = (SaleCategoryDecorator) rhs;

        int result;
        result = categoryComparator.compare(left.getCategory(), right.getCategory());
        if (result == 0){
            if (left.isHeader() && !right.isHeader()){
                return -1;
            }
            if (!left.isHeader() && right.isHeader()){
                return 1;
            }
            if (!(left.isHeader() || right.isHeader())){
                result = salesComparator.compare(left.getSale(), right.getSale());
            }
        }

        return result;
    }
}