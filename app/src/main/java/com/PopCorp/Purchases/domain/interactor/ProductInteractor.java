package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.data.repository.db.ProductDBRepository;

import java.util.List;

import rx.Observable;

public class ProductInteractor {

    private ProductDBRepository dbRepository = new ProductDBRepository();

    public Observable<List<Product>> getAllProducts(){
        return dbRepository.getAllProducts();
    }
}
