package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.ProductDAO;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.domain.repository.ProductRepository;

import java.util.List;

import rx.Observable;

public class ProductDBRepository implements ProductRepository {

    private ProductDAO dao = new ProductDAO();

    @Override
    public Observable<List<Product>> getAllProducts() {
        return Observable.just(dao.getAllProducts());
    }

    public void addItem(Product product) {
        dao.updateOrAddToDB(product);
    }
}
