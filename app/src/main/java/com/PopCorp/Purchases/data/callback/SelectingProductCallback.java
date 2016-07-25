package com.PopCorp.Purchases.data.callback;

import com.PopCorp.Purchases.data.model.Product;

public interface SelectingProductCallback extends RecyclerCallback<Product> {

    void onCountPlus(Product product);
    void onCountMinus(Product product);
}
