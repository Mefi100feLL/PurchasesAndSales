package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.Shop;

import java.util.ArrayList;

public interface SalesInCategoryView extends SalesView {

    void showShopsEmpty();

    void showShopsForSelectingFavorites(ArrayList<Shop> shops);

    void showFavoriteShopsEmpty();
}
