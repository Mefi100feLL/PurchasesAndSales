package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.presentation.decorator.SaleDecorator;
import com.PopCorp.Purchases.presentation.decorator.SaleShopDecorator;

import java.util.Comparator;

public class SalesShopComparator implements Comparator<SaleDecorator> {

    private ShopComparator shopComparator = new ShopComparator();
    private SalesComparator salesComparator = new SalesComparator();

    @Override
    public int compare(SaleDecorator lhs, SaleDecorator rhs) {
        SaleShopDecorator left = (SaleShopDecorator) lhs;
        SaleShopDecorator right = (SaleShopDecorator) rhs;

        int result;
        result = shopComparator.compare(left.getShop(), right.getShop());
        if (result == 0){
            if (left.isHeader() && !right.isHeader()){
                return -1;
            }
            if (!left.isHeader() && right.isHeader()){
                return 1;
            }
            if (!(left.isHeader() || right.isHeader())){
                salesComparator.compare(left.getSale(), right.getSale());
            }
        }

        return result;
    }
}