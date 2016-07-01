package com.PopCorp.Purchases.data.callback;

import com.PopCorp.Purchases.data.model.ShoppingList;

public interface ShareListCallback {

    void shareAsSMS(ShoppingList list);
    void shareAsEmail(ShoppingList list);
    void shareAsText(ShoppingList list);
}
