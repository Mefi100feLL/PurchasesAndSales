package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.Category;

import java.util.List;

import rx.Observable;

public interface CategoryRepository {

    Observable<List<Category>> getData();
}
