package com.PopCorp.Purchases.data.callback;

import android.view.View;

import com.PopCorp.Purchases.data.model.ShoppingList;

public interface ShoppingListCallback extends RecyclerCallback<ShoppingList> {

    void onOverflowClicked(View v, ShoppingList list);
}
