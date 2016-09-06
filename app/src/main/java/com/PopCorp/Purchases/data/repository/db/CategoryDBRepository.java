package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.CategoryDAO;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.domain.repository.CategoryRepository;

import java.util.List;

import rx.Observable;

public class CategoryDBRepository implements CategoryRepository {

    private CategoryDAO dao = new CategoryDAO();

    @Override
    public Observable<List<Category>> getData() {
        return Observable.just(dao.getAllCategories());
    }

    public void addAllCategories(List<Category> categories) {
        dao.addAllCategories(categories);
    }

    public void remove(Category category) {
        dao.remove(category);
    }
}
