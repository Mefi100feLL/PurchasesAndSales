package com.PopCorp.Purchases.data.callback;

import android.view.View;

public interface SaleMainCallback {

    void showComments();
    void showInfo();
    void openSameSale(View view, int saleId);
}
