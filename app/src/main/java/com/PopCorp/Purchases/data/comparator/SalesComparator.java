package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.Sale;

import java.util.Comparator;

public class SalesComparator implements Comparator<Sale> {

    @Override
    public int compare(Sale lhs, Sale rhs) {
        return lhs.getId() - rhs.getId();
    }
}
