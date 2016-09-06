package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.presentation.decorator.skidkaonline.ShopDecorator;

import java.util.Comparator;

public class ShopDecoratorComparator implements Comparator<ShopDecorator> {

    private ShopComparator comparator = new ShopComparator();

    @Override
    public int compare(ShopDecorator lhs, ShopDecorator rhs) {
        int result = 0;
        if (lhs.getCategory() != null && rhs.getCategory() != null) {
            result = lhs.getCategory().getName().compareToIgnoreCase(rhs.getCategory().getName());
        }
        if (result == 0) {
            if (!lhs.isHeader() && rhs.isHeader()) {
                result = 1;
            } else if (lhs.isHeader() && !rhs.isHeader()) {
                result = -1;
            }
        }
        if (result == 0 && lhs.getShop() != null && rhs.getShop() != null) {
            result = comparator.compare(lhs.getShop(), rhs.getShop());
        }
        return result;
    }
}
