package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.Category;

import java.util.ArrayList;

public interface SalesInShopView extends SalesView {

    void showCategoriesEmpty();

    void showCategoriesForSelectingFavorites(ArrayList<Category> categories);

    void showFavoriteCategoriesEmpty();
}
