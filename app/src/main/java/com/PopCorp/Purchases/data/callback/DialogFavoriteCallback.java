package com.PopCorp.Purchases.data.callback;

import java.util.List;

public interface DialogFavoriteCallback<T> {

    void onSelected(List<T> result);
    void onCancel();
}
