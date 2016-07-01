package com.PopCorp.Purchases.data.callback;

import android.view.View;

public interface RecyclerCallback<T> {

    void onItemClicked(View view, T item);
    void onItemLongClicked(View view, T item);
    void onEmpty();
    void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener);
    void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener);
}
