package com.PopCorp.Purchases.data.callback;

import android.view.View;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemSale;

public interface ListItemCallback extends RecyclerCallback<ListItem> {

    void onItemSaleClicked(View v, ListItemSale sale);
}
