package com.PopCorp.Purchases.data.callback;

public interface FavoriteRecyclerCallback<T> extends RecyclerCallback<T> {

    void onFavoriteClicked(T item);
}
