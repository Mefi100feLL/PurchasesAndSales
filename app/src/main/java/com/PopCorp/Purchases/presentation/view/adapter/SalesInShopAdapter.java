package com.PopCorp.Purchases.presentation.view.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;

import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.presentation.decorator.SaleCategoryDecorator;
import com.PopCorp.Purchases.presentation.decorator.SaleDecorator;

import java.util.ArrayList;
import java.util.Comparator;

public class SalesInShopAdapter extends SalesAdapter {

    public SalesInShopAdapter(RecyclerCallback<Sale> callback, ArrayList<Sale> objects, Comparator<SaleDecorator> saleComparator) {
        super(callback, objects, saleComparator);
    }

    @Override
    protected ArrayList<Sale> getFilterResults(CharSequence constraint) {
        ArrayList<Sale> result = new ArrayList<>();
        if (constraint.equals("")){
            return objects;
        }
        for (Sale sale : objects){
            if (constraint.equals(String.valueOf(sale.getCategory().getName()))){
                result.add(sale);
            }
        }
        return result;
    }

    @Override
    protected void update(ArrayList<Sale> sales) {
        publishItems.beginBatchedUpdates();
        for (Sale sale : sales) {
            SaleCategoryDecorator decorator = new SaleCategoryDecorator(sale, false, sale.getCategory());
            int index = publishItems.indexOf(decorator);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(decorator);
            } else {
                publishItems.updateItemAt(index, decorator);
            }
        }

        ArrayList<SaleDecorator> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            SaleDecorator decorator = publishItems.get(i);
            if (!decorator.isHeader() && !sales.contains(decorator.getSale())) {
                arrayForRemoving.add(decorator);
            }
        }
        for (SaleDecorator decorator : arrayForRemoving) {
            publishItems.remove(decorator);
        }

        ArrayList<SaleCategoryDecorator> headers = new ArrayList<>();
        for (Sale sale : sales) {
            SaleCategoryDecorator header = new SaleCategoryDecorator(sale, true, sale.getCategory());
            if (!headers.contains(header)) {
                headers.add(header);
            }
        }
        for (SaleCategoryDecorator header : headers) {
            if (publishItems.indexOf(header) == SortedList.INVALID_POSITION) {
                publishItems.add(header);
            }
        }
        arrayForRemoving.clear();
        for (int i = 0; i < publishItems.size(); i++) {
            SaleDecorator decorator = publishItems.get(i);
            if (decorator.isHeader() && !headers.contains(decorator)) {
                arrayForRemoving.add(decorator);
            }
        }
        for (SaleDecorator header : arrayForRemoving) {
            publishItems.remove(header);
        }
        publishItems.endBatchedUpdates();
    }

    @Override
    public int indexOf(Sale sale){
        SaleCategoryDecorator decorator = new SaleCategoryDecorator(sale, false, sale.getCategory());
        return publishItems.indexOf(decorator);
    }
}