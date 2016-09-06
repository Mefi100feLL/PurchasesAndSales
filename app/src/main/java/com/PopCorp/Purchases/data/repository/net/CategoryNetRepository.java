package com.PopCorp.Purchases.data.repository.net;

import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.CategoryRepository;

import java.util.List;

import rx.Observable;

public class CategoryNetRepository implements CategoryRepository {

    API api = APIFactory.getAPI();

    @Override
    public Observable<List<Category>> getData() {
        return api.getCategories()
                .flatMap(UniversalDTO::getData);
    }
}
