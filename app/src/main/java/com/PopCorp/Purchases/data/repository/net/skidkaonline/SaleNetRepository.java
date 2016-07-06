package com.PopCorp.Purchases.data.repository.net.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.skidkaonline.SaleRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public class SaleNetRepository implements SaleRepository {

    private API api = APIFactory.getAPI();

    @Override
    public Observable<List<Sale>> getData(int cityId, String shopUrl) {
        return api.getSkidkaonlineSales(cityId, shopUrl);
    }

    @Override
    public Observable<Sale> getSale(int cityId, int saleId) {
        return api.getSkidkaonlineSale(cityId, saleId);
    }
}
