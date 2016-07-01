package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.SameSale;

import java.util.Comparator;

public class SameSaleComparator implements Comparator<SameSale> {

    @Override
    public int compare(SameSale lhs, SameSale rhs) {
        return lhs.getSaleId() - rhs.getSaleId();
    }
}
