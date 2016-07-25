package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;

import java.util.Comparator;

public class SaleCommentComparator implements Comparator<SaleComment> {

    @Override
    public int compare(SaleComment lhs, SaleComment rhs) {
        int result = 0;
        if (lhs.getDateTime() > rhs.getDateTime()) {
            result = 1;
        } else if (lhs.getDateTime() < rhs.getDateTime()) {
            result = -1;
        }
        if (result == 0 && !lhs.getAuthor().equals(rhs.getAuthor())) {
            result = lhs.getAuthor().compareToIgnoreCase(rhs.getAuthor());
        }
        if (result == 0 && !lhs.getText().equals(rhs.getText())) {
            result = lhs.getText().compareToIgnoreCase(rhs.getText());
        }
        return result;
    }
}
