package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;

import java.util.Comparator;

public class ListItemDecoratorBuyedComparator implements Comparator<ListItemDecorator> {

    private Comparator<ListItemDecorator> child;

    public ListItemDecoratorBuyedComparator(Comparator<ListItemDecorator> child) {
        this.child = child;
    }

    @Override
    public int compare(ListItemDecorator lhs, ListItemDecorator rhs) {
        int result = 0;
        boolean lhsBuyed = lhs.isHeader() ? lhs.isBuyed() : lhs.getItem().isBuyed();
        boolean rhsBuyed = rhs.isHeader() ? rhs.isBuyed() : rhs.getItem().isBuyed();
        if (lhsBuyed && !rhsBuyed) {
            result = 1;
        } else if (!lhsBuyed && rhsBuyed) {
            result = -1;
        }
        if (result == 0) {
            result = child.compare(lhs, rhs);
        }
        return result;
    }

    public Comparator<ListItemDecorator> getChild() {
        return child;
    }

    public void setChild(Comparator<ListItemDecorator> child) {
        this.child = child;
    }
}
