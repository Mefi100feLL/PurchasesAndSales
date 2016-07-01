package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;

import java.util.Comparator;

public class ListItemDecoratorComparator implements Comparator<ListItemDecorator> {

    private Comparator<ListItem> comparator;

    @Override
    public int compare(ListItemDecorator lhs, ListItemDecorator rhs) {

        return 0;
    }
}
