package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Sale;

import java.util.Comparator;

/**
 * Created by Александр on 06.07.2016.
 */
public class SaleComparator implements Comparator<Sale> {

    @Override
    public int compare(Sale lhs, Sale rhs) {
        int result = lhs.getCatalog().compareToIgnoreCase(rhs.getCatalog());
        if (result == 0){
            if (lhs.getId() > rhs.getId()){
                result = -1;
            } else if (lhs.getId() < rhs.getId()){
                result = 1;
            }
        }
        return result;
    }
}
