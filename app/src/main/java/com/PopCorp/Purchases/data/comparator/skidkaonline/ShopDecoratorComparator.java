package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.presentation.decorator.skidkaonline.ShopDecorator;

import java.util.Comparator;

/**
 * Created by Александр on 06.07.2016.
 */
public class ShopDecoratorComparator implements Comparator<ShopDecorator> {

    @Override
    public int compare(ShopDecorator lhs, ShopDecorator rhs) {
        int result = 0;
        if (!lhs.isHeader() && rhs.isHeader()){
            result = -1;
        } else if(lhs.isHeader() && !rhs.isHeader()){
            result = 1;
        }
        if (result == 0){

        }
        return 0;
    }
}
