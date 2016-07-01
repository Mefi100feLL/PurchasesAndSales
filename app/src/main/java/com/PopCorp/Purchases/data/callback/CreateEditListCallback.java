package com.PopCorp.Purchases.data.callback;

import com.PopCorp.Purchases.data.model.ShoppingList;

public interface CreateEditListCallback {

    void onListEdited(ShoppingList list, String name, String currency);
    void addNewList(String name, String currency);
}
