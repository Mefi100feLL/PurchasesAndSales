package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.ShoppingList;

import java.util.List;

import rx.Observable;

public interface ShoppingListRepository {

    Observable<List<ShoppingList>> getData();
}
