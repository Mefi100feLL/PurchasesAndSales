package com.PopCorp.Purchases.data.repository.net;

import com.PopCorp.Purchases.data.dao.CategoryDAO;
import com.PopCorp.Purchases.data.dao.ShopDAO;
import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.SaleRepository;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class SaleNetRepository implements SaleRepository {

    API api = APIFactory.getAPI();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private ShopDAO shopDAO = new ShopDAO();

    @Override
    public Observable<List<Sale>> getData(int regionId, int[] shops, int[] categories, int[] categoriesTypes) {
        return api.getSales(regionId, Arrays.toString(shops), Arrays.toString(categories), Arrays.toString(categoriesTypes))
                .flatMap(UniversalDTO::getData)
                .map(sales -> {
                    for (Sale sale : sales){
                        sale.setShop(shopDAO.getShop(sale));
                        sale.setCategory(categoryDAO.getCategory(sale));
                    }
                    return sales;
                });
    }

    @Override
    public Observable<Sale> getSale(int regionId, int cityId) {
        return api.getSale(regionId, cityId)
                .flatMap(UniversalDTO::getData);
    }
}
