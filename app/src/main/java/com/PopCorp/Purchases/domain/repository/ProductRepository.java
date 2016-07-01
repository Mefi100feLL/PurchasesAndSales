package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.Product;

import java.util.List;

import rx.Observable;

public interface ProductRepository {

    Observable<List<Product>> getAllProducts();
}
