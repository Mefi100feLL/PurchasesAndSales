package com.PopCorp.Purchases.presentation.view.adapter;

import android.support.v7.util.SortedList;

import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.presentation.decorator.SaleDecorator;
import com.PopCorp.Purchases.presentation.decorator.SaleShopDecorator;

import java.util.ArrayList;
import java.util.Comparator;

public class SalesInCategoryAdapter extends SalesAdapter {

    public SalesInCategoryAdapter(RecyclerCallback<Sale> callback, ArrayList<Sale> objects, Comparator<SaleDecorator> saleComparator) {
        super(callback, objects, saleComparator);
    }

    @Override
    protected ArrayList<Sale> getFilterResults(CharSequence constraint) {
        ArrayList<Sale> result = new ArrayList<>();
        if (constraint.equals("")){
            return objects;
        }
        String query = (String) constraint;
        if (query.startsWith("query=")){
            query = query.replace("query=", "").toLowerCase();
        } else {
            query = "";
        }
        for (Sale sale : objects) {
            if (!query.isEmpty() && sale.getTitle().toLowerCase().contains(query)){
                result.add(sale);
                continue;
            }
            if (constraint.equals(String.valueOf(sale.getShop().getName()))) {
                result.add(sale);
            }
        }
        return result;
    }

    @Override
    protected void update(ArrayList<Sale> sales) {
        publishItems.beginBatchedUpdates();
        for (Sale sale : sales) {
            SaleShopDecorator decorator = new SaleShopDecorator(sale, false, sale.getShop());
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

        ArrayList<SaleShopDecorator> headers = new ArrayList<>();
        for (Sale sale : sales) {
            SaleShopDecorator header = new SaleShopDecorator(sale, true, sale.getShop());
            if (!headers.contains(header)) {
                headers.add(header);
            }
        }
        for (SaleShopDecorator header : headers) {
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
        if (publishItems.size() == 0){
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }

    @Override
    public int indexOf(Sale sale) {
        SaleShopDecorator decorator = new SaleShopDecorator(sale, false, sale.getShop());
        return publishItems.indexOf(decorator);
    }
}
