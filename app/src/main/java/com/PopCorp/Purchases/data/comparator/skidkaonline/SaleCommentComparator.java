package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;

import java.util.Comparator;

public class SaleCommentComparator implements Comparator<SaleComment> {

    @Override
    public int compare(SaleComment lhs, SaleComment rhs) {
        if (lhs.equals(rhs)){
            return 0;
        }
        return -1;
    }
}
